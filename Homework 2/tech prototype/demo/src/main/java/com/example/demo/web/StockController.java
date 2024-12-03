package com.example.demo.web;

import com.example.demo.model.Stock;
import com.example.demo.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
@CrossOrigin(origins = "http://localhost:3001")
public class StockController {

    private final StockService currencyDataService;

    @Autowired
    public StockController(StockService currencyDataService) {
        this.currencyDataService = currencyDataService;
    }

    @GetMapping("/company/{companyName}/date/{date}")
    public ResponseEntity<List<Stock>> getCurrencyDataByCompanyAndDate(
            @PathVariable String companyName,
            @PathVariable String date) {

        List<Stock> currencyDataList = currencyDataService.getCurrencyDataByCompanyAndDate(companyName, date);

        if (!currencyDataList.isEmpty()) {
            return new ResponseEntity<>(currencyDataList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/save")
    public ResponseEntity<Stock> saveCurrencyData(@RequestBody Stock currencyData) {
        Stock savedData = currencyDataService.saveCurrencyData(currencyData);
        return new ResponseEntity<>(savedData, HttpStatus.CREATED);
    }

    @DeleteMapping("/{companyName}/{date}")
    public ResponseEntity<Void> deleteCurrencyData(@PathVariable String companyName, @PathVariable String date) {
        currencyDataService.deleteCurrencyData(companyName, date);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}