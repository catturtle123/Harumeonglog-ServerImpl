package com.example.harumeonglog.global.common;

import com.example.harumeonglog.global.common.response.CustomResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health Check", description = "health check 관련 API 입니다.")
public class HealthCheckController {

    @Operation(summary = "헬스 체킹 API by 김준환", description = "서버 상태 확인하는 API")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "COMMON200", description = "성공입니다.")
    })
    @GetMapping("/health")
    public CustomResponse<String> healthCheck() {
        return CustomResponse.ok("I am healthy");
    }
}
