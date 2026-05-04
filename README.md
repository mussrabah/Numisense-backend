# Numisense Backend

**Numisense-backend** is a high-performance Spring Boot and Kotlin-based backend system designed for precision agriculture. It integrates IoT telemetry, PostGIS geospatial analysis, Edge-to-Cloud AI diagnostics, and a Spatial Marketplace to empower farmers with data-driven insights.

---

## 🚀 Key Features

### Agronomy & AI Diagnostics
- Edge AI synchronization for crop disease detection.
- LLM-based fallback using Google Gemini for identifying unknown diseases.
- Automated crop rotation suggestions based on historical yield and suitability rules.

### IoT & Automation
- High-throughput telemetry ingestion via Apache Kafka.
- Real-time rule engine that triggers actuators (e.g. water pumps) based on moisture thresholds.
- Real-time notifications using WebSockets (STOMP).

### Geospatial Intelligence
- Utilizes PostGIS for farm zone mapping and spatial queries.
- Nearby marketplace listings search using geographical radius calculations.

### Marketplace
- Peer-to-peer commerce for agricultural goods with location-aware discovery.
- Bidding system with real-time alerts.

### Infrastructure
- MinIO for local S3-compatible image storage (diagnostic uploads).
- Flyway for database schema versioning.
- JWT Security for stateless authentication.

---

## 🛠️ Tech Stack

- **Language:** Kotlin (JVM 21)
- **Framework:** Spring Boot 4.0.6 (Spring Boot 3.x compatible)
- **Database:** PostgreSQL 16 + PostGIS
- **Messaging:** Apache Kafka (KRaft mode) & WebSockets
- **Storage:** MinIO (S3 Compatible)
- **Build Tool:** Gradle

---

## 🏗️ Getting Started

### Prerequisites
- Java 21 installed
- Docker & Docker Compose (for infrastructure services)

---

### 1. Launch Infrastructure

The project includes a `docker-compose.yml` to spin up the database, Kafka, and MinIO.

```bash
docker-compose up -d
```

- Postgres: localhost:5432
- Kafka: localhost:9092
- MinIO: localhost:9000 (API), localhost:9001 (Console)

---

### 2. Configure Environment

Update `src/main/resources/application.properties` if your local settings differ from the defaults.

To use AI features, provide a Gemini API key:

```properties
gemini.api.key=YOUR_ACTUAL_API_KEY
```

---

### 3. Run the Application

#### On Linux / macOS

```bash
chmod +x gradlew
./gradlew bootRun
```

#### On Windows

```bash
gradlew.bat bootRun
```

---

## 🔌 API Endpoints (Brief Overview)

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/auth/register` | POST | Register a new farmer |
| `/api/v1/dashboard/summary/{zoneId}` | GET | Get moisture, tasks, and weather (BFF pattern) |
| `/api/v1/ai/diagnostics/sync` | POST | Sync disease report from mobile to cloud |
| `/api/v1/marketplace/nearby` | GET | Find items within radius using PostGIS |
| `/ws-numiterra` | WS | STOMP WebSocket handshake endpoint |

---

## 🧪 Testing

The project uses **Testcontainers** to run integration tests against real instances of PostgreSQL and Kafka.

```bash