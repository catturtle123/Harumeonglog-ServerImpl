package com.example.harumeonglog.global.validation.validator;


import com.example.harumeonglog.global.validation.annotation.CheckSizeValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.harumeonglog.global.error.code.GeneralErrorCode.NOT_VALID_SIZE;


@Component
@RequiredArgsConstructor
public class CheckSizeValidator implements ConstraintValidator<CheckSizeValidation, Integer> {
    @Override
    public void initialize(CheckSizeValidation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value <= 0) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(NOT_VALID_SIZE.getMessage())
                    .addConstraintViolation();

            return false;
        }
        return true;
    }
}
