package io.github.rizanmusthafa.patient_service.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI patientServiceOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Patient Service API")
                                                .description("REST API for managing patient records. " +
                                                                "Provides endpoints for creating, reading, updating, and deleting patient information. "
                                                                +
                                                                "Supports pagination for listing patients and includes validation for required fields.")
                                                .version("v1.0.0")
                                                .contact(new Contact()
                                                                .name("Patient Service Support")
                                                                .email("support@example.com")))
                                .tags(List.of(
                                                new Tag()
                                                                .name("Patient")
                                                                .description("Patient management operations")));
        }
}
