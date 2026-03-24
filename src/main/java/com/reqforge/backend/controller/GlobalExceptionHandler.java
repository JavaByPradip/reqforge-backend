package com.reqforge.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleMethodNotAllowed(HttpRequestMethodNotSupportedException ex) {

        Map<String, Object> response = new HashMap<>();

        response.put("error", "Method Not Allowed");
        response.put("message", "Invalid HTTP method used");
        response.put("requestedMethod", ex.getMethod());

        return ResponseEntity.status(405).body(response);
    }
}