package io.github.rizanmusthafa.patient_service.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneOrEmailRequiredValidator.class)
@Documented
public @interface PhoneOrEmailRequired {
    String message() default "Either phone number or email must be provided";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

