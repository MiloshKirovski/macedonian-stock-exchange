package com.example.demo.web;

import com.example.demo.model.CompanyData;
import com.example.demo.model.TechnicalIndicatorsResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/lstm")
@CrossOrigin(origins = "http://localhost:3000")
public class LSTMController {

    private static final String FLASK_URL = "http://127.0.0.1:5000/price";

    @PostMapping
    public ResponseEntity<TechnicalIndicatorsResponse> getPredictedPrice(@RequestBody CompanyData companyData) {
        System.out.println("hello");
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestJson = "{\"companyName\": \"" + companyData.getCompanyName() + "\", \"companyPrice\": " + companyData.getCompanyPrice() + "}";

        System.out.println(requestJson);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    FLASK_URL, HttpMethod.POST, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            TechnicalIndicatorsResponse technicalIndicatorsResponse = objectMapper.readValue(response.getBody(), TechnicalIndicatorsResponse.class);

            return ResponseEntity.ok(technicalIndicatorsResponse);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error processing request: " + e.getMessage(), e);
        }
    }
}
