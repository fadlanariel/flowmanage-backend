# FlowManage Backend

FlowManage is a **project & task management backend API** built with **Spring Boot**, focusing on **clean architecture, real-world pagination, audit logging, and secure design**.

This project is intentionally designed as a **portfolio-grade backend**, emphasizing production patterns rather than simple CRUD.

---

## 🚀 Tech Stack

- Java 17  
- Spring Boot 3  
- Spring Data JPA  
- Spring Security (JWT)  
- PostgreSQL  
- Flyway Migration  
- OpenAPI / Swagger  
- Lombok  

---

## 🧱 Architecture

Layered architecture with strict separation of concerns:

Controller → Service → Repository → Database

Key principles:
- No circular dependencies
- Authorization & ownership enforced at service layer
- Admin features isolated from user ownership logic
- Pagination & validation handled centrally

---

## ✨ Features

### 🔐 Authentication
- JWT-based authentication
- Secured endpoints
- Ownership validation for user resources

---

### 📁 Project Management
- Create, update, delete projects
- List projects with pagination & sorting
- User-based ownership validation

---

### 📝 Task Management
- Tasks belong to projects
- Partial updates using PATCH
- Status handled via enum (`TODO`, `IN_PROGRESS`, `DONE`)
- Pagination & sorting supported

---

### 📜 Audit Logging (Admin)
- Records project & task actions
- Supports filtering & pagination
- Accessible only by admin endpoints

---

## 📦 API Response Format

All endpoints return a **consistent response wrapper**.

### Success
```json
{
  "data": { ... },
  "meta": {
    "page": 0,
    "size": 10,
    "totalElements": 25,
    "totalPages": 3
  }
}
```

### Error
```json
{
  "status": 401,
  "message": "Unauthorized",
  "path": "/api/projects"
}
```

---
## 🧭 Pagination & Guardrails

Default pagination fallback

- size limited to max 100
- Sort field whitelist
- Invalid sort ignored safely
Example:
```
?page=0&size=20&sort=createdAt,desc
```

---

## 🧪 Validation & Error Handling

- Bean Validation (@Valid)
- Centralized GlobalExceptionHandler
- Clean HTTP status mapping
- No internal stack traces leaked

---
## 📘 API Documentation

Swagger UI is available at:
```
/swagger-ui/index.html
```

Features:
- JWT Authorization support
- Auto-documented request & response models
- Pagination parameters included

---

## 🛠️ Running the Application
### Requirements
- Java 17
- PostgreSQL
- Maven

### Steps
```
git clone https://github.com/yourusername/flowmanage-backend.git
cd flowmanage-backend
mvn spring-boot:run
```

Database configuration is located in:
```
application.yml
```

Flyway migrations run automatically on startup.

---

## 🧠 Design Decisions
- PATCH for partial updates
- Ownership checks enforced at service layer
- Admin audit logs do not depend on project ownership
- Pageable & Specification used for flexible querying
- Ready for future RBAC extension

---

## 📌 Project Status
✅ Core backend features completed

✅ Production-ready design applied

⏸ Additional features intentionally out of scope

---

## 👤 Author

Fadlan Ariel

Backend Engineer

Spring Boot • REST API • System Design
