# Customer API

This project is a RESTful CRUD service based on Spring Boot, with support for Docker deployment, Kubernetes management, and automated CI/CD pipelines. The goal of the project is to provide a fast solution for backend service development and deployment, making it easier for developers to maintain services.

## Features

- **Documentation**: A detailed 7-step Word document is available in the `./doc/` directory, explaining each step in the process.
- **RESTful API**: Provides standard CRUD operations, supporting JSON data format.
- **Docker Support**: Containers the application using a Dockerfile, enabling it to run on any environment that supports Docker.
- **Kubernetes Support**: Provides Kubernetes configuration files (e.g., `./k8s/*.yaml`) to deploy the application on a Kubernetes cluster (e.g., minikube).
- **CI/CD Pipeline**: Integrates GitHub Actions for automated build, test, and deployment. Supports PR reviews, auto-building, and auto-deploying.

## Tech Stack

- **Backend**: Spring Boot 3
- **Containerization**: Docker
- **Orchestration**: Kubernetes (MiniKube)
- **CI/CD**: GitHub Actions
- **Database**: H2 SQL Database (in-memory)

## Testing Environment

- **Integration Test**: Windows, Linux or MacOS
- **Docker + MiniKube **: Linux (Ubuntu 18), please refer ./doc/Step2, Step4, Step5 and Step6

## Setup Instructions

### Step 1: Install Required Software

Download and install:
- [IntelliJ IDEA 2023](https://www.jetbrains.com/idea/download/other.html)
- [OpenJDK 17](https://www.openlogic.com/openjdk-downloads)

### Step 2: Build the Project
- Open the project root directory in IntelliJ IDEA.
- Set the project SDK to JDK 17.
- In the project root directory, open a terminal and input:mvn clean package -DskipTests , this will create ./target/user-app-0.0.1-SNAPSHOT.jar.
- To start the application:
    ```bash
    java -jar user-app-0.0.1-SNAPSHOT.jar

### Step 3: Swagger UI
- Use a browser (Chrome or Firefox) and try http://localhost:8080/swagger-ui/index.html
  to test the API endpoints.

### Step 4: Test API Endpoints
- For Integration tests, please open a terminal in project root directory and input: mvn verify
- Or if you are using Linux OS, please open a terminal in ./app_integration and input: chmod +x test.sh && ./test.sh
- Or use IDEA / terminal to launch the application and open a browser with http://localhost:8080/swagger-ui/index.html
- to test the following endpoints:

    - Customer Endpoints
        - POST /api/customer: Create a new customer (ID is ignored).
        - GET /api/customer/list: Get a list of customers with pagination support.
        - GET /api/customer/{id}: Get the customer by ID.
        - PUT /api/customer: Update the customer by a Customer JSON object (not all fields needed).
        - DELETE /api/customer: Delete the customer by ID

### Step 5: Testing Guide
- You may start by testing POST /api/customer. Use a JSON body format, e.g.,
  ```json
   {
      "firstName": "John",
      "lastName": "Doe",
      "emailAddress": "john345@test.com",
      "phoneNumber": "555-321-6781"
    }
- A successful creation response includes the customer id created by the backend, like:
  ```json
    {
      "id": "cc767113-cb58-4f12-8cf6-62a35604fb2f",
      "firstName": "John",
      "middleName": null,
      "lastName": "Doe",
      "emailAddress": "john345@test.com",
      "phoneNumber": "555-321-6781"
    }
- To test with Docker + MiniKube + Ubuntu, please refer ./doc/Step2, Step4, Step5 and Step6
