package io.github.rizanmusthafa.patient_service.dto;

import io.github.rizanmusthafa.patient_service.validation.PhoneOrEmailRequired;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PhoneOrEmailRequired
@Schema(description = "Patient data transfer object")
public class PatientDto {

    @Schema(description = "Patient unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Schema(description = "Patient's first name", example = "John", required = true)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Schema(description = "Patient's last name", example = "Doe", required = true)
    private String lastName;

    @Schema(description = "Patient's street address", example = "123 Main Street")
    private String address;

    @Schema(description = "Patient's city", example = "New York")
    private String city;

    @Schema(description = "Patient's state or province", example = "NY")
    private String state;

    @Schema(description = "Patient's postal/zip code", example = "10001")
    private String zipCode;

    @Schema(description = "Patient's phone number (required if email is not provided)", example = "+1234567890")
    private String phoneNumber;

    @Email(message = "Email should be valid")
    @Schema(description = "Patient's email address (required if phone number is not provided)", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Timestamp when the patient record was created", example = "2024-01-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the patient record was last updated", example = "2024-01-15T10:30:00", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updatedAt;
}
