package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public double getPositivePercentage() {
        return positivePercentage;
    }

    public void setPositivePercentage(double positivePercentage) {
        this.positivePercentage = positivePercentage;
    }

    public double getNegativePercentage() {
        return negativePercentage;
    }

    public void setNegativePercentage(double negativePercentage) {
        this.negativePercentage = negativePercentage;
    }

    public double getNeutralPercentage() {
        return neutralPercentage;
    }

    public void setNeutralPercentage(double neutralPercentage) {
        this.neutralPercentage = neutralPercentage;
    }

    public int getTotalNotifications() {
        return totalNotifications;
    }

    public void setTotalNotifications(int totalNotifications) {
        this.totalNotifications = totalNotifications;
    }
}
