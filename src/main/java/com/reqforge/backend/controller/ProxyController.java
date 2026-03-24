package com.reqforge.backend.controller;

import com.reqforge.backend.model.RequestHistory;
import com.reqforge.backend.repository.RequestHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/proxy")
@CrossOrigin("*")
public class ProxyController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RequestHistoryRepository historyRepo;

    @PostMapping
    public ResponseEntity<?> callApi(@RequestBody Map<String, Object> request) {
        RequestHistory history = new RequestHistory();
        String url = null;
        String method = null;
        Object body = null;
        Map<String, String> headers = null;
        try{

         url = (String) request.get("url");
         method = (String) request.get("method");
         headers = (Map<String, String>) request.get("headers");
         body = request.get("body");
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
            history.setUrl(url);
            history.setMethod(method);
            assert headers != null;
            history.setHeaders(headers.toString());
            history.setBody(body != null ? body.toString() : "");
            history.setResponse(response.getBody());
            history.setStatus(response.getStatusCode().value());
            history.setTimestamp(LocalDateTime.now());

            historyRepo.save(history);

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
                history.setUrl(url);
                history.setMethod(method);
                history.setHeaders(headers.toString());
                history.setBody(body != null ? body.toString() : "");
                history.setResponse(responseBody);
                history.setStatus(ex.getStatusCode().value());
                history.setTimestamp(LocalDateTime.now());
                return ResponseEntity
                        .status(ex.getStatusCode())
                        .body(json);


            } catch (Exception parseEx) {
                history.setUrl(url);
                history.setMethod(method);
                history.setHeaders(headers.toString());
                history.setBody(body != null ? body.toString() : "");
                history.setResponse(responseBody);
                history.setStatus(ex.getStatusCode().value());
                history.setTimestamp(LocalDateTime.now());
                return ResponseEntity
                        .status(ex.getStatusCode())
                        .body(responseBody);
            }
        }
    }



    @GetMapping("/history")
    public List<RequestHistory> getHistory() {
        return historyRepo.findAll();
    }



}