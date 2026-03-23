package com.reqforge.backend.controller;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api")
    @CrossOrigin("*")
    public class ApiController {

        @GetMapping("/")
        public String hello() {
            return "ReqForge Backend is Working 🚀";
        }
    }

