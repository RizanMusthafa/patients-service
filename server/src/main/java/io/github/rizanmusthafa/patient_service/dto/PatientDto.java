package io.github.rizanmusthafa.patient_service.dto;

import io.github.rizanmusthafa.patient_service.validation.PhoneOrEmailRequired;
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
public class PatientDto {

    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private String address;

    private String city;

    private String state;

    private String zipCode;

    private String phoneNumber;

    @Email(message = "Email should be valid")
    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}

