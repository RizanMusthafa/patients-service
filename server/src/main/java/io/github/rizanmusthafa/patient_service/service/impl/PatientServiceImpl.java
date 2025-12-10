package io.github.rizanmusthafa.patient_service.service.impl;

import io.github.rizanmusthafa.patient_service.dto.PatientDto;
import io.github.rizanmusthafa.patient_service.exception.PatientNotFoundException;
import io.github.rizanmusthafa.patient_service.mapper.PatientMapper;
import io.github.rizanmusthafa.patient_service.model.Patient;
import io.github.rizanmusthafa.patient_service.repository.PatientRepository;
import io.github.rizanmusthafa.patient_service.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PatientDto> findAll() {
        return patientRepository.findAll().stream()
                .map(patientMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PatientDto findById(Long id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
        return patientMapper.toDto(patient);
    }

    @Override
    public PatientDto create(PatientDto dto) {
        Patient patient = patientMapper.toEntity(dto);
        Patient savedPatient = patientRepository.save(patient);
        return patientMapper.toDto(savedPatient);
    }

    @Override
    public PatientDto update(Long id, PatientDto dto) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));

        existingPatient.setFirstName(dto.getFirstName());
        existingPatient.setLastName(dto.getLastName());
        existingPatient.setAddress(dto.getAddress());
        existingPatient.setCity(dto.getCity());
        existingPatient.setState(dto.getState());
        existingPatient.setZipCode(dto.getZipCode());
        existingPatient.setPhoneNumber(dto.getPhoneNumber());
        existingPatient.setEmail(dto.getEmail());

        Patient updatedPatient = patientRepository.save(existingPatient);
        return patientMapper.toDto(updatedPatient);
    }

    @Override
    public PatientDto patch(Long id, PatientDto dto) {
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));

        // Only update fields that are provided (non-null)
        if (dto.getFirstName() != null) {
            existingPatient.setFirstName(dto.getFirstName());
        }
        if (dto.getLastName() != null) {
            existingPatient.setLastName(dto.getLastName());
        }
        if (dto.getAddress() != null) {
            existingPatient.setAddress(dto.getAddress());
        }
        if (dto.getCity() != null) {
            existingPatient.setCity(dto.getCity());
        }
        if (dto.getState() != null) {
            existingPatient.setState(dto.getState());
        }
        if (dto.getZipCode() != null) {
            existingPatient.setZipCode(dto.getZipCode());
        }
        if (dto.getPhoneNumber() != null) {
            existingPatient.setPhoneNumber(dto.getPhoneNumber());
        }
        if (dto.getEmail() != null) {
            existingPatient.setEmail(dto.getEmail());
        }

        Patient updatedPatient = patientRepository.save(existingPatient);
        return patientMapper.toDto(updatedPatient);
    }

    @Override
    public void delete(Long id) {
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
    }
}
