package com.ordo.oauth.controller;

import com.ordo.oauth.service.AlgorithmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hello")
public class HelloController {

    private final AlgorithmService algorithmService;

    @GetMapping
    public String hello() {
        return "오르도아이씨티";
    }

    @GetMapping("/api-auth-test")
    public String authTest() {
        return "/api-auth-test_OK!!!!";
    }

    @GetMapping("/{num}")
    public Integer divide(@PathVariable Integer num) {
        return algorithmService.sumOfDigit(num);
    }
}
