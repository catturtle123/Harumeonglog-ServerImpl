package com.example.harumeonglog.domain.common.auth.annotation;

import io.swagger.v3.oas.annotations.Parameter;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Parameter(hidden = true)
public @interface AuthenticatedMember {

}
