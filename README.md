# Student Management REST API

Spring Boot REST API for managing students, built with Spring Data JPA (Hibernate), MySQL and Redis caching.

## Tech stack
Java 25 · Spring Boot 3 · Spring Data JPA / Hibernate · MySQL 8 · Redis · Bean Validation · Swagger UI · Docker · JUnit 5 + Mockito

## Features
- CRUD for students (`/api/students`)
- Validation with clear error messages (400)
- Duplicate email protection (409) and not-found handling (404) via a global exception handler
- Pagination, sorting and search by name
- Redis cache on `GET /api/students/{id}` (updated on update, cleared on delete)
- Swagger UI for live testing
- Credentials via environment variables (nothing hardcoded)

## Architecture
Controller -> Service (business logic + caching) -> Repository (Spring Data JPA) -> MySQL
Service <-> Redis (cache)

## Run with Docker (easiest)
```bash
docker compose up --build
```
Open http://localhost:8080/swagger-ui.html

## Run locally
1. Start MySQL (3306) and Redis (6379).
2. Set environment variable `DB_PASSWORD` (and `DB_USER` if not root).
3. `mvn spring-boot:run`

## API
| Method | URL | Description |
|---|---|---|
| POST | /api/students | Create |
| GET | /api/students?name=&page=0&size=10 | List, search, paginate |
| GET | /api/students/{id} | Get one (cached) |
| PUT | /api/students/{id} | Update |
| DELETE | /api/students/{id} | Delete |

Sample body:
```json
{ "name": "Maddy", "email": "maddy@example.com", "course": "CSE", "age": 22 }
```

## Tests
```bash
mvn test
```
