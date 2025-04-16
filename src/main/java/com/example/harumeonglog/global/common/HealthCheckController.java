package com.example.harumeonglog.global.common;

import com.example.harumeonglog.global.common.response.CustomResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {

    @GetMapping("/health")
    public CustomResponse<String> healthCheck() {
        return CustomResponse.ok("I am healthy");
    }
}
