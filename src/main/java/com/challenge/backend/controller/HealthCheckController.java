package com.challenge.backend.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-check")
@AllArgsConstructor
public class HealthCheckController {
    @GetMapping
    public String index(){
        return "hi Server running \uD83D\uDD2A \uD83D\uDC80";
    }
}
