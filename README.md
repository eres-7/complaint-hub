# complaint-hub

## 📌 Overview
This is a **Complaint Hub** built with **Spring Boot**, **PostgreSQL**, **Hibernate** and **Liquibase**.  
It allows users to:
- Add new complaints
- Edit complaint content
- Retrieve stored complaints

The service ensures:
- Each **complaint** is unique by `productId` and `reporter`
- If a duplicate complaint is submitted, the **count is incremented** instead of inserting a new record
- The **country of the user** is determined based on the IP address using a geolocation service

---

## 🚀 **Getting Started**

### **1️⃣ Prerequisites**
Ensure you have the following installed:
- ✅ **Java 17+** (Required for Spring Boot 3)
- ✅ **Maven 3.8+** (For dependency management)
- ✅ **Docker** (For running PostgreSQL via Testcontainers)
- ✅ **PostgreSQL** (If running locally instead of Testcontainers)

### **🔹 Running PostgreSQL in Docker**

```sh
docker run --name postgres-db -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres -e POSTGRES_DB=complaints_db -p 5432:5432 -d postgres:latest
```


### **2️⃣ Running the Application**

Configure `application.yml` for your PostgreSQL in Docker:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/complaints_db
    username: postgres
    password: postgres
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  liquibase:
    change-log: classpath:/db/changelog/db.changelog-master.yaml
```

Then, **run the application:**

`mvn spring-boot:run`

### **3️⃣ API Documentation (Swagger)**

The API is documented using **OpenAPI (Swagger UI)**.

- **Run the application**
- Open your browser and visit:\
  👉 [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

---

## 🛠 Building the Project

To build the application:

`mvn clean install -Ptestcontainers`


## 🧪 Running Tests

By default, **Testcontainers-based tests are disabled**.\
To enable them, run tests with the `testcontainers` profile:

`mvn test -Ptestcontainers`

Otherwise, run standard unit tests:

`mvn test`

### Testcontainers support

This project
uses [Testcontainers at development time](https://docs.spring.io/spring-boot/3.4.3/reference/features/dev-services.html#features.dev-services.testcontainers).

Testcontainers has been configured to use the following Docker images:

* [`postgres:latest`](https://hub.docker.com/_/postgres)

---

## 📜 API Endpoints

| **Method** | **Endpoint**           | **Description**            |
| ---------- | ---------------------- | -------------------------- |
| `POST`     | `/api/complaints`      | Submit a new complaint     |
| `PUT`      | `/api/complaints/{id}` | Update complaint content   |
| `GET`      | `/api/complaints/{id}` | Retrieve a complaint by ID |

---

## 🔗 Project Structure

```
📂 complaint-hub
 ┣ 📂 src
 ┃ ┣ 📂 main
 ┃ ┃ ┣ 📂 java/pl/eres/complaint_hub
 ┃ ┃ ┃ ┣ 📂 complaint/controller    # REST Controllers
 ┃ ┃ ┃ ┣ 📂 complaint/service       # Business Logic
 ┃ ┃ ┃ ┣ 📂 complaint/repository    # JPA Repositories
 ┃ ┃ ┃ ┣ 📂 complaint/entity        # JPA Entities
 ┃ ┃ ┃ ┣ 📂 complaint/exception     # Custom Exceptions
 ┃ ┃ ┃ ┣ 📂 complaint/util          # Utility Classes
 ┃ ┃ ┣ 📂 resources
 ┃ ┃ ┃ ┣ 📂 db/changelog            # Liquibase Migration Scripts
 ┃ ┃ ┃ ┣ 📜 application.yml         # Configuration
 ┣ 📜 README.md
 ┣ 📜 pom.xml
```

---

## 🛠 Technologies Used

- **Java 21**
- **Spring Boot 3.4**
- **Spring Data JPA**
- **PostgreSQL**
- **Liquibase**
- **Testcontainers**
- **Swagger (OpenAPI)**
- **Docker**
- **Lombok**

---

🚀 **Enjoy using the Complaint Hub!** 🚀