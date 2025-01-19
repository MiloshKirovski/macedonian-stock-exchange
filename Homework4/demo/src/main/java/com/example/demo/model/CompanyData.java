package com.example.demo.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CompanyData {

    @NotEmpty(message = "Company name cannot be empty")
    private String companyName;

    @NotEmpty(message = "Start date cannot be empty")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Start date must be in the format yyyy-MM-dd")
    private String startDate;

    @NotEmpty(message = "End date cannot be empty")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "End date must be in the format yyyy-MM-dd")
    private String endDate;

    private String companyPrice;

}
