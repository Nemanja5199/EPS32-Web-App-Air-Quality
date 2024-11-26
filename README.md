# **Air Quality Monitoring System**

A real-time air quality monitoring system that collects temperature and humidity data using an ESP32 microcontroller, processes the data via a Spring Boot backend, and displays it dynamically on a web interface.

---

## **Architecture Overview**

This system leverages an ESP32 microcontroller equipped with a DHT sensor to gather environmental data such as temperature and humidity. The collected data is sent to a REST API built with Spring Boot and Kotlin.

The backend performs the following functions:
- **Stores data** in a PostgreSQL database.
- **Broadcasts data** in real-time to connected clients using WebSocket.

The frontend web interface receives and displays real-time updates, providing a seamless view of the current temperature and humidity.

<img src="https://github.com/user-attachments/assets/a1843776-91bd-4dba-8b4e-73745a4e05f7" alt="Architecture Diagram" width="300"/>

---

## **Getting Started**

### **1. Clone the Repository**
Start by cloning the repository to your local machine:
```bash
git clone <repository-url>
cd <repository-name>
```
### **2. Configure Environment Variables**

```bash
SERVER_PORT=8080
DB_NAME=airquality
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_URL=jdbc:postgresql://db:5432/airquality
HIBERNATE_DDL_AUTO=update
```

## **Running the Application**

The application is containerized using Docker. To start all services:

```bash
docker-compose up --build
```

## **ESP32 Setup**

- **Flash your ESP32 microcontroller** with the provided firmware in the repository.
- **Update the firmware** with your WiFi credentials and the API endpoint URL for the backend.


When powered on, the ESP32 will automatically begin collecting data and transmitting it to the backend.


## **API Documentation**
The REST API provides several endpoints for sensor data management

### **Endpoints** ###

**POST /api/sensor**
Submit new sensor readings to the backend.
Request Body Example:
```bash
{
  "temperature": 25.5,
  "humidity": 60.2
}
```
**GET /api/sensor**
Retrieve all recorded sensor readings.
```bash
{
    "temperature": 24.6,
    "humidity": 41.0
}
```

## **Fronted Overlook**

The web page can be accesed with the link:
```bash
http://localhost:8080/
```

![Sensor website](https://github.com/user-attachments/assets/c934c230-2034-4f3c-843c-161e3267491b)






