package io.github.rizanmusthafa.patient_service.validation;

import io.github.rizanmusthafa.patient_service.dto.PatientDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneOrEmailRequiredValidator implements ConstraintValidator<PhoneOrEmailRequired, PatientDto> {

    @Override
    public void initialize(PhoneOrEmailRequired constraintAnnotation) {
    }

    @Override
    public boolean isValid(PatientDto dto, ConstraintValidatorContext context) {
        if (dto == null) {
            return true;
        }
        
        boolean hasPhone = dto.getPhoneNumber() != null && !dto.getPhoneNumber().trim().isEmpty();
        boolean hasEmail = dto.getEmail() != null && !dto.getEmail().trim().isEmpty();
        
        return hasPhone || hasEmail;
    }
}

