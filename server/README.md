# Patient Service - Server

Spring Boot REST API for patient management.

## Tech Stack

- **Java 21**
- **Spring Boot 3.4.12**
- **PostgreSQL** - Database
- **Flyway** - Database migrations
- **Spring Data JPA** - Data persistence
- **SpringDoc OpenAPI** - API documentation

## Prerequisites

- Java 21
- Maven 3.6+
- PostgreSQL database

## Setup

1. Create a PostgreSQL database:

   ```sql
   CREATE DATABASE patient_service;
   ```

2. Configure database connection in `src/main/resources/application.yaml` or use environment variables:

   - `DB_URL` - Database URL (default: `jdbc:postgresql://localhost:5432/patient_service`)
   - `DB_USERNAME` - Database username (default: `postgres`)
   - `DB_PASSWORD` - Database password (default: `123456789`)

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

The server will start on port `8083` by default.

## API Documentation

Once the server is running, access the API documentation at:

- Swagger UI: `http://localhost:8083/swagger-ui.html` or `http://localhost:8083/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8083/v3/api-docs`
- OpenAPI YAML: `http://localhost:8083/v3/api-docs.yaml`

**Note:** If the Swagger UI is not accessible, ensure the application is running and check the console logs for any errors.

## API Endpoints

- `GET /api/patient` - Get all patients (with pagination)
- `GET /api/patient/{id}` - Get patient by ID
- `POST /api/patient` - Create a new patient
- `PUT /api/patient/{id}` - Update a patient
- `PATCH /api/patient/{id}` - Partially update a patient
- `DELETE /api/patient/{id}` - Delete a patient

## Database Migrations

Database migrations are managed by Flyway and located in `src/main/resources/db/migration/`.
