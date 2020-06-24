package com.reviewtrungtam.webapp.validation.validator;

import com.reviewtrungtam.webapp.validation.constraints.NullOrURL;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.net.MalformedURLException;

public class NullOrURLValidator implements ConstraintValidator<NullOrURL, String> {

    @Override
    public void initialize(NullOrURL constraintAnnotation) {

    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s != null && s.length() != 0) {
            try {
                java.net.URL url = new java.net.URL(s);
            } catch (MalformedURLException var5) {
                return false;
            }
        }
        return true;
    }
}
