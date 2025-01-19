import pandas as pd
from flask import Flask, request, jsonify
from flask_cors import CORS

app = Flask(__name__)
CORS(app, resources={r"/*": {
    "origins": "*",
    "allow_headers": "*",
    "methods": ["GET", "POST", "PUT", "DELETE", "OPTIONS"]
}})
@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({'status': 'healthy'}), 200
@app.route('/nlp', methods=['POST'])
def get_info():
    try:
        data = request.get_json()
        company_name = data['companyName']
        df = pd.read_csv('sentiment_analysis_results1.csv')
        result_df = df[df['Company'] == company_name]
        if result_df.empty:
            return jsonify({"error": "No matching company found"}), 404
        return result_df.iloc[0].to_json(), 200
    except Exception as e:
        return jsonify({"error": f"Error processing request: {e}"}), 400


if __name__ == '__main__':
    app.run(debug=True, port=6001,host='0.0.0.0')
