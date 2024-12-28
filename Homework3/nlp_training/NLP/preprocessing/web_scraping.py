from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from selenium.common.exceptions import TimeoutException, WebDriverException
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
import pandas as pd
import time
import logging
import os
import pdfplumber
import docx
import pythoncom
import win32com.client
import re

# Set up logging
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')


def setup_driver(download_folder):
    options = webdriver.ChromeOptions()
    options.add_argument('--disable-gpu')
    options.add_argument('--no-sandbox')
    options.add_argument('--disable-dev-shm-usage')
    options.add_argument('--ignore-certificate-errors')

    prefs = {
        "download.default_directory": download_folder,
        "download.prompt_for_download": False,
        "download.directory_upgrade": True,
        "plugins.plugins_disabled": ["Chrome PDF Viewer"]
    }
    options.add_experimental_option("prefs", prefs)

    driver = webdriver.Chrome(
        service=Service(ChromeDriverManager().install()),
        options=options
    )
    return driver


def handle_cookie_consent(driver):
    try:
        consent_button = WebDriverWait(driver, 10).until(
            EC.element_to_be_clickable((By.XPATH, "//button[contains(text(), 'Се согласувам')]"))
        )
        consent_button.click()
        time.sleep(1)
        logging.info("Cookie consent handled.")
    except TimeoutException:
        logging.warning("No cookie consent banner appeared.")


def wait_for_download_complete(download_folder, timeout=30):
    start_time = time.time()
    while time.time() - start_time < timeout:

        temp_files = [f for f in os.listdir(download_folder)
                      if f.endswith(('.crdownload', '.tmp', '.part'))]
        if not temp_files:

            files = os.listdir(download_folder)
            if files:
                newest_file = max([os.path.join(download_folder, f) for f in files],
                                  key=os.path.getctime)
                return newest_file
        time.sleep(0.5)
    raise TimeoutException("Download did not complete within the specified timeout")


def find_and_download_file(driver, download_folder):
    file_link = ""
    file_path = ""

    try:

        download_elements = driver.find_elements(By.XPATH, "//div[contains(@title, 'Превземи')]")

        if download_elements:
            download_element = download_elements[0]
            file_name = download_element.get_attribute('title')

            timestamp = int(time.time())
            clean_filename = re.sub(r'[^\w\-_\. ]', '_', file_name)
            unique_filename = f"{timestamp}_{clean_filename}"

            driver.execute_script("arguments[0].click();", download_element)

            logging.info(f"Clicked download element: {file_name}")

            temp_file = wait_for_download_complete(download_folder)

            file_extension = os.path.splitext(temp_file)[1]
            file_path = os.path.join(download_folder, unique_filename + file_extension)

            os.rename(temp_file, file_path)
            file_link = file_name

            logging.info(f"File downloaded: {file_path}")

    except Exception as e:
        logging.error(f"Error downloading file: {str(e)}")

    return file_link, file_path


def scrape_table_data(driver, download_folder):
    all_data = []
    current_page = 1
    current_row_index = 0
    processed_items = set()

    while True:
        logging.info(f"Processing page {current_page}, starting from row {current_row_index}")
        try:

            if current_page > 1:
                try:

                    pagination = WebDriverWait(driver, 10).until(
                        EC.presence_of_element_located((By.CLASS_NAME, "pagination"))
                    )

                    page_links = pagination.find_elements(By.CLASS_NAME, "page-link")
                    for link in page_links:
                        if link.text.strip() == str(current_page):
                            driver.execute_script("arguments[0].click();", link)
                            time.sleep(2)
                            break
                except Exception as e:
                    logging.error(f"Error navigating to page {current_page}: {e}")
                    break

            table = WebDriverWait(driver, 10).until(
                EC.presence_of_element_located((By.CLASS_NAME, "table-hover"))
            )
            rows = table.find_elements(By.TAG_NAME, "tr")[1:]

            while current_row_index < len(rows):
                try:
                    row = rows[current_row_index]
                    cols = row.find_elements(By.TAG_NAME, "td")

                    if not cols or len(cols) < 3:
                        current_row_index += 1
                        continue

                    company = cols[0].text.strip()
                    title = cols[1].text.strip()
                    date_time = cols[2].text.strip()
                    entry_id = f"{company}_{title}_{date_time}"

                    if entry_id in processed_items:
                        current_row_index += 1
                        continue

                    current_entry = {
                        'company': company,
                        'title': title,
                        'date_time': date_time
                    }

                    title_cell = cols[1]
                    driver.execute_script("arguments[0].click();", title_cell)
                    time.sleep(2)

                    page_text = ""
                    file_link = ""
                    file_path = ""

                    try:

                        WebDriverWait(driver, 10).until(
                            EC.presence_of_element_located((By.CLASS_NAME, "text-left"))
                        )
                        page_text = driver.find_element(By.CLASS_NAME, "text-left").text.strip()

                        file_link, file_path = find_and_download_file(driver, download_folder)

                    except Exception as e:
                        logging.warning(f"Error extracting details: {str(e)}")

                    all_data.append({
                        'Company': current_entry['company'],
                        'Title': current_entry['title'],
                        'DateTime': current_entry['date_time'],
                        'PageText': page_text,
                        'FileLink': file_link,
                        'FilePath': file_path
                    })

                    processed_items.add(entry_id)

                    logging.info(f"Processed page {current_page}, row {current_row_index}: {company} - {title}")

                    driver.back()
                    time.sleep(2)

                    if current_page > 1:
                        pagination = WebDriverWait(driver, 10).until(
                            EC.presence_of_element_located((By.CLASS_NAME, "pagination"))
                        )
                        page_links = pagination.find_elements(By.CLASS_NAME, "page-link")
                        for link in page_links:
                            if link.text.strip() == str(current_page):
                                driver.execute_script("arguments[0].click();", link)
                                time.sleep(2)
                                break

                    table = WebDriverWait(driver, 10).until(
                        EC.presence_of_element_located((By.CLASS_NAME, "table-hover"))
                    )
                    rows = table.find_elements(By.TAG_NAME, "tr")[1:]

                    current_row_index += 1

                except Exception as e:
                    logging.error(f"Error processing row {current_row_index} on page {current_page}: {str(e)}")
                    current_row_index += 1
                    continue

            current_row_index = 0

            try:
                pagination = WebDriverWait(driver, 10).until(
                    EC.presence_of_element_located((By.CLASS_NAME, "pagination"))
                )
                next_button = pagination.find_elements(By.XPATH, ".//li[contains(@class, 'page-item')]")[-1]

                if 'disabled' in next_button.get_attribute('class'):
                    logging.info("Reached last page")
                    break

                driver.execute_script("arguments[0].click();", next_button)
                current_page += 1
                time.sleep(2)

            except Exception as e:
                logging.error(f"Error navigating to next page: {str(e)}")
                break

        except Exception as e:
            logging.error(f"Error processing page: {str(e)}")
            break

    return all_data


def main():
    try:

        download_folder = os.path.abspath("downloaded_files")
        os.makedirs(download_folder, exist_ok=True)

        driver = setup_driver(download_folder)
        driver.get("https://seinet.com.mk/documents/")
        handle_cookie_consent(driver)

        data = scrape_table_data(driver, download_folder)

        df = pd.DataFrame(data)
        df.to_csv('sei_net_data.csv', index=False, encoding='utf-8-sig')



    except Exception as e:
        logging.error(f"Script execution failed: {str(e)}")
    finally:
        if 'driver' in locals():
            driver.quit()


if __name__ == "__main__":
    main()
