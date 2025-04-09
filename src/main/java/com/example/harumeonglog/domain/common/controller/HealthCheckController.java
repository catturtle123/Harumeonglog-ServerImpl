package com.example.harumeonglog.domain.common.controller;

import com.example.harumeonglog.domain.common.controller.response.CustomResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public CustomResponse<String> healthCheck() {
        return CustomResponse.ok("I am healthy");
    }
}
