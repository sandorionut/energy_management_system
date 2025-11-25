# Energy Management System

**Student:** Sandor Ionut Daniel  
**Group:** 30242  
**Assignment:** 2 (Distributed Systems)

## 1. Project Overview
This project is a distributed Energy Management System designed to monitor and manage smart energy metering devices. It follows a **Microservices Architecture**, utilizing **Spring Boot** for backend services, **React** for the frontend, **RabbitMQ** for asynchronous communication, and **PostgreSQL** for data persistence. The entire system is containerized using **Docker**.

### Key Features
*   **User Management**: Admin and Client roles, secure authentication (JWT).
*   **Device Management**: CRUD operations for smart devices, mapping devices to users.
*   **Monitoring**: Real-time ingestion of sensor data, hourly consumption calculation, and historical data visualization.
*   **Device Simulator**: A standalone Java application that simulates smart energy meters sending data via RabbitMQ.
*   **Chat System**: Real-time communication between Admins and Clients (WebSocket).
*   **Security**: Role-Based Access Control (RBAC) enforced via API Gateway and Service layers.

---

## 2. Architecture
The system consists of the following components:

### Microservices
1.  **User Service** (`:8082`): Manages user accounts (Admin/Client).
2.  **Device Service** (`:8083`): Manages smart devices and their association with users.
3.  **Monitoring Service** (`:8084`): Consumes sensor data from RabbitMQ, stores measurements, and provides consumption statistics.
4.  **Auth Service** (`:8081`): Handles login and JWT token generation.

### Infrastructure
*   **API Gateway (Traefik)**: Single entry point (`:80`) for all requests, handling routing and load balancing.
*   **Message Broker (RabbitMQ)**: Handles device measurements and inter-service synchronization.
*   **Databases**: Dedicated PostgreSQL instances for each microservice to ensure loose coupling.
*   **Frontend**: React application serving the User and Admin dashboards.

---

## 3. Getting Started

### Prerequisites
*   **Docker Desktop** (installed and running)
*   **Java 17+** (for running the simulator locally)
*   **Maven** (optional, for local builds)

### Installation & Running
The entire system is defined in `docker-compose.yml`. To start the application:

1.  Clone the repository.
2.  Open a terminal in the project root.
3.  Run the following command:
    ```powershell
    docker compose up -d --build
    ```
4.  Wait for all containers to start (approx. 1-2 minutes).

### Accessing the Application
*   **Frontend**: [http://localhost:3000](http://localhost:3000)
*   **Traefik Dashboard**: [http://localhost:8080](http://localhost:8080)
*   **RabbitMQ Management**: [http://localhost:15672](http://localhost:15672) (User: `devuser`, Pass: `devpass`)

---

## 4. Usage Guide

### Default Credentials
*   **Admin**: `admin@test.com` / `admin` (or create one via SQL if DB is empty)
*   **Client**: Register a new account via the "Sign Up" page.

### 1. Admin Workflow
1.  Log in as Admin.
2.  **Manage Users**: Create, update, or delete users.
3.  **Manage Devices**: Create devices and assign them to users.
4.  **View Consumption**: Go to "Manage Devices" and select any device to view its energy consumption chart.

### 2. Client Workflow
1.  Log in as a Client.
2.  **Dashboard**: View assigned devices.
3.  **Charts**: Select a device and date to view hourly energy consumption.

### 3. Running the Device Simulator
The simulator generates realistic sensor data for testing.

1.  Navigate to the `device-simulator` folder.
2.  Edit `config.properties` (optional):
    *   Set `device.id` to a valid Device ID from your database (copy from Admin UI).
3.  Run the simulator:
    *   **Windows**: Double-click `run-simulator.bat` or run `.\run-simulator.bat` in terminal.
    *   **IDE**: Run `SimulatorApp.java`.
4.  Observe the real-time updates on the Frontend Dashboard.

---

## 5. API Documentation
Swagger UI is available for each microservice via the API Gateway:

*   **User Service**: [http://localhost/user/swagger-ui/index.html](http://localhost/user/swagger-ui/index.html)
*   **Device Service**: [http://localhost/device/swagger-ui/index.html](http://localhost/device/swagger-ui/index.html)
*   **Monitoring Service**: [http://localhost/monitoring/swagger-ui/index.html](http://localhost/monitoring/swagger-ui/index.html)
*   **Auth Service**: [http://localhost/auth/swagger-ui/index.html](http://localhost/auth/swagger-ui/index.html)

---

## 6. Deployment Diagram
*(See `SD_Project_Diagram_2.png` in the repository for the visual representation)*

The deployment follows a containerized approach where the React Frontend communicates with the backend via the Traefik Reverse Proxy. Services communicate asynchronously via RabbitMQ for critical data flows (measurements, synchronization).

---

## 7. Troubleshooting
*   **Docker fails to start**: Ensure ports 80, 3000, 5432-5436, and 5672 are free.
*   **Simulator connection refused**: Ensure RabbitMQ is running (`docker ps`) and `config.properties` points to `localhost`.
*   **No data in charts**: Ensure the simulator is running and using the correct `device.id`.
