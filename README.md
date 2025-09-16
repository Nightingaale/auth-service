# Auth-Service (Production)
A Spring Boot microservice that integrates with Keycloak to provide fast authentication and authorization using JWT
<p align="left">
  <img src="https://skillicons.dev/icons?i=java,spring,postgres,kafka,redis,docker,gradle,prometheus,postman,git"/>
</p>

# Features

- SSO: Provides an authentication system, allowing users to log in once and access multiple applications without needing to re-enter credentials
- Identity and Access Management: Manage user roles and permissions, ensuring secure access control for applications
- Scalable Architecture: Supports integration with third-party identity providers
- Event-Driven User Activity Logging: Publishes user-related events (registration, login, etc.) to Kafka, enabling asynchronous processing, real-time analytics, and integration with other microservices
- User Token Blacklisting: Maintains a Redis hash of revoked or expired users' JWT, ensuring that invalidated tokens cannot be used for authentication and enhancing overall security

# Prerequisites
- Java Development Kit(JDK): Version 17 or higher
- Gradle: For project build and dependency management
- Keycloak: Authentication management
- Vault: Keep your API keys, JWT tokens & secure data
  
  ```sh
  java --version
  gradle --version
  docker --version
  ```
