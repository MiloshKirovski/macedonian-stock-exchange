package com.example.demo.service.impl;

import com.example.demo.model.Stock;
import com.example.demo.repository.StockRepository;
import com.example.demo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository currencyDataRepository;

    public List<Stock> getCurrencyDataByCompanyAndDate(String companyName, String date) {
        return currencyDataRepository.findByCompanyNameAndDate(companyName, date);
    }

    @Autowired
    public StockServiceImpl(StockRepository currencyDataRepository) {
        this.currencyDataRepository = currencyDataRepository;
    }

    @Override
    public List<Stock> getAllCurrencyData() {
        return currencyDataRepository.findAll();
    }

    @Override
    public Optional<Stock> getCurrencyDataById(String companyName, String date) {
        return currencyDataRepository.findById(companyName + "" + date);
    }

    @Override
    public Stock saveCurrencyData(Stock currencyData) {
        return currencyDataRepository.save(currencyData);
    }

    @Override
    public void deleteCurrencyData(String companyName, String date) {
        currencyDataRepository.deleteById(companyName + "" + date);
    }
}