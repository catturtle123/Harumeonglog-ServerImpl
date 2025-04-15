package com.example.harumeonglog.domain.common.controller;

import com.example.harumeonglog.domain.common.config.data.ProfileConfigData;
import com.example.harumeonglog.domain.common.controller.response.CustomResponse;
import com.example.harumeonglog.domain.common.controller.code.BaseErrorCode;
import com.example.harumeonglog.domain.common.controller.code.GeneralErrorCode;
import com.example.harumeonglog.domain.common.domain.exception.GeneralException;
import com.example.harumeonglog.domain.common.util.discord.service.DiscordService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice(annotations = RestController.class)
public class ExceptionAdvice {

    private final DiscordService discordService;
    private final ProfileConfigData profileConfigData;

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<CustomResponse<List<String>>> constraintViolationException(ConstraintViolationException e) {

        log.error(Arrays.toString(e.getStackTrace()));
        List<String> message = e.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        BaseErrorCode code = GeneralErrorCode._BAD_REQUEST;
        log.error("Exception Advice(ConstraintViolationException): {}", message);

        return ResponseEntity.status(code.getHttpStatus()).body(CustomResponse.fail(code, message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse<Map<String, String>>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> error = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(field -> error.put(field.getField(), field.getDefaultMessage()));
        BaseErrorCode code = GeneralErrorCode._BAD_REQUEST;

        log.error("Exception Advice(MethodArgumentNotValidException): {}", error);
        return ResponseEntity.status(code.getHttpStatus()).body(CustomResponse.fail(code, error));
    }

    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<CustomResponse<String>> generalException(GeneralException e) {
        loggingError(e, e.getMessage());

        BaseErrorCode code = e.getBaseErrorCode();

        return ResponseEntity.status(code.getHttpStatus()).body(CustomResponse.fail(code, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<String>> exception(Exception e, WebRequest webRequest) {
        loggingError(e);

        BaseErrorCode code = GeneralErrorCode._INTERNAL_SERVER_ERROR;
        String message = e.getClass().getSimpleName() + ": " + e.getMessage();

        if (!profileConfigData.getOnProfile().equals("local")) {
            sendDiscordMessage(webRequest, e);
        }

        return ResponseEntity.status(code.getHttpStatus()).body(CustomResponse.fail(code, message));
    }

    private void loggingError(Exception e) {
        loggingError(e, e.getMessage());
    }

    private void loggingError(Exception e, Object message) {
        log.error("Exception Advice({}): {}", e.getClass().getSimpleName(), message);
        log.error(getErrorMethodAndClass(e));
    }

    private void sendDiscordMessage(WebRequest webRequest, Exception exception) {
        discordService.sendErrorMessage(webRequest, exception, getErrorMethodAndClass(exception));
    }

    private String getErrorMethodAndClass(Exception e) {
        StackTraceElement[] stackTraces = e.getStackTrace();
        if (stackTraces.length > 0) {
            StackTraceElement stackTrace = stackTraces[0];
            return "Method: " + stackTrace.getMethodName() + "\n Class: " + stackTrace.getClassName();
        }
        return null;
    }
}
