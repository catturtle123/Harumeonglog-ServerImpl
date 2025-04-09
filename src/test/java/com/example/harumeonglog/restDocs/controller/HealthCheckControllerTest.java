package com.example.harumeonglog.restDocs.controller;

import com.example.harumeonglog.domain.common.controller.HealthCheckController;
import com.example.harumeonglog.restDocs.base.AbstractRestDocsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthCheckController.class)
public class HealthCheckControllerTest extends AbstractRestDocsTest {

    @Test
    @DisplayName("헬스 체킹")
    void canHealthCheck() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/health"));

        result.andDo(restDocs.document(
                commonResponse
        ));
    }

}
