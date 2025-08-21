package ru.yandex.practicum.statsclient;

import org.apache.hc.client5.http.classic.methods.HttpHead;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class BaseClientStats {
    private final RestTemplate restTemplate;

    public BaseClientStats(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    protected <T> ResponseEntity<Object> post(String path, Map<String, Object> params, T body) {
        return this.doRequest(HttpMethod.POST, path, params, body);
    }

    protected <T> ResponseEntity<Object> get(String path, Map<String, Object> params, T body) {
        return this.doRequest(HttpMethod.GET, path, params, body);
    }

    private <T> ResponseEntity<Object> doRequest(HttpMethod method,
                                                 String path,
                                                 Map<String, Object> params,
                                                 T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, this.addHeaders());
        ResponseEntity<Object> response;
        try {
            if (params != null) {
                response = restTemplate.exchange(path, method, requestEntity, Object.class, params);
            } else {
                response = restTemplate.exchange(path, method, requestEntity, Object.class);
            }
        } catch (HttpStatusCodeException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsByteArray());
        }
        return response;
    }

    private HttpHeaders addHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return httpHeaders;
    }
}
