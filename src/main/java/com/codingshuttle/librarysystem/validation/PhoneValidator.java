package com.codingshuttle.librarysystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public boolean isValid(String phoneNo, ConstraintValidatorContext context) {
       // already added NonBlank so we need to regex
        return phoneNo.matches("^\\d{10}$");
    }
}
