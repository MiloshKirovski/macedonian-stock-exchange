package com.example.demo.service.impl;

import com.example.demo.service.HttpService;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class HttpServiceImpl implements HttpService {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> Optional<T> postForObject(String url, Object request, Class<T> responseType) {
        return Optional.ofNullable(restTemplate.postForObject(url, request, responseType));
    }
}
