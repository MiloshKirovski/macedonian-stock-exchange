import pandas as pd
import re
from textblob import TextBlob
from deep_translator import GoogleTranslator
from tqdm import tqdm


def clean_text(text):
    if not isinstance(text, str):
        return ""
    text = re.sub(r'[^a-zA-Z\s]', '', text)
    text = text.lower().strip()
    return text


def analyze_sentiment(text):
    try:

        translated = GoogleTranslator(source='mk', target='en').translate(text)
        cleaned_text = clean_text(translated)

        analysis = TextBlob(cleaned_text)
        polarity = analysis.sentiment.polarity

        if polarity > 0.1:
            return "POSITIVE"
        elif polarity < -0.1:
            return "NEGATIVE"
        else:
            return "NEUTRAL"
    except Exception as e:
        print(f"Error in sentiment analysis: {e}")
        return "NEUTRAL"


def calculate_company_sentiments(csv_file):
    df = pd.read_csv(csv_file)

    results = {}

    companies = df['Company'].unique()

    for company in tqdm(companies, desc="Processing companies"):
        company_docs = df[df['Company'] == company]

        total_docs = len(company_docs)
        if total_docs == 0:
            continue

        sentiments = []

        for text in company_docs['PageText']:
            sentiment = analyze_sentiment(text)
            sentiments.append(sentiment)

        positive_count = sentiments.count("POSITIVE")
        negative_count = sentiments.count("NEGATIVE")
        neutral_count = sentiments.count("NEUTRAL")

        results[company] = {
            'Total number of notifications': total_docs,
            'Positive %': round((positive_count / total_docs) * 100, 2),
            'Negative %': round((negative_count / total_docs) * 100, 2),
            'Neutral %': round((neutral_count / total_docs) * 100, 2)
        }

    results_df = pd.DataFrame.from_dict(results, orient='index')
    results_df = results_df.sort_values('Total number of notifications', ascending=False)
    return results_df


def create_sentiment_summary(input_csv, output_csv):
    try:
        results_df = calculate_company_sentiments(input_csv)
        results_df.to_csv(output_csv)
        return results_df

    except Exception as e:
        print(f"Error in creating summary: {e}")
        return None


if __name__ == "__main__":
    input_csv = 'updated_sei_net_data.csv'
    output_csv = 'sentiment_analysis_results1.csv'
    results = create_sentiment_summary(input_csv, output_csv)
