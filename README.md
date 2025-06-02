# Auth-service
Spring-boot microservice that integrates with Keycloak to provide robust authentication and authorization using JWT
<p align="left">
  <img src="https://skillicons.dev/icons?i=java,spring,postgres,kafka,redis,docker,gradle,postman,git"/>
</p>
  </p>

# Features

- SSO: Provides an authentication system, allowing users to log in once and access multiple applications without needing to re-enter credentials
- Identity and Access Management: Manage user roles and permissions, ensuring secure access control for applications
- Scalable Architecture: Supports integration with third-party identity providers

# Prerequisites
- Java Development Kit(JDK): Version 17 or higher
- Gradle: For project build and dependency management
- Keycloak: Authentication management

## Getting Started

Set up Auth-Service locally using Docker Compose for the dev environment.

### Prerequisites

Ensure you have the following installed:
- Java 17
- Gradle
- Docker
- Keycloak, PostgreSQL, Kafka, Redis (use Docker Compose)
  
  ```sh
  java --version
  gradle --version
  docker --version
  ```

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/Nightingaale/Auth-Service.git
   ```
2. Move to the project directory):
   ```sh
   cd Auth-Service
   ```
3. Build microservice with Gradle:
    ```sh
   ./gradlew build
   ```
5. In src/main/resources/application.yml, configure the PostgreSQL settings:
   ```sh
   datasource:
    driver-class-name: org.postgresql.Driver
    url: <your-db-url>
    username: <your-db-username>
    password: <your-db-password>
    ```
7. Start the dev environment with Docker Compose:
   ```sh
   docker-compose up -d
   ```
8. Verify all services are running:
   ```sh
   docker ps
   ```
9. Configure Keycloak:
- Location Keycloak file: `/.env-example`
- Access the Keycloak admin console at http://localhost:8080
   ```sh
  keycloak:
   jwk-certs: <your-jwk-certs>
   realm: <your-realm>
   auth-server-url: <your-auth-server-url>
   resource: <your-client>
   credentials:
     client-id: <your-client>
     secret: <your-client-secret>
  use-resource-role-mappings: true
   ```

## API Reference

#### Sign up

```http
  POST /api/v1/auth/sign-up
```

#### Sign in

```http
  POST /api/v1/auth/sign-in
```

#### Logout

```http
  POST /api/v1/auth/logout
```

#### Remove

```http
  POST /api/v1/auth/remove-user
```

#### User's JWT Token

```http
  POST /realms/your-realm-name/protocol/openid-connect/token
```
