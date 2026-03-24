package com.reqforge.backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RequestHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;
    private String method;

    @Column(length = 5000)
    private String headers;

    @Column(length = 10000)
    private String body;

    @Column(length = 10000)
    private String response;

    private int status;

    private LocalDateTime timestamp;
}