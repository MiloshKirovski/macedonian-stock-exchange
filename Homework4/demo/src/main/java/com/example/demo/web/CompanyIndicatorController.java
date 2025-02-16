package com.example.demo.web;

import com.example.demo.model.CompanyData;
import com.example.demo.model.TechnicalIndicatorsResponse;
import com.example.demo.service.FlaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/indicators")
public class CompanyIndicatorController {

    @Autowired
    private FlaskService flaskService;

    @PostMapping("/calculate")
    public ResponseEntity<TechnicalIndicatorsResponse> calculateIndicators(@RequestBody CompanyData companyData) {
        try {
            TechnicalIndicatorsResponse response = flaskService.getIndicatorsFromFlask(companyData);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
