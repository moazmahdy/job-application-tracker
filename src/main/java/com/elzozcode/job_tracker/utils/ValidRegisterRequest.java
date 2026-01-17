package com.elzozcode.job_tracker.utils;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RegisterDtoValidator.class)
@Documented
public @interface ValidRegisterRequest {
    String message() default "Invalid register request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
