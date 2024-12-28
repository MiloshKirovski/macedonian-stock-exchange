package com.example.demo.service.impl;

import com.example.demo.model.CompanyData;
import com.example.demo.model.Stock;
import com.example.demo.repository.StockRepository;
import com.example.demo.model.TechnicalIndicatorsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlaskService {
    @Autowired
    private StockRepository stockRepository;


    private Date convertStringToDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            throw new RuntimeException("Error converting date: " + dateStr + " - " + e.getMessage(), e);
        }
    }


    public TechnicalIndicatorsResponse getIndicatorsFromFlask(CompanyData companyData) {
        try {
            Date startDate = convertStringToDate(companyData.getStartDate());
            Date endDate = convertStringToDate(companyData.getEndDate());

            List<Stock> stocks = stockRepository.findByCompanyNameAndDateRange(
                    companyData.getCompanyName(),
                    startDate,
                    endDate
            );

            if (stocks.isEmpty()) {
                throw new RuntimeException("No stock data found for: " +
                        companyData.getCompanyName() +
                        " between " + startDate +
                        " and " + endDate);
            }

            Map<String, Object> payload = new HashMap<>();
            payload.put("stock_data", prepareStockData(stocks));

            RestTemplate restTemplate = new RestTemplate();
            String flaskUrl = "http://127.0.0.1:5000/calculate";

            TechnicalIndicatorsResponse response = restTemplate.postForObject(flaskUrl, payload, TechnicalIndicatorsResponse.class);

            if (response == null) {
                throw new RuntimeException("Received null response from Flask service");
            }

            return response;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error processing request: " + e.getMessage(), e);
        }
    }


    private List<Map<String, Object>> prepareStockData(List<Stock> stocks) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return stocks.stream().map(stock -> {
            Map<String, Object> stockData = new HashMap<>();

            try {
                stockData.put("Date", dateFormat.format(stock.getDate_new()));
                stockData.put("Price", parsePrice(stock.getPriceOfLastTransaction()));
                stockData.put("Volume", stock.getQuantity());
            } catch (Exception e) {
                throw new RuntimeException("Error preparing stock data for stock: " + stock + " - " + e.getMessage(), e);
            }

            return stockData;
        }).collect(Collectors.toList());
    }


    private double parsePrice(String priceStr) {
        try {
            return Double.parseDouble(priceStr.replace(",", "."));
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid price format: " + priceStr, e);
        }
    }
}
