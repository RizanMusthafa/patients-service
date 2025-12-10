# Patient Service

A full-stack patient management application with a Spring Boot backend and React frontend.

## Project Structure

- **`server/`** - Spring Boot REST API backend
  - See [server/README.md](./server/README.md) for details
- **`client/`** - React TypeScript frontend
  - See [client/README.md](./client/README.md) for details

## Features

- Patient CRUD operations (Create, Read, Update, Delete)
- Paginated patient listing
- Patient search and filtering
- Responsive Material-UI interface
- RESTful API with validation
- Database migrations with Flyway
- API documentation with Swagger/OpenAPI

## Quick Start

### Backend

1. Set up PostgreSQL database
2. Configure database credentials in `server/src/main/resources/application.yaml`
3. Navigate to `server/` and run:
   ```bash
   ./mvnw spring-boot:run
   ```

### Frontend

1. Navigate to `client/` and install dependencies:
   ```bash
   yarn install
   ```
2. Start the development server:
   ```bash
   yarn dev
   ```

## Technology Stack

**Backend:**

- Java 21, Spring Boot 3.4.12
- PostgreSQL, Flyway
- Spring Data JPA, Spring Validation

**Frontend:**

- React 19, TypeScript
- Vite, Material-UI
- Axios

## API Documentation

When the backend is running, access Swagger UI at:
`http://localhost:8083/swagger-ui.html`

## Future Improvements

### Backend

- [ ] Add request/response logging and monitoring
- [ ] Implement caching for frequently accessed data
- [ ] Support for advanced search and filtering
- [ ] Add data export functionality (CSV/PDF)

### Frontend

- [ ] Add form validation with better error handling
- [ ] Implement loading states and skeleton screens
- [ ] Implement advanced filtering and sorting
- [ ] Add patient detail page/view/modal
- [ ] Implement responsive design improvements
- [ ] Add dark mode support
- [ ] Implement data export functionality
