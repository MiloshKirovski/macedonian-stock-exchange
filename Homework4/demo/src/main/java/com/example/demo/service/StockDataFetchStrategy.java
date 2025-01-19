package com.example.demo.service;

import com.example.demo.model.Stock;

import java.util.List;

public interface StockDataFetchStrategy {
    List<Stock> fetchStockData(String companyName, String date);
}
