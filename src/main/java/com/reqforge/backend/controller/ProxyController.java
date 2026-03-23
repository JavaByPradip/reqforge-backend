package com.reqforge.backend.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/proxy")
@CrossOrigin("*")
public class ProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseEntity<?> callApi(@RequestBody Map<String, Object> request) {

        String url = (String) request.get("url");
        String method = (String) request.get("method");
        Map<String, String> headers = (Map<String, String>) request.get("headers");
        Object body = request.get("body");

        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            headers.forEach(httpHeaders::set);
        }

        HttpEntity<Object> entity = new HttpEntity<>(body, httpHeaders);

        ResponseEntity<String> response;

        switch (method.toUpperCase()) {
            case "GET":
                response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
                break;
            case "POST":
                response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
                break;
            case "PUT":
                response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);
                break;
            case "DELETE":
                response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);
                break;
            default:
                return ResponseEntity.badRequest().body("Invalid Method");
        }

        return ResponseEntity.ok(response.getBody());
    }
}