# Employee Management API

A production-ready REST API built with Spring Boot for managing employees and departments. The API supports full CRUD operations, pagination, sorting, and department-based filtering.

---

## Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Data JPA**
- **Microsoft SQL Server (MSSQL)**
- **Lombok**
- **Maven**

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- Microsoft SQL Server instance running

### Configuration

Set the following environment variables before running:

| Variable | Description | Default |
|---|---|---|
| `SERVER_PORT` | Port the app runs on | `8080` |
| `SQL_PORT` | MSSQL port | `1434` |
| `DB_NAME` | Database name | `mydb` |
| `SA_PASSWORD` | MSSQL SA password | — |

### Running the App

```bash
mvn clean install
mvn spring-boot:run
```

The app will start on `http://localhost:8080`. The database and tables are created automatically on startup via Hibernate.

---

## Base URL

```
http://localhost:8080/api
```

---

## Endpoints

### Departments

#### Create Department
```
POST /api/departments
```
**Request Body:**
```json
{
  "name": "Engineering"
}
```
**Response:** `201 Created`
```json
{
  "message": "Department created successfully",
  "data": {
    "id": 1,
    "name": "Engineering"
  },
  "timestamp": "2026-03-30T11:55:00"
}
```

---

#### Get All Departments
```
GET /api/departments
```
**Response:** `200 OK`
```json
{
  "message": "Departments retrieved successfully",
  "data": [
    { "id": 1, "name": "Engineering" },
    { "id": 2, "name": "HR" }
  ],
  "timestamp": "2026-03-30T11:55:00"
}
```

---

#### Get Department By ID
```
GET /api/departments/{id}
```
**Response:** `200 OK`
```json
{
  "message": "Department retrieved successfully",
  "data": {
    "id": 1,
    "name": "Engineering"
  },
  "timestamp": "2026-03-30T11:55:00"
}
```

---

#### Update Department
```
PUT /api/departments/{id}
```
**Request Body:**
```json
{
  "name": "Product Engineering"
}
```
**Response:** `200 OK`
```json
{
  "message": "Department updated successfully",
  "data": {
    "id": 1,
    "name": "Product Engineering"
  },
  "timestamp": "2026-03-30T11:55:00"
}
```

---

#### Delete Department
```
DELETE /api/departments/{id}
```
**Response:** `204 No Content`

> **Note:** A department cannot be deleted if it has employees assigned to it. A `409 Conflict` will be returned in that case.

---

### Employees

#### Create Employee
```
POST /api/employees
```
**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "departmentId": 1
}
```
**Response:** `201 Created`
```json
{
  "message": "Employee created successfully",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "department": {
      "id": 1,
      "name": "Engineering"
    }
  },
  "timestamp": "2026-03-30T11:55:00"
}
```

---

#### Get All Employees
```
GET /api/employees
```
**Query Parameters:**

| Parameter | Type | Default | Description |
|---|---|---|---|
| `page` | int | `1` | Page number (1-based) |
| `size` | int | `10` | Page size |
| `sortField` | string | `id` | Field to sort by |
| `sortDirection` | string | `ASC` | Sort direction (`ASC` or `DESC`) |
| `departmentId` | Long | — | Filter by department ID (optional) |

**Example:**
```
GET /api/employees?page=1&size=10&sortField=lastName&sortDirection=ASC
GET /api/employees?departmentId=1&page=1&size=5
```

**Response:** `200 OK` — paginated list of employees.
```json
{
  "message": "Employees retrieved successfully",
  "data": {
    "content": [ ... ],
    "totalElements": 50,
    "totalPages": 5,
    "size": 10,
    "number": 0
  },
  "timestamp": "2026-03-30T11:55:00"
}
```

---

#### Get Employee By ID
```
GET /api/employees/{id}
```
**Response:** `200 OK`
```json
{
  "message": "Employee retrieved successfully",
  "data": {
    "id": 1,
    "firstName": "John",
    "lastName": "Doe",
    "email": "john.doe@example.com",
    "department": {
      "id": 1,
      "name": "Engineering"
    }
  },
  "timestamp": "2026-03-30T11:55:00"
}
```

---

#### Update Employee (Full)
```
PUT /api/employees/{id}
```
**Request Body:**
```json
{
  "firstName": "John",
  "lastName": "Smith",
  "email": "john.smith@example.com",
  "departmentId": 2
}
```
**Response:** `200 OK`
```json
{
  "message": "Employee updated successfully",
  "data": { ... },
  "timestamp": "2026-03-30T11:55:00"
}
```

---

#### Partial Update Employee
```
PATCH /api/employees/{id}
```
Only include the fields you want to update. All fields are optional.

**Request Body:**
```json
{
  "email": "newemail@example.com"
}
```
**Response:** `200 OK`
```json
{
  "message": "Employee updated successfully",
  "data": { ... },
  "timestamp": "2026-03-30T11:55:00"
}
```

---

#### Delete Employee
```
DELETE /api/employees/{id}
```
**Response:** `204 No Content`

---

## Error Responses

All errors return a consistent JSON structure:

```json
{
  "error": "Employee not found with id: 5"
}
```

Validation errors return a map of field-level messages:

```json
{
  "errors": {
    "email": "Invalid email format",
    "firstName": "First name is required"
  }
}
```

| Status Code | Meaning |
|---|---|
| `400 Bad Request` | Validation failed or bad input |
| `404 Not Found` | Resource not found |
| `409 Conflict` | Duplicate email or department name, or deleting a department with employees |

---

## Architecture

The project follows a strict layered architecture:

```
Controller  →  Service  →  Repository  →  Database
```

- **Controller** — handles HTTP, input validation (`@Valid`), delegates to service
- **Service** — business logic, exception throwing
- **Repository** — Spring Data JPA, no custom queries unless necessary
- **DTOs** — separate request and response objects, raw entities are never exposed directly
- **GlobalExceptionHandler** — consistent error responses across all endpoints