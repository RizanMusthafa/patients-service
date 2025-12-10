package io.github.rizanmusthafa.patient_service.controller;

import io.github.rizanmusthafa.patient_service.dto.PageResponse;
import io.github.rizanmusthafa.patient_service.dto.PatientDto;
import io.github.rizanmusthafa.patient_service.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Patient", description = "Patient management API endpoints")
public class PatientController {

        private final PatientService patientService;

        @Operation(summary = "Get all patients", description = "Retrieve a paginated list of all patients. Supports pagination with page and size parameters.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved list of patients", content = @Content(schema = @Schema(implementation = PageResponse.class))),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @GetMapping
        public ResponseEntity<PageResponse<PatientDto>> getAllPatients(
                        @Parameter(description = "Page number (0-indexed)", example = "0") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Number of items per page", example = "10") @RequestParam(defaultValue = "10") int size) {
                PageResponse<PatientDto> patients = patientService.findAll(page, size);
                return ResponseEntity.ok(patients);
        }

        @Operation(summary = "Get patient by ID", description = "Retrieve a specific patient by their unique identifier.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Successfully retrieved patient", content = @Content(schema = @Schema(implementation = PatientDto.class))),
                        @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @GetMapping("/{id}")
        public ResponseEntity<PatientDto> getPatientById(
                        @Parameter(description = "Patient ID", required = true, example = "1") @PathVariable Long id) {
                PatientDto patient = patientService.findById(id);
                return ResponseEntity.ok(patient);
        }

        @Operation(summary = "Create a new patient", description = "Create a new patient record. First name, last name, and either phone number or email are required.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "Patient successfully created", content = @Content(schema = @Schema(implementation = PatientDto.class))),
                        @ApiResponse(responseCode = "400", description = "Validation error - invalid input data", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PostMapping
        public ResponseEntity<PatientDto> createPatient(
                        @Parameter(description = "Patient data to create", required = true) @Valid @RequestBody PatientDto patientDto) {
                PatientDto createdPatient = patientService.create(patientDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdPatient);
        }

        @Operation(summary = "Update patient", description = "Update an existing patient record. All fields must be provided (full update).")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Patient successfully updated", content = @Content(schema = @Schema(implementation = PatientDto.class))),
                        @ApiResponse(responseCode = "400", description = "Validation error - invalid input data", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PutMapping("/{id}")
        public ResponseEntity<PatientDto> updatePatient(
                        @Parameter(description = "Patient ID", required = true, example = "1") @PathVariable Long id,
                        @Parameter(description = "Updated patient data", required = true) @Valid @RequestBody PatientDto patientDto) {
                PatientDto updatedPatient = patientService.update(id, patientDto);
                return ResponseEntity.ok(updatedPatient);
        }

        @Operation(summary = "Partially update patient", description = "Partially update an existing patient record. Only provided fields will be updated (partial update).")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "Patient successfully updated", content = @Content(schema = @Schema(implementation = PatientDto.class))),
                        @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @PatchMapping("/{id}")
        public ResponseEntity<PatientDto> patchPatient(
                        @Parameter(description = "Patient ID", required = true, example = "1") @PathVariable Long id,
                        @Parameter(description = "Partial patient data to update", required = true) @RequestBody PatientDto patientDto) {
                PatientDto updatedPatient = patientService.patch(id, patientDto);
                return ResponseEntity.ok(updatedPatient);
        }

        @Operation(summary = "Delete patient", description = "Delete a patient record by their unique identifier.")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "204", description = "Patient successfully deleted", content = @Content),
                        @ApiResponse(responseCode = "404", description = "Patient not found", content = @Content),
                        @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deletePatient(
                        @Parameter(description = "Patient ID", required = true, example = "1") @PathVariable Long id) {
                patientService.delete(id);
                return ResponseEntity.noContent().build();
        }
}
