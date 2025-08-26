# Auth-service
Spring-boot microservice that integrates with Keycloak to provide fast authentication and authorization using JWT
<p align="left">
  <img src="https://skillicons.dev/icons?i=java,spring,postgres,kafka,redis,docker,gradle,postman,git"/>
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
   git clone https://github.com/Nightingaale/auth-service.git
   ```
2. Move to the project directory:
   ```sh
   cd auth-service
   ```
3. Build microservice with Gradle:
    ```sh
   ./gradlew build
   ```
4. Start your microservice:
   ```sh
   ./gradlew bootrun
   ```
9. Verify all services are running:
   ```sh
   docker ps
   ```
10. Configure Keycloak:
- Location Keycloak file: `/.env-example`
- Location Keycloak config file: `.config`
- Enter "Create Realm" and take Keycloak config
- Fill your client's secret in application.yaml file
   ```sh
  keycloak:
   credentials:
     client-id: auth-service
     secret: <your-client-secret>
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

#### User's JWT

```http
  POST /realms/your-realm-name/protocol/openid-connect/token
```
