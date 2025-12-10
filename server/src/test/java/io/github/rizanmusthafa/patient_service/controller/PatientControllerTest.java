package io.github.rizanmusthafa.patient_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.rizanmusthafa.patient_service.dto.PageResponse;
import io.github.rizanmusthafa.patient_service.dto.PatientDto;
import io.github.rizanmusthafa.patient_service.exception.PatientNotFoundException;
import io.github.rizanmusthafa.patient_service.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PatientService patientService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        void getAllPatients_ShouldReturnPaginatedListOfPatients() throws Exception {
                PatientDto patient1 = createPatientDto(1L, "John", "Doe");
                PatientDto patient2 = createPatientDto(2L, "Jane", "Smith");
                List<PatientDto> patients = Arrays.asList(patient1, patient2);

                PageResponse<PatientDto> pageResponse = new PageResponse<>(
                                patients, 0, 10, 2L, 1, true, true);

                when(patientService.findAll(0, 10)).thenReturn(pageResponse);

                mockMvc.perform(get("/api/patient")
                                .param("page", "0")
                                .param("size", "10"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.content").isArray())
                                .andExpect(jsonPath("$.content[0].id").value(1L))
                                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                                .andExpect(jsonPath("$.content[1].id").value(2L))
                                .andExpect(jsonPath("$.content[1].firstName").value("Jane"))
                                .andExpect(jsonPath("$.page").value(0))
                                .andExpect(jsonPath("$.size").value(10))
                                .andExpect(jsonPath("$.totalElements").value(2L))
                                .andExpect(jsonPath("$.totalPages").value(1))
                                .andExpect(jsonPath("$.first").value(true))
                                .andExpect(jsonPath("$.last").value(true));

                verify(patientService).findAll(0, 10);
        }

        @Test
        void getAllPatients_WithDefaultPagination_ShouldReturnFirstPage() throws Exception {
                PatientDto patient1 = createPatientDto(1L, "John", "Doe");
                List<PatientDto> patients = Arrays.asList(patient1);

                PageResponse<PatientDto> pageResponse = new PageResponse<>(
                                patients, 0, 10, 1L, 1, true, true);

                when(patientService.findAll(0, 10)).thenReturn(pageResponse);

                mockMvc.perform(get("/api/patient"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content").isArray())
                                .andExpect(jsonPath("$.page").value(0))
                                .andExpect(jsonPath("$.size").value(10));

                verify(patientService).findAll(0, 10);
        }

        @Test
        void getPatientById_WhenPatientExists_ShouldReturnPatient() throws Exception {
                PatientDto patient = createPatientDto(1L, "John", "Doe");

                when(patientService.findById(1L)).thenReturn(patient);

                mockMvc.perform(get("/api/patient/1"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.firstName").value("John"))
                                .andExpect(jsonPath("$.lastName").value("Doe"));

                verify(patientService).findById(1L);
        }

        @Test
        void getPatientById_WhenPatientNotFound_ShouldReturn404() throws Exception {
                when(patientService.findById(1L))
                                .thenThrow(new PatientNotFoundException("Patient not found with id: 1"));

                mockMvc.perform(get("/api/patient/1"))
                                .andExpect(status().isNotFound())
                                .andExpect(jsonPath("$.error").value("Patient Not Found"));

                verify(patientService).findById(1L);
        }

        @Test
        void createPatient_WithValidData_ShouldReturn201() throws Exception {
                PatientDto inputDto = createPatientDto(null, "John", "Doe");
                PatientDto createdDto = createPatientDto(1L, "John", "Doe");

                when(patientService.create(any(PatientDto.class))).thenReturn(createdDto);

                mockMvc.perform(post("/api/patient")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(inputDto)))
                                .andExpect(status().isCreated())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.firstName").value("John"));

                verify(patientService).create(any(PatientDto.class));
        }

        @Test
        void createPatient_WithInvalidData_ShouldReturn400() throws Exception {
                PatientDto invalidDto = new PatientDto();
                invalidDto.setFirstName("");

                mockMvc.perform(post("/api/patient")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidDto)))
                                .andExpect(status().isBadRequest());

                verify(patientService, never()).create(any());
        }

        @Test
        void updatePatient_WhenPatientExists_ShouldReturn200() throws Exception {
                PatientDto updateDto = createPatientDto(1L, "John Updated", "Doe");
                when(patientService.update(eq(1L), any(PatientDto.class))).thenReturn(updateDto);

                mockMvc.perform(put("/api/patient/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L))
                                .andExpect(jsonPath("$.firstName").value("John Updated"));

                verify(patientService).update(eq(1L), any(PatientDto.class));
        }

        @Test
        void updatePatient_WhenPatientNotFound_ShouldReturn404() throws Exception {
                PatientDto updateDto = createPatientDto(1L, "John", "Doe");
                when(patientService.update(eq(1L), any(PatientDto.class)))
                                .thenThrow(new PatientNotFoundException("Patient not found with id: 1"));

                mockMvc.perform(put("/api/patient/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDto)))
                                .andExpect(status().isNotFound());

                verify(patientService).update(eq(1L), any(PatientDto.class));
        }

        @Test
        void patchPatient_WhenPatientExists_ShouldReturn200() throws Exception {
                PatientDto patchDto = new PatientDto();
                patchDto.setFirstName("John Patched");

                PatientDto patchedDto = createPatientDto(1L, "John Patched", "Doe");
                when(patientService.patch(eq(1L), any(PatientDto.class))).thenReturn(patchedDto);

                mockMvc.perform(patch("/api/patient/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(patchDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.firstName").value("John Patched"));

                verify(patientService).patch(eq(1L), any(PatientDto.class));
        }

        @Test
        void deletePatient_WhenPatientExists_ShouldReturn204() throws Exception {
                doNothing().when(patientService).delete(1L);

                mockMvc.perform(delete("/api/patient/1"))
                                .andExpect(status().isNoContent());

                verify(patientService).delete(1L);
        }

        @Test
        void deletePatient_WhenPatientNotFound_ShouldReturn404() throws Exception {
                doThrow(new PatientNotFoundException("Patient not found with id: 1"))
                                .when(patientService).delete(1L);

                mockMvc.perform(delete("/api/patient/1"))
                                .andExpect(status().isNotFound());

                verify(patientService).delete(1L);
        }

        private PatientDto createPatientDto(Long id, String firstName, String lastName) {
                PatientDto dto = new PatientDto();
                dto.setId(id);
                dto.setFirstName(firstName);
                dto.setLastName(lastName);
                dto.setEmail("test@example.com");
                dto.setPhoneNumber("1234567890");
                dto.setCreatedAt(LocalDateTime.now());
                dto.setUpdatedAt(LocalDateTime.now());
                return dto;
        }
}
