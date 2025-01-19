package com.example.demo.service;

import com.example.demo.model.CompanyData;
import com.example.demo.model.TechnicalIndicatorsResponse;

public interface FlaskService {
    TechnicalIndicatorsResponse getIndicatorsFromFlask(CompanyData companyData);
}
