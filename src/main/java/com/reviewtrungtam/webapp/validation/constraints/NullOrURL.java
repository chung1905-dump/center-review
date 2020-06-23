package com.reviewtrungtam.webapp.validation.constraints;

import com.reviewtrungtam.webapp.validation.validator.NullOrURLValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NullOrURLValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NullOrURL {
    String message() default "{org.hibernate.validator.constraints.URL.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
