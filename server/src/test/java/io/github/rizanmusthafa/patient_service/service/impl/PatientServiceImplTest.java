package io.github.rizanmusthafa.patient_service.service.impl;

import io.github.rizanmusthafa.patient_service.dto.PageResponse;
import io.github.rizanmusthafa.patient_service.dto.PatientDto;
import io.github.rizanmusthafa.patient_service.exception.PatientNotFoundException;
import io.github.rizanmusthafa.patient_service.mapper.PatientMapper;
import io.github.rizanmusthafa.patient_service.model.Patient;
import io.github.rizanmusthafa.patient_service.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;
    private PatientDto patientDto;

    @BeforeEach
    void setUp() {
        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setEmail("john.doe@example.com");
        patient.setPhoneNumber("1234567890");
        patient.setCreatedAt(LocalDateTime.now());
        patient.setUpdatedAt(LocalDateTime.now());

        patientDto = new PatientDto();
        patientDto.setId(1L);
        patientDto.setFirstName("John");
        patientDto.setLastName("Doe");
        patientDto.setEmail("john.doe@example.com");
        patientDto.setPhoneNumber("1234567890");
    }

    @Test
    void findAll_ShouldReturnListOfPatients() {
        List<Patient> patients = Arrays.asList(patient);
        when(patientRepository.findAll()).thenReturn(patients);
        when(patientMapper.toDto(patient)).thenReturn(patientDto);

        List<PatientDto> result = patientService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(patientDto, result.get(0));
        verify(patientRepository).findAll();
        verify(patientMapper).toDto(patient);
    }

    @Test
    void findAll_WithPagination_ShouldReturnPageResponse() {
        List<Patient> patients = Arrays.asList(patient);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Patient> patientPage = new PageImpl<>(patients, pageable, 1L);

        when(patientRepository.findAll(pageable)).thenReturn(patientPage);
        when(patientMapper.toDto(patient)).thenReturn(patientDto);

        PageResponse<PatientDto> result = patientService.findAll(0, 10);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(patientDto, result.getContent().get(0));
        assertEquals(0, result.getPage());
        assertEquals(10, result.getSize());
        assertEquals(1L, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertTrue(result.isFirst());
        assertTrue(result.isLast());

        verify(patientRepository).findAll(pageable);
        verify(patientMapper).toDto(patient);
    }

    @Test
    void findById_WhenPatientExists_ShouldReturnPatientDto() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.toDto(patient)).thenReturn(patientDto);

        PatientDto result = patientService.findById(1L);

        assertNotNull(result);
        assertEquals(patientDto, result);
        verify(patientRepository).findById(1L);
        verify(patientMapper).toDto(patient);
    }

    @Test
    void findById_WhenPatientNotFound_ShouldThrowException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.findById(1L));
        verify(patientRepository).findById(1L);
        verify(patientMapper, never()).toDto(any());
    }

    @Test
    void create_ShouldSaveAndReturnPatientDto() {
        PatientDto newDto = new PatientDto();
        newDto.setFirstName("Jane");
        newDto.setLastName("Smith");
        newDto.setEmail("jane@example.com");

        Patient newPatient = new Patient();
        newPatient.setFirstName("Jane");
        newPatient.setLastName("Smith");
        newPatient.setEmail("jane@example.com");

        Patient savedPatient = new Patient();
        savedPatient.setId(2L);
        savedPatient.setFirstName("Jane");
        savedPatient.setLastName("Smith");
        savedPatient.setEmail("jane@example.com");

        PatientDto savedDto = new PatientDto();
        savedDto.setId(2L);
        savedDto.setFirstName("Jane");
        savedDto.setLastName("Smith");
        savedDto.setEmail("jane@example.com");

        when(patientMapper.toEntity(newDto)).thenReturn(newPatient);
        when(patientRepository.save(newPatient)).thenReturn(savedPatient);
        when(patientMapper.toDto(savedPatient)).thenReturn(savedDto);

        PatientDto result = patientService.create(newDto);

        assertNotNull(result);
        assertEquals(2L, result.getId());
        verify(patientMapper).toEntity(newDto);
        verify(patientRepository).save(newPatient);
        verify(patientMapper).toDto(savedPatient);
    }

    @Test
    void update_WhenPatientExists_ShouldUpdateAndReturnPatientDto() {
        PatientDto updateDto = new PatientDto();
        updateDto.setFirstName("John Updated");
        updateDto.setLastName("Doe Updated");
        updateDto.setEmail("john.updated@example.com");

        Patient updatedPatient = new Patient();
        updatedPatient.setId(1L);
        updatedPatient.setFirstName("John Updated");
        updatedPatient.setLastName("Doe Updated");
        updatedPatient.setEmail("john.updated@example.com");

        PatientDto updatedDto = new PatientDto();
        updatedDto.setId(1L);
        updatedDto.setFirstName("John Updated");
        updatedDto.setLastName("Doe Updated");
        updatedDto.setEmail("john.updated@example.com");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(updatedPatient);
        when(patientMapper.toDto(updatedPatient)).thenReturn(updatedDto);

        PatientDto result = patientService.update(1L, updateDto);

        assertNotNull(result);
        assertEquals("John Updated", result.getFirstName());
        verify(patientRepository).findById(1L);
        verify(patientRepository).save(any(Patient.class));
        verify(patientMapper).toDto(updatedPatient);
    }

    @Test
    void update_WhenPatientNotFound_ShouldThrowException() {
        PatientDto updateDto = new PatientDto();
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.update(1L, updateDto));
        verify(patientRepository).findById(1L);
        verify(patientRepository, never()).save(any());
    }

    @Test
    void delete_WhenPatientExists_ShouldDeletePatient() {
        when(patientRepository.existsById(1L)).thenReturn(true);

        patientService.delete(1L);

        verify(patientRepository).existsById(1L);
        verify(patientRepository).deleteById(1L);
    }

    @Test
    void delete_WhenPatientNotFound_ShouldThrowException() {
        when(patientRepository.existsById(1L)).thenReturn(false);

        assertThrows(PatientNotFoundException.class, () -> patientService.delete(1L));
        verify(patientRepository).existsById(1L);
        verify(patientRepository, never()).deleteById(any());
    }

    @Test
    void patch_WhenPatientExists_ShouldPartiallyUpdateAndReturnPatientDto() {
        PatientDto patchDto = new PatientDto();
        patchDto.setFirstName("John Patched");

        Patient patchedPatient = new Patient();
        patchedPatient.setId(1L);
        patchedPatient.setFirstName("John Patched");
        patchedPatient.setLastName("Doe");

        PatientDto patchedDto = new PatientDto();
        patchedDto.setId(1L);
        patchedDto.setFirstName("John Patched");
        patchedDto.setLastName("Doe");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patchedPatient);
        when(patientMapper.toDto(patchedPatient)).thenReturn(patchedDto);

        PatientDto result = patientService.patch(1L, patchDto);

        assertNotNull(result);
        assertEquals("John Patched", result.getFirstName());
        verify(patientRepository).findById(1L);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void patch_WhenPatientNotFound_ShouldThrowException() {
        PatientDto patchDto = new PatientDto();
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PatientNotFoundException.class, () -> patientService.patch(1L, patchDto));
        verify(patientRepository).findById(1L);
        verify(patientRepository, never()).save(any());
    }
}
