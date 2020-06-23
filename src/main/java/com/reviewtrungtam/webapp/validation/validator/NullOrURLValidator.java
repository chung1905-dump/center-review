package com.reviewtrungtam.webapp.validation.validator;

import com.reviewtrungtam.webapp.validation.constraints.NullOrURL;
import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NullOrURLValidator implements ConstraintValidator<NullOrURL, String> {
    @Override
    public void initialize(NullOrURL constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        URLValidator urlValidator = new URLValidator();
        return s == null || urlValidator.isValid(s, constraintValidatorContext);
    }
}
