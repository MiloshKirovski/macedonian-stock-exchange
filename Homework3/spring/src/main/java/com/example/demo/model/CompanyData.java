package com.example.demo.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompanyData {
    private String companyName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String companyPrice;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStartDate() {
        return String.valueOf(startDate);
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {

        return String.valueOf(endDate);
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getCompanyPrice() {
        return companyPrice;
    }
}