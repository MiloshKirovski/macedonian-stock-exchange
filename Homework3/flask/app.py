import pandas as pd
from flask import Flask, request, jsonify
from ta.momentum import RSIIndicator, StochasticOscillator
from ta.trend import SMAIndicator, EMAIndicator, MACD
from ta.volatility import BollingerBands
from ta.volume import OnBalanceVolumeIndicator
import numpy as np
from keras._tf_keras.keras.models import load_model

app = Flask(__name__)
def predict_next_price(company_name, current_price, lag=3):
    prices = [current_price] * lag

    features = np.array(prices).reshape(1, -1)

    scaler = np.load(f"scalers/scalers/{company_name}_scaler.npy", allow_pickle=True).item()
    model = load_model(f"models/models/{company_name}.keras")

    features_scaled = scaler.transform(features)
    features_reshaped = features_scaled.reshape(1, lag, features_scaled.shape[1] // lag)

    prediction = model.predict(features_reshaped, verbose=0)
    return prediction[0, 0]

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

    print("Data after date conversion:\n", data)

    try:
        if len(data) < 2:
            print("Not enough data for resampling")
            result = {'daily': calculate_technical_indicators(data.copy())}
            result['daily'].reset_index(inplace=True)
            result['daily']['Date'] = result['daily']['Date'].dt.strftime('%Y-%m-%d')
            return result

        if data['Price'].dtype != 'float64' or data['Volume'].dtype != 'float64':
            data['Price'] = pd.to_numeric(data['Price'], errors='coerce')
            data['Volume'] = pd.to_numeric(data['Volume'], errors='coerce')

        daily = calculate_technical_indicators(data.copy())
        daily.reset_index(inplace=True)
        daily['Date'] = daily['Date'].dt.strftime('%Y-%m-%d')
        print(f"Daily Data after Technical Indicators:\n{daily.head()}")

        weekly = calculate_technical_indicators(data.resample('W').mean())
        weekly.reset_index(inplace=True)
        weekly['Date'] = weekly['Date'].dt.strftime('%Y-%m-%d')
        print(f"Weekly Data after Technical Indicators:\n{weekly.head()}")

        monthly = calculate_technical_indicators(data.resample('M').mean())
        monthly.reset_index(inplace=True)
        monthly['Date'] = monthly['Date'].dt.strftime('%Y-%m-%d')
        print(f"Monthly Data after Technical Indicators:\n{monthly.head()}")

        return {'daily': daily, 'weekly': weekly, 'monthly': monthly}
    except Exception as e:
        print(f"Exception during technical indicator calculation or resampling: {e}")
        raise ValueError(f"Error processing time frames: {e}")

@app.route('/calculate', methods=['POST'])
def calculate_indicators():
    try:
        content = request.get_json()
        print(content)
        if 'stock_data' not in content:
            return jsonify({'error': 'Missing stock_data in request payload'}), 400

        data = pd.DataFrame(content['stock_data'])
        required_columns = {'Date', 'Price', 'Volume'}
        if not required_columns.issubset(data.columns):
            return jsonify({'error': f'Missing required columns: {required_columns - set(data.columns)}'}), 400

        result = calculate_time_frames(data)
        print("Daily Data for Response:", result['daily'].head().to_dict(orient='records'))
        print("Weekly Data for Response:", result['weekly'].head().to_dict(orient='records'))
        print("Monthly Data for Response:", result['monthly'].head().to_dict(orient='records'))
        response = {'daily': result['daily'].to_dict(orient='records'),
                    'weekly': result['weekly'].to_dict(orient='records'),
                    'monthly': result['monthly'].to_dict(orient='records')}
        print("JSON Response: ", response)

        return jsonify(response)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

@app.route('/price', methods=['POST'])
def get_price():
    try:
        data = request.get_json()
        company_name = data['companyName']
        company_price = data['companyPrice']

        predicted_price = predict_next_price(company_name, company_price)

        predicted_price = float(predicted_price)
        print(f"Predicted price type: {type(predicted_price)}")

        return jsonify({
            'predictedPrice': predicted_price
        })
    except Exception as e:
        return jsonify({"error": str(e)}), 400

@app.route('/nlp', methods=['POST'])
def get_info():
    try:
        data = request.get_json()
        company_name = data['companyName']

        df = pd.read_csv('sentiment_analysis_results1.csv')

        result_df = df[df['Company'] == company_name]

        if result_df.empty:
            print(f"No matching company found for: {company_name}")
            return jsonify({"error": "No matching company found"}), 404

        result_json = result_df.iloc[0].to_json()

        return result_json, 200

    except Exception as e:
        print(f"Error reading or querying CSV: {e}")
        return jsonify({"error": f"Error processing request: {e}"}), 400

if __name__ == '__main__':
    app.run(debug=True)
