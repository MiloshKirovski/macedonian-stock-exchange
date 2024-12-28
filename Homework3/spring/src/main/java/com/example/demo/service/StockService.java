package com.example.demo.service;

import com.example.demo.model.Stock;
import java.util.List;
import java.util.Optional;

public interface StockService {

    List<Stock> getAllCurrencyData();

    Optional<Stock> getCurrencyDataById(String companyName, String date);

    Stock saveCurrencyData(Stock currencyData);

    void deleteCurrencyData(String companyName, String date);
    public List<Stock> getCurrencyDataByCompanyAndDate(String companyName, String date);
}