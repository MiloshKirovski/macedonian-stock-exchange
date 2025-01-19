from flask import Flask, request, jsonify
import numpy as np
from keras._tf_keras.keras.models import load_model
import os
from flask_cors import CORS
app = Flask(__name__)
CORS(app, resources={r"/*": {
    "origins": "*",
    "allow_headers": "*",
    "methods": ["GET", "POST", "PUT", "DELETE", "OPTIONS"]
}})

class ModelLoaderFactory:
    def __init__(self, scaler_dir='scalers/scalers', model_dir='models/models'):
        self.scaler_dir = scaler_dir
        self.model_dir = model_dir

    def create_scaler_and_model(self, company_name):
        raise NotImplementedError("Subclasses should implement this method")


class SpecificModelLoaderFactory(ModelLoaderFactory):

    def create_scaler_and_model(self, company_name):
        try:
            scaler_path = os.path.join(self.scaler_dir, f"{company_name}_scaler.npy")
            model_path = os.path.join(self.model_dir, f"{company_name}.keras")

            scaler = np.load(scaler_path, allow_pickle=True).item()
            model = load_model(model_path)

            return scaler, model
        except Exception as e:
            raise FileNotFoundError(f"Error loading scaler or model for {company_name}: {e}")


factory = SpecificModelLoaderFactory()


def predict_next_price(company_name, current_price, lag=3):
    prices = [current_price] * lag
    features = np.array(prices).reshape(1, -1)

    scaler, model = factory.get_scaler_and_model(company_name)

    features_scaled = scaler.transform(features)
    features_reshaped = features_scaled.reshape(1, lag, features_scaled.shape[1] // lag)

    prediction = model.predict(features_reshaped, verbose=0)
    return prediction[0, 0]

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({'status': 'healthy'}), 200
@app.route('/price', methods=['POST'])
def get_price():
    try:
        data = request.get_json()
        company_name = data['companyName']
        company_price = data['companyPrice']
        predicted_price = predict_next_price(company_name, company_price)
        return jsonify({'predictedPrice': float(predicted_price)})
    except Exception as e:
        return jsonify({"error": str(e)}), 400


if __name__ == '__main__':
    app.run(debug=True, port=6002, host='0.0.0.0')