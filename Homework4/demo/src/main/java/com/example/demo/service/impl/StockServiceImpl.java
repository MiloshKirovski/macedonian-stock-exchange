package com.example.demo.service.impl;

import com.example.demo.model.Stock;
import com.example.demo.repository.StockRepository;
import com.example.demo.service.StockDataFetchStrategy;
import com.example.demo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService {

    private final StockRepository stockRepository;
    private StockDataFetchStrategy stockDataFetchStrategy;

    @Autowired
    public StockServiceImpl(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
        this.stockDataFetchStrategy = DatabaseFetchStrategy.getInstance(stockRepository);
    }

    @Override
    public List<Stock> getAllStocks() {
        return stockRepository.findAll();
    }

    @Override
    public Optional<Stock> getStockById(String companyName, String date) {
        return stockRepository.findById(companyName + date);
    }

    @Override
    public Stock saveStock(Stock stock) {
        return stockRepository.save(stock);
    }

    @Override
    public void deleteStock(String companyName, String date) {
        stockRepository.deleteById(companyName + date);
    }

    @Override
    public List<Stock> getStockByCompanyAndDate(String companyName, String date) {
        return stockDataFetchStrategy.fetchStockData(companyName, date);
    }

    private static class DatabaseFetchStrategy implements StockDataFetchStrategy {

        private static DatabaseFetchStrategy instance;
        private final StockRepository stockRepositoryData;

        private DatabaseFetchStrategy(StockRepository stockRepository) {
            this.stockRepositoryData = stockRepository;
        }

        public static synchronized DatabaseFetchStrategy getInstance(StockRepository stockRepository) {
            if (instance == null) {
                instance = new DatabaseFetchStrategy(stockRepository);
            }
            return instance;
        }

        @Override
        public List<Stock> fetchStockData(String companyName, String date) {
            return stockRepositoryData.findByCompanyNameAndDate(companyName, date);
        }
    }
}