package io.github.rizanmusthafa.patient_service.service;

import io.github.rizanmusthafa.patient_service.dto.PageResponse;
import io.github.rizanmusthafa.patient_service.dto.PatientDto;

import java.util.List;

public interface PatientService {
    List<PatientDto> findAll();
    PageResponse<PatientDto> findAll(int page, int size);

    PatientDto findById(Long id);

    PatientDto create(PatientDto dto);

    PatientDto update(Long id, PatientDto dto);

    PatientDto patch(Long id, PatientDto dto);

    void delete(Long id);
}
