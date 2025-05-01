package com.example.harumeonglog.global.validation.annotation;

import com.example.harumeonglog.global.validation.validator.CheckCursorValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckCursorValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckCursorValidation {
    String message() default "허용 되지 않은 cursor 값 입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}