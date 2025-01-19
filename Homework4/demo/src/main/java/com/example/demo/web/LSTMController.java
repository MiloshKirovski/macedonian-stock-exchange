package com.example.demo.web;

import com.example.demo.model.CompanyData;
import com.example.demo.model.TechnicalIndicatorsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/lstm")
public class LSTMController {

    @Value("${app.flask.api3-url}")
    private String flaskUrl;

    private final RestTemplate restTemplate;

    public LSTMController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<TechnicalIndicatorsResponse> getPredictedPrice(@RequestBody CompanyData companyData) {
        System.out.println("hello " + flaskUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestJson = "{\"companyName\": \"" + companyData.getCompanyName() + "\", \"companyPrice\": " + companyData.getCompanyPrice() + "}";

        System.out.println(requestJson);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    flaskUrl, HttpMethod.POST, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            TechnicalIndicatorsResponse technicalIndicatorsResponse = objectMapper.readValue(response.getBody(), TechnicalIndicatorsResponse.class);

            return ResponseEntity.ok(technicalIndicatorsResponse);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error processing request: " + e.getMessage(), e);
        }
    }
}
