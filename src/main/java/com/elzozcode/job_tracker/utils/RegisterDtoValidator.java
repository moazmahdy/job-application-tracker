package com.elzozcode.job_tracker.utils;

import com.elzozcode.job_tracker.dtos.RegisterDto;
import com.elzozcode.job_tracker.entity.enums.UserType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegisterDtoValidator
        implements ConstraintValidator<ValidRegisterRequest, RegisterDto> {

    @Override
    public boolean isValid(RegisterDto dto, ConstraintValidatorContext context) {

        if (dto.getType() == UserType.COMPANY) {
            if (dto.getCompanyName() == null || dto.getCompanyName().isBlank()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "Company name is required for COMPANY type")
                        .addPropertyNode("companyName")
                        .addConstraintViolation();
                return false;
            }
        }

        if (dto.getType() == UserType.USER && dto.getCompanyName() != null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                            "Company name must be null for USER type")
                    .addPropertyNode("companyName")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

}
