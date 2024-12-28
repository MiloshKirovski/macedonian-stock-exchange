import pandas as pd
import chardet
from PyPDF2 import PdfReader


def detect_encoding(file_path):
    with open(file_path, 'rb') as f:
        result = chardet.detect(f.read())
    return result.get('encoding', 'utf-8')


def extract_text_from_pdf(file_path):
    try:
        reader = PdfReader(file_path)
        text = ''.join(page.extract_text() for page in reader.pages if page.extract_text())
        return text
    except Exception as e:
        return f"Error reading PDF: {e}"


def process_files_and_update_csv(csv_file):
    df = pd.read_csv(csv_file)

    if 'PageText' not in df.columns:
        df['PageText'] = ''

    for idx, row in df.iterrows():
        file_path = row.get('FilePath')

        if pd.isna(file_path) or not os.path.exists(file_path):
            continue

        encoding = detect_encoding(file_path)

        if file_path.lower().endswith('.pdf'):
            page_text = extract_text_from_pdf(file_path)
        else:
            try:
                with open(file_path, 'r', encoding=encoding, errors='ignore') as file:
                    page_text = file.read()
            except Exception as e:
                print(f"Error reading file at {file_path}: {e}")
                continue

        df.at[idx, 'PageText'] = page_text

    updated_csv_path = 'updated_' + os.path.basename(csv_file)
    df.to_csv(updated_csv_path, index=False, encoding='utf-8-sig')


try:
    csv_file_path = 'sei_net_data.csv'
    process_files_and_update_csv(csv_file_path)
except Exception as e:
    print(f"Error processing CSV: {e}")
