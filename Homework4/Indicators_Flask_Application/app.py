import pandas as pd
from flask import Flask, request, jsonify
from ta.momentum import RSIIndicator, StochasticOscillator
from ta.trend import SMAIndicator, EMAIndicator, MACD
from ta.volatility import BollingerBands
from ta.volume import OnBalanceVolumeIndicator
import numpy as np
from flask_cors import CORS


app = Flask(__name__)
CORS(app, resources={r"/*": {
    "origins": "*",
    "allow_headers": "*",
    "methods": ["GET", "POST", "PUT", "DELETE", "OPTIONS"]
}})
def calculate_wma(prices, window):
    weights = np.arange(1, window + 1)
    return prices.rolling(window).apply(lambda x: np.dot(x, weights) / weights.sum(), raw=True)

def calculate_technical_indicators(data):
    try:
        data['Price'] = pd.to_numeric(data['Price'], errors='coerce')
        data['Volume'] = pd.to_numeric(data['Volume'], errors='coerce')
        data.dropna(subset=['Price', 'Volume'], inplace=True)

        if len(data) >= 14:
            data['RSI'] = RSIIndicator(data['Price'], window=14).rsi()
            data['Stochastic'] = StochasticOscillator(data['Price'], data['Price'], data['Price'], window=14).stoch()
            data['MACD'] = MACD(data['Price']).macd()
        else:
            data['RSI'] = np.nan
            data['Stochastic'] = np.nan
            data['MACD'] = np.nan

        if len(data) >= 20:
            data['CCI'] = (data['Price'] - data['Price'].rolling(20).mean()) / (0.015 * data['Price'].rolling(20).std())
        else:
            data['CCI'] = np.nan

        if len(data) >= 10:
            data['ROC'] = data['Price'].pct_change(periods=10) * 100
        else:
            data['ROC'] = np.nan

        if len(data) >= 14:
            data['SMA'] = SMAIndicator(data['Price'], window=14).sma_indicator()
            data['EMA'] = EMAIndicator(data['Price'], window=14).ema_indicator()
            data['WMA'] = calculate_wma(data['Price'], window=14)
        else:
            data['SMA'] = np.nan
            data['EMA'] = np.nan
            data['WMA'] = np.nan

        if len(data) >= 20:
            bb = BollingerBands(data['Price'], window=20, window_dev=2)
            data['Bollinger_Bands_Upper'] = bb.bollinger_hband()
            data['Bollinger_Bands_Lower'] = bb.bollinger_lband()
        else:
            data['Bollinger_Bands_Upper'] = np.nan
            data['Bollinger_Bands_Lower'] = np.nan

        data['OBV'] = OnBalanceVolumeIndicator(data['Price'], data['Volume']).on_balance_volume()
        data['Signal'] = data['RSI'].apply(lambda rsi: 'Buy' if rsi < 30 else 'Sell' if rsi > 70 else 'Hold')

        numeric_cols = data.select_dtypes(include=[np.number]).columns
        data[numeric_cols] = data[numeric_cols].round(2)

        data.fillna(0, inplace=True)
        return data
    except Exception as e:
        raise ValueError(f"Error calculating technical indicators: {e}")

def calculate_time_frames(data):
    data['Date'] = pd.to_datetime(data['Date'], errors='coerce')
    data.dropna(subset=['Date'], inplace=True)
    data.set_index('Date', inplace=True)

    try:
        daily = calculate_technical_indicators(data.copy())
        weekly = calculate_technical_indicators(data.resample('W').mean())
        monthly = calculate_technical_indicators(data.resample('M').mean())

        return {'daily': daily.reset_index(), 'weekly': weekly.reset_index(), 'monthly': monthly.reset_index()}
    except Exception as e:
        raise ValueError(f"Error processing time frames: {e}")
@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({'status': 'healthy'}), 200
@app.route('/calculate', methods=['POST'])
def calculate_indicators():
    try:
        content = request.get_json()
        if 'stock_data' not in content:
            return jsonify({'error': 'Missing stock_data in request payload'}), 400

        data = pd.DataFrame(content['stock_data'])
        required_columns = {'Date', 'Price', 'Volume'}
        if not required_columns.issubset(data.columns):
            return jsonify({'error': f'Missing required columns: {required_columns - set(data.columns)}'}), 400

        result = calculate_time_frames(data)
        response = {
            'daily': result['daily'].to_dict(orient='records'),
            'weekly': result['weekly'].to_dict(orient='records'),
            'monthly': result['monthly'].to_dict(orient='records')
        }
        return jsonify(response)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(host="0.0.0.0", debug=True,port=5000)