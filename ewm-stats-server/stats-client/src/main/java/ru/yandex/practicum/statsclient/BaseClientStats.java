package ru.yandex.practicum.statsclient;

import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class BaseClientStats {
    private final RestTemplate restTemplate;

    public BaseClientStats(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected <T, R> ResponseEntity<R> post(String path, Map<String, Object> params, T body, Class<R> responseType) {
        return doRequest(HttpMethod.POST, path, params, body, responseType);
    }

    protected <T> ResponseEntity<T> get(String path, Map<String, Object> params, Class<T> responseType) {
        return doRequest(HttpMethod.GET, path, params, null, responseType);
    }

    private <T, R> ResponseEntity<R> doRequest(HttpMethod method, String path, Map<String, Object> params, T body, Class<R> responseType) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, addHeaders());
        try {
            if (params != null) {
                return restTemplate.exchange(path, method, requestEntity, responseType, params);
            } else {
                return restTemplate.exchange(path, method, requestEntity, responseType);
            }
        } catch (HttpStatusCodeException e) {
            throw new RuntimeException("Request failed: " + e.getResponseBodyAsString(), e);
        }
    }

    private HttpHeaders addHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
