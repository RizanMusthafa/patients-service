package io.github.rizanmusthafa.patient_service.mapper;

import io.github.rizanmusthafa.patient_service.dto.PatientDto;
import io.github.rizanmusthafa.patient_service.model.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {

    public PatientDto toDto(Patient patient) {
        if (patient == null) {
            return null;
        }
        return new PatientDto(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getAddress(),
                patient.getCity(),
                patient.getState(),
                patient.getZipCode(),
                patient.getPhoneNumber(),
                patient.getEmail(),
                patient.getCreatedAt(),
                patient.getUpdatedAt()
        );
    }

    public Patient toEntity(PatientDto dto) {
        if (dto == null) {
            return null;
        }
        Patient patient = new Patient();
        patient.setId(dto.getId());
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setAddress(dto.getAddress());
        patient.setCity(dto.getCity());
        patient.setState(dto.getState());
        patient.setZipCode(dto.getZipCode());
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setEmail(dto.getEmail());
        return patient;
    }
}

