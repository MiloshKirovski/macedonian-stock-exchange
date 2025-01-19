package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SentimentAnalysisResponse {

    @JsonProperty("Company")
    private String company;

    @JsonProperty("Positive %")
    private double positivePercentage;

    @JsonProperty("Negative %")
    private double negativePercentage;

    @JsonProperty("Neutral %")
    private double neutralPercentage;

    @JsonProperty("Total number of notifications")
    private int totalNotifications;
}
