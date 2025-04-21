package com.example.harumeonglog.global.error;

import com.example.harumeonglog.global.common.response.CustomResponse;
import com.example.harumeonglog.global.error.code.BaseErrorCode;
import com.example.harumeonglog.global.error.code.CommentErrorCode;
import com.example.harumeonglog.global.error.code.GeneralErrorCode;
import com.example.harumeonglog.global.error.exception.GeneralException;
import com.example.harumeonglog.global.data.ProfileConfigData;
import com.example.harumeonglog.global.discord.service.DiscordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

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

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomResponse<String>> handleDuplicateEntry(DataIntegrityViolationException e) {
        loggingError(e, e.getMessage());

        BaseErrorCode code = GeneralErrorCode._IS_ALREADY;

        return ResponseEntity.status(code.getHttpStatus()).body(CustomResponse.fail(code, null));
    }


    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<CustomResponse<String>> generalException(GeneralException e) {
        loggingError(e, e.getMessage());

        BaseErrorCode code = e.getBaseErrorCode();

        return ResponseEntity.status(code.getHttpStatus()).body(CustomResponse.fail(code, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomResponse<String>> exception(Exception e, HttpServletRequest request) {
        loggingError(e);

        BaseErrorCode code = GeneralErrorCode._INTERNAL_SERVER_ERROR;
        String message = e.getClass().getSimpleName() + ": " + e.getMessage();

        if (!profileConfigData.getOnProfile().equals("local")) {
            sendDiscordMessage(request, e);
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

    private void sendDiscordMessage(HttpServletRequest webRequest, Exception exception) {
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
