package com.example.harumeonglog.domain.common.controller.response.util;

import com.example.harumeonglog.domain.common.controller.code.BaseCode;
import com.example.harumeonglog.domain.common.controller.code.BaseErrorCode;
import com.example.harumeonglog.domain.common.controller.response.CustomResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class HttpResponseUtil {

    private static final String CONTENT_TYPE = "application/json";
    private static final String CHARACTER_ENCODING = "UTF-8";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> void writeResponse(HttpServletResponse httpServletResponse, BaseErrorCode code, T result) throws IOException {
        setResponseDetail(httpServletResponse, code.getHttpStatus());
        objectMapper.writeValue(httpServletResponse.getOutputStream(), CustomResponse.fail(code, result));
    }

    public static <T> void writeResponse(HttpServletResponse httpServletResponse, BaseCode code, T result) throws IOException {
        setResponseDetail(httpServletResponse, code.getHttpStatus());
        objectMapper.writeValue(httpServletResponse.getOutputStream(), CustomResponse.success(code, result));
    }

    private static void setResponseDetail(HttpServletResponse httpServletResponse, HttpStatus httpStatus) {
        httpServletResponse.setContentType(CONTENT_TYPE);
        httpServletResponse.setCharacterEncoding(CHARACTER_ENCODING);
        httpServletResponse.setStatus(httpStatus.value());
    }
}
