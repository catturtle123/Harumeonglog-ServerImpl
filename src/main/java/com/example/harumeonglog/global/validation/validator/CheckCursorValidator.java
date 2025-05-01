package com.example.harumeonglog.global.validation.validator;

import com.example.harumeonglog.global.error.code.GeneralErrorCode;
import com.example.harumeonglog.global.validation.annotation.CheckCursorValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckCursorValidator implements ConstraintValidator<CheckCursorValidation, Long> {
    @Override
    public void initialize(CheckCursorValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        if (value < 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(GeneralErrorCode.NOT_VALID_CURSOR.getMessage())
                    .addConstraintViolation();

            return false;
        }
        return true;
    }
}
