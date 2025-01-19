package com.example.demo.service.impl;

import com.example.demo.model.CompanyData;
import com.example.demo.model.Stock;
import com.example.demo.model.TechnicalIndicatorsResponse;
import com.example.demo.repository.StockRepository;
import com.example.demo.service.FlaskService;
import com.example.demo.service.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlaskServiceImpl implements FlaskService {

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private HttpService httpService;

    @Value("${app.flask.api1-url}")
    private String flaskApiUrl;

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
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No stock data found for company: " +
                        companyData.getCompanyName() +
                        " between " + startDate +
                        " and " + endDate);
            }

            Map<String, Object> payload = new HashMap<>();
            payload.put("stock_data", prepareStockData(stocks));

            return httpService.postForObject(flaskApiUrl, payload, TechnicalIndicatorsResponse.class)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Received null response from Flask service"));

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error processing request: " + e.getMessage(), e);
        }
    }

    private Date convertStringToDate(String dateStr) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(dateStr, formatter);
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            throw new RuntimeException("Error converting date: " + dateStr + " - " + e.getMessage(), e);
        }
    }

    private List<Map<String, Object>> prepareStockData(List<Stock> stocks) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return stocks.stream().map(stock -> {
            Map<String, Object> stockData = new HashMap<>();
            stockData.put("Date", dateFormat.format(stock.getDate_new()));
            stockData.put("Price", parsePrice(stock.getPriceOfLastTransaction()));
            stockData.put("Volume", stock.getQuantity());
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
