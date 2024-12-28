package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class TechnicalIndicatorsResponse {

    @JsonProperty("predictedPrice")
    private Double predictedPrice;

    @JsonProperty("daily")
    private List<IndicatorData> daily;

    @JsonProperty("weekly")
    private List<IndicatorData> weekly;

    @JsonProperty("monthly")
    private List<IndicatorData> monthly;

    public Double getPredictedPrice() {
        return predictedPrice;
    }

    public void setPredictedPrice(Double predictedPrice) {
        this.predictedPrice = predictedPrice;
    }

    public static class IndicatorData {
        @JsonProperty("Date")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private String date;

        @JsonProperty("Price")
        private Double price;

        @JsonProperty("Volume")
        private Double volume;

        @JsonProperty("RSI")
        private Double rsi;

        @JsonProperty("Stochastic")
        private Double stochastic;

        @JsonProperty("MACD")
        private Double macd;

        @JsonProperty("CCI")
        private Double cci;

        @JsonProperty("ROC")
        private Double roc;

        @JsonProperty("SMA")
        private Double sma;

        @JsonProperty("EMA")
        private Double ema;

        @JsonProperty("WMA")
        private Double wma;

        @JsonProperty("Bollinger_Bands_Upper")
        private Double bollingerBandsUpper;

        @JsonProperty("Bollinger_Bands_Lower")
        private Double bollingerBandsLower;

        @JsonProperty("OBV")
        private Double obv;

        @JsonProperty("Signal")
        private String signal;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Double getPrice() {
            return price;
        }

        public void setPrice(Double price) {
            this.price = price;
        }

        public Double getVolume() {
            return volume;
        }

        public void setVolume(Double volume) {
            this.volume = volume;
        }

        public Double getRsi() {
            return rsi;
        }

        public void setRsi(Double rsi) {
            this.rsi = rsi;
        }

        public Double getStochastic() {
            return stochastic;
        }

        public void setStochastic(Double stochastic) {
            this.stochastic = stochastic;
        }

        public Double getMacd() {
            return macd;
        }

        public void setMacd(Double macd) {
            this.macd = macd;
        }

        public Double getCci() {
            return cci;
        }

        public void setCci(Double cci) {
            this.cci = cci;
        }

        public Double getRoc() {
            return roc;
        }

        public void setRoc(Double roc) {
            this.roc = roc;
        }

        public Double getSma() {
            return sma;
        }

        public void setSma(Double sma) {
            this.sma = sma;
        }

        public Double getEma() {
            return ema;
        }

        public void setEma(Double ema) {
            this.ema = ema;
        }

        public Double getWma() {
            return wma;
        }

        public void setWma(Double wma) {
            this.wma = wma;
        }

        public Double getBollingerBandsUpper() {
            return bollingerBandsUpper;
        }

        public void setBollingerBandsUpper(Double bollingerBandsUpper) {
            this.bollingerBandsUpper = bollingerBandsUpper;
        }

        public Double getBollingerBandsLower() {
            return bollingerBandsLower;
        }

        public void setBollingerBandsLower(Double bollingerBandsLower) {
            this.bollingerBandsLower = bollingerBandsLower;
        }

        public Double getObv() {
            return obv;
        }

        public void setObv(Double obv) {
            this.obv = obv;
        }

        public String getSignal() {
            return signal;
        }

        public void setSignal(String signal) {
            this.signal = signal;
        }
    }
}
