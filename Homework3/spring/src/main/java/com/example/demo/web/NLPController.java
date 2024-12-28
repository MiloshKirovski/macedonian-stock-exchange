package com.example.demo.web;

import com.example.demo.model.CompanyData;
import com.example.demo.model.SentimentAnalysisResponse;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/sentiment-analysis")
@CrossOrigin(origins = "http://localhost:3000")
public class NLPController {

    private static final String FLASK_URL = "http://127.0.0.1:5000/nlp";

    @PostMapping
    public ResponseEntity<SentimentAnalysisResponse> getSentimentAnalysis(@RequestBody CompanyData companyInput) {
        System.out.println("Received request for sentiment analysis");

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        String requestJson = "{\"companyName\":\"" + companyInput.getCompanyName() + "\"}";

        System.out.println("Request to Flask: " + requestJson);

        HttpEntity<String> entity = new HttpEntity<>(requestJson, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    FLASK_URL, HttpMethod.POST, entity, String.class);

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
