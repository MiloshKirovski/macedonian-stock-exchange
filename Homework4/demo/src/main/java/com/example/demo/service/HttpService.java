package com.example.demo.service;

import java.util.Optional;

public interface HttpService {
    <T> Optional<T> postForObject(String url, Object request, Class<T> responseType);
}
