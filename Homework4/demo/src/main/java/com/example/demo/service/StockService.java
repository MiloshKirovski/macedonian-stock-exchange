package com.example.demo.service;

import com.example.demo.model.Stock;
import java.util.List;
import java.util.Optional;

public interface StockService {
    List<Stock> getAllStocks();
    Optional<Stock> getStockById(String companyName, String date);
    Stock saveStock(Stock stock);
    void deleteStock(String companyName, String date);
    List<Stock> getStockByCompanyAndDate(String companyName, String date);
}
