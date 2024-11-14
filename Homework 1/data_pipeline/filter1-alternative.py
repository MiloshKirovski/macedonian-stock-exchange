from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from bs4 import BeautifulSoup
import time

start_time = time.time()

options = webdriver.ChromeOptions()
options.add_argument('--headless')
options.add_argument('--disable-gpu')
options.add_argument('--no-sandbox')

driver = webdriver.Chrome(options=options)
driver.get("https://www.mse.mk/en/stats/current-schedule")


def extract_codes():

    soup = BeautifulSoup(driver.page_source, "html.parser")
    codes = {
        link.text.strip()
        for link in soup.select("td a[href^='/en/symbol/']")
        if link.text.strip() and not any(char.isdigit() for char in link.text.strip())
    }
    return codes


tab_selectors = [
    "results-fixingWith20PercentLimit",
    "results-continuousTradingMode",
    "results-fixingWithoutLimit"
]

all_codes = set()

for tab in tab_selectors:
    try:
        WebDriverWait(driver, 5).until(
            EC.element_to_be_clickable((By.CSS_SELECTOR, f"a[href='#{tab}']"))
        ).click()

        WebDriverWait(driver, 5).until(
            EC.presence_of_element_located((By.ID, tab))
        )

        all_codes.update(extract_codes())

    except Exception as e:
        print(f"Error loading tab {tab}: {e}")

driver.quit()
print(time.time()-start_time)
print(list(all_codes))