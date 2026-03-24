package com.reqforge.backend.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.util.Map;

@RestController
@RequestMapping("/proxy")
@CrossOrigin("*")
public class ProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping
    public ResponseEntity<?> callApi(@RequestBody Map<String, Object> request) {
        try{

        String url = (String) request.get("url");
        String method = (String) request.get("method");
        Map<String, String> headers = (Map<String, String>) request.get("headers");
        Object body = request.get("body");
        System.out.println("URL: " + url);
        System.out.println("METHOD: " + method);

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

            return ResponseEntity.status(response.getStatusCode())
                    .body(response.getBody());

        } catch (HttpStatusCodeException ex) {

            ObjectMapper mapper = new ObjectMapper();
            String responseBody = ex.getResponseBodyAsString();

            if (responseBody == null || responseBody.isEmpty()) {
                responseBody = ex.getStatusCode() + " " + ex.getStatusText();
            }

            try {
                Object json = mapper.readValue(responseBody, Object.class);

                return ResponseEntity
                        .status(ex.getStatusCode())
                        .body(json);

            } catch (Exception parseEx) {
                return ResponseEntity
                        .status(ex.getStatusCode())
                        .body(responseBody);
            }
        }
    }
}