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

    @Data
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
    }
}
