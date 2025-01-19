package com.example.demo.web;

import com.example.demo.model.CompanyData;
import com.example.demo.model.SentimentAnalysisResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sentiment-analysis")
public class NLPController {


    @Value("${app.flask.api2-url}")
    private String flaskUrl;

    private final RestTemplate restTemplate;

    public NLPController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping
    public ResponseEntity<SentimentAnalysisResponse> getSentimentAnalysis(@RequestBody CompanyData companyInput) {
        System.out.println("Received request for sentiment analysis");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestJson = "{\"companyName\":\"" + companyInput.getCompanyName() + "\"}";

        System.out.println("Request to Flask: " + requestJson);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    flaskUrl, HttpMethod.POST, entity, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            SentimentAnalysisResponse sentimentAnalysisResponse = objectMapper.readValue(response.getBody(), SentimentAnalysisResponse.class);

            System.out.println("Deserialized response: " + sentimentAnalysisResponse);

            return ResponseEntity.ok(sentimentAnalysisResponse);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error processing request: " + e.getMessage(), e);
        }
    }
}
