# ProjectConnect: A Resilient Microservices Platform

ProjectConnect is a comprehensive, cloud-native application built using a microservices architecture. It demonstrates key patterns for building robust, scalable, and resilient systems with **Spring Boot**, **Spring Cloud**, and **Resilience4j**.

The application simulates a simple social preference system where authenticated users can "like" other profiles, showcasing a real-world use case for inter-service communication and fault tolerance.

## Architectural Overview

This project follows a classic microservices pattern with a central discovery server, an API gateway, and several backend services, all containerized with Docker.

```
    subgraph "Client"
        User
    end

    subgraph "Infrastructure"
        A[API Gateway <br> (Port 8030)]
        D[Discovery Server (Eureka) <br> (Port 8761)]
    end

    subgraph "Backend Services"
        U[User Service <br> (Authentication & User Info)]
        S[Sapient Service <br> (Dummy User Profiles)]
        P[Preferences Service <br> (Likes & Business Logic)]
    end

    User -->|HTTP Requests| A
    A -->|Routes to...| U
    A -->|Routes to...| S
    A -->|Routes to...| P

    U <-->|Registers with| D
    S <-->|Registers with| D
    P <-->|Registers with| D
    A <-->|Discovers Services| D

    P -->|Feign Client call| U
    P -->|Feign Client call| S
```

### Core Concepts & Features

*   **Service Discovery**: Uses **Netflix Eureka** to allow services to register themselves and discover others dynamically.
*   **Centralized Entry Point**: A dedicated **API Gateway** (built with Spring Cloud Gateway) acts as a single entry point for all client requests, handling routing and cross-cutting concerns.
*   **JWT Authentication**: The `user-service` handles user registration and login, issuing JWTs for secure, stateless authentication. The API Gateway validates these tokens for protected routes.
*   **Inter-Service Communication**: Services communicate with each other using declarative REST clients via **Spring Cloud OpenFeign**.
*   **Fault Tolerance & Resilience**: The `preferences-service` is hardened with **Resilience4j**, implementing:
    *   **Circuit Breaker**: Prevents cascading failures by stopping requests to failing services (`user-service`, `sapient-service`).
    *   **Retry**: Automatically retries failed requests due to transient network issues.
    *   **Rate Limiter**: Protects the API from being overwhelmed by too many requests in a short period.
*   **Containerization**: Every service is fully containerized using **Docker** and orchestrated with **Docker Compose** for easy setup and deployment.

### Services Overview

| Service               | Port (Host) | Description                                                                                             |
| --------------------- | ----------- | ------------------------------------------------------------------------------------------------------- |
| `discovery-server`    | `8761`      | The Eureka server for service registration and discovery.                                               |
| `api-gateway`         | `8030`      | The single entry point for all incoming requests. Handles routing and JWT validation.                   |
| `user-service`        | `8010`  | Manages user accounts, handles signup/login, and generates JWTs.                                        |
| `sapient-service`     | `8020`  | Loads and serves a collection of dummy user profiles from `dummyjson.com` and provides search functionality. |
| `preferences-service` | `8040`  | Contains the core business logic. Allows authenticated users to "like" dummy users, communicating with other services. |

### Technology Stack

*   **Frameworks**: Spring Boot 3, Spring Cloud 2025.0.0
*   **Language**: Java 21
*   **Build Tool**: Apache Maven
*   **Resilience**: Resilience4j (Circuit Breaker, Retry, Rate Limiter)
*   **Database**: In-memory H2 Database for all services
*   **Containerization**: Docker & Docker Compose
*   **Authentication**: JWT (JSON Web Tokens)

## Getting Started

Follow these instructions to get the entire application running on your local machine.

### Prerequisites

*   **JDK 21**: Ensure you have Java 21 installed.
*   **Maven**: Required for building the projects.
*   **Docker & Docker Compose**: This is the recommended way to run the application. [Install Docker Desktop](https://www.docker.com/products/docker-desktop/).

### How to Run the Application

#### Method 1: Using Docker Compose (Recommended)

This is the simplest way to run the entire stack.

1.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd <your-project-directory>
    ```

2.  **Run Docker Compose:**
    From the root directory (where `docker-compose.yml` is located), run the following command:
    ```bash
    docker-compose up -d --build
    ```
    This command will build the Docker image for each service and start them in the correct order. The `-d` flag runs the containers in detached mode.

3.  **Check the status:**
    You can see the running containers with `docker ps`. Once all services are up, you can access the Eureka dashboard at `http://localhost:8761`. You should see `API-GATEWAY`, `USER`, `SAPIENT`, and `PREFERENCES` registered.

#### Method 2: Running Locally with Maven

If you prefer to run each service individually without Docker:

1.  **Start the Discovery Server:**
    ```bash
    cd Backend/discovery-server
    ./mvnw spring-boot:run
    ```

2.  **Start the Backend Services (in separate terminals):**
    ```bash
    # Terminal 2
    cd Backend/user
    ./mvnw spring-boot:run

    # Terminal 3
    cd Backend/sapient
    ./mvnw spring-boot:run

    # Terminal 4
    cd Backend/preferences
    ./mvnw spring-boot:run
    ```

3.  **Start the API Gateway:**
    ```bash
    # Terminal 5
    cd Backend/api-gateway
    ./mvnw spring-boot:run
    ```

## API Endpoints & Usage Workflow

All requests go through the API Gateway at `http://localhost:8030`.

#### 1. Sign Up a New User

```bash
curl -X POST http://localhost:8030/api/v1/user/auth/signup \
-H "Content-Type: application/json" \
-d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "password": "password123"
}'
```

#### 2. Log In to Get a JWT

```bash
curl -X POST http://localhost:8030/api/v1/user/auth/login \
-H "Content-Type: application/json" \
-d '{
    "email": "john.doe@example.com",
    "password": "password123"
}'
```
**Response:**
```json
{
    "jwtToken": "eyJhbGciOiJIUzI1NiJ9...",
    "email": "john.doe@example.com",
    "id": "1"
}
```
**Copy the `jwtToken` for the next steps.**

#### 3. Search for Dummy Users (Protected Endpoint)

```bash
export TOKEN="your-jwt-token-here"

curl -X GET http://localhost:8030/api/v1/sapient/dummy-users/search/john \
-H "Authorization: Bearer $TOKEN"
```

#### 4. "Like" a Dummy User (Protected Endpoint)

This endpoint is protected by the `AuthenticationFilter` in the API Gateway and utilizes Resilience4j patterns in the `preferences-service`.

```bash
curl -X POST http://localhost:8030/api/v1/preferences/core/like \
-H "Authorization: Bearer $TOKEN" \
-H "Content-Type: application/json" \
-d '{
    "dummyUserId": 5
}'
```
**Response:**
```
success
```

### Resilience4j in Action: Testing the Circuit Breaker

You can see the circuit breaker work by simulating a service failure.

1.  With the application running via Docker Compose, stop the `user-service`:
    ```bash
    docker-compose stop user-service
    ```
2.  Now, try to send the "Like" request from Step 4 again.
3.  **Observe the behavior:**
    *   The first few requests might be slow or fail with a 500 error as the `Retry` mechanism kicks in.
    *   After a few failures, the circuit breaker for the `user-service` will **open**.
    *   Subsequent requests will fail *instantly* with a custom error message like `"User service is currently unavailable. Please try again later."`, demonstrating that the fallback has been triggered and preventing the system from waiting on a dead service.
4.  Restart the service to close the circuit:
    ```bash
    docker-compose start user-service
    ```
