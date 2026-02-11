# WebServ Docker Architecture

This document explains how our multi-stage Docker build works, showing the complete flow from source files to running container.

## 🏗️ Multi-Stage Build Overview

Our Dockerfile uses a **2-stage build** to create an optimized production image:

```
┌─────────────────────────────────────────────────────────────────┐
│                    DOCKER BUILD FLOW                            │
│                                                                 │
│  Host Files  →  Build Stage  →  Runtime Stage  →  Container     │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## 📋 Stage 1: Build Stage

**Base Image:** `maven:3.8.4-openjdk-17`

```
┌─────────────────────────────────────────────────────────────────┐
│                      BUILD STAGE LAYERS                         │
│                                                                 │
│ ┌─────────────────────────────────────────────────────────────┐ │
│ │                YOUR JAVA APPLICATION                        │ │
│ │  • Compiled .class files                                    │ │
│ │  • Generated ANTLR parsers                                  │ │  
│ │  • Packaged JAR file (webserv.jar)                          │ │
│ └─────────────────────────────────────────────────────────────┘ │
│                             ↑                                   │
│ ┌─────────────────────────────────────────────────────────────┐ │
│ │                BUILD TOOLS LAYER                            │ │
│ │  • GNU Make                                                 │ │
│ │  • GCC Compiler                                             │ │
│ │  • Build utilities                                          │ │
│ └─────────────────────────────────────────────────────────────┘ │
│                             ↑                                   │
│ ┌─────────────────────────────────────────────────────────────┐ │
│ │            MAVEN + OpenJDK 17 LAYER                         │ │
│ │  • Apache Maven 3.8.4                                       │ │
│ │  • OpenJDK 17 (Full JDK)                                    │ │
│ │  • Maven dependencies cached                                │ │
│ └─────────────────────────────────────────────────────────────┘ │
│                             ↑                                   │
│ ┌─────────────────────────────────────────────────────────────┐ │
│ │                 BASE OS LAYER                               │ │
│ │  • Oracle Linux 8.5                                         │ │
│ │  • Package managers (yum/microdnf)                          │ │
│ │  • Basic Unix tools                                         │ │
│ └─────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

### Build Stage Commands Flow:

```
┌─────────────────────────────────────────────────────────────────┐
│                    BUILD COMMANDS FLOW                          │
│                                                                 │
│  1. WORKDIR /app                                                │
│     └─── Set working directory in container                     │
│                                                                 │
│  2. Install build tools                                         │
│     └─── microdnf update && install make, gcc, gcc-c++          │
│                                                                 │
│  3. Copy build files first (Docker layer caching)               │
│     ├─── COPY mvnw .                                            │
│     ├─── COPY .mvn ./.mvn                                       │
│     ├─── COPY pom.xml .                                         │
│     └─── COPY Makefile .                                        │
│                                                                 │
│  4. Make mvnw executable                                        │
│     └─── RUN chmod +x ./mvnw                                    │
│                                                                 │
│  5. Install system dependencies                                 │
│     └─── RUN make dep                                           │
│                                                                 │
│  6. Download Maven dependencies (cached if pom.xml unchanged)   │
│     └─── RUN ./mvnw dependency:go-offline                       │
│                                                                 │
│  7. Copy source code                                            │
│     └─── COPY src ./src                                         │
│                                                                 │
│  8. Build the application                                       │
│     └─── RUN make package                                       │
│         └─── Executes: ./mvnw clean package -DskipTests         │
│                                                                 │
│  RESULT: /app/target/webserv.jar (Fat JAR with all deps)        │
└─────────────────────────────────────────────────────────────────┘
```

## 🚀 Stage 2: Runtime Stage

**Base Image:** `eclipse-temurin:17-jre`

```
┌─────────────────────────────────────────────────────────────────┐
│                    RUNTIME STAGE LAYERS                         │
│                                                                 │
│ ┌─────────────────────────────────────────────────────────────┐ │
│ │                 YOUR APPLICATION                            │ │
│ │  • webserv.jar (copied from build stage)                    │ │
│ │  • test.conf (configuration file)                           │ │
│ │  • public/ (static web files)                               │ │
│ │  • Non-root user (appgroup:appuser)                         │ │
│ └─────────────────────────────────────────────────────────────┘ │
│                             ↑                                   │
│ ┌─────────────────────────────────────────────────────────────┐ │
│ │              JAVA RUNTIME LAYER                             │ │
│ │  • OpenJDK 17 JRE (Runtime only, smaller)                   │ │
│ │  • Eclipse Temurin distribution                             │ │
│ │  • JVM optimized for production                             │ │
│ └─────────────────────────────────────────────────────────────┘ │
│                             ↑                                   │
│ ┌─────────────────────────────────────────────────────────────┐ │
│ │                 BASE OS LAYER                               │ │
│ │  • Ubuntu 22.04 LTS (smaller than Oracle Linux)             │ │
│ │  • Essential libraries only                                 │ │
│ │  • Security patches                                         │ │
│ └─────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

### Runtime Stage Commands Flow:

```
┌─────────────────────────────────────────────────────────────────┐
│                   RUNTIME COMMANDS FLOW                         │
│                                                                 │
│  1. WORKDIR /app                                                │
│     └─── Set working directory                                  │
│                                                                 │
│  2. Copy application from build stage                           │
│     └─── COPY --from=build /app/target/webserv.jar app.jar      │
│                                                                 │
│  3. Copy configuration and static files                         │
│     ├─── COPY test.conf .                                       │
│     └─── COPY public ./public                                   │
│                                                                 │
│  4. Create non-root user for security                           │
│     ├─── RUN addgroup --system appgroup                         │
│     ├─── RUN adduser --system --ingroup appgroup appuser        │
│     └─── USER appuser                                           │
│                                                                 │
│  5. Expose port for networking                                  │
│     └─── EXPOSE 8080                                            │
│                                                                 │
│  6. Define how to start the application                         │
│     └─── ENTRYPOINT ["java", "-jar", "app.jar"]                 │
│                                                                 │
│  RESULT: Optimized container ready to run                       │
└─────────────────────────────────────────────────────────────────┘
```

## 📁 File Flow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                      FILE FLOW THROUGH DOCKER                   │
│                                                                 │
│  HOST FILESYSTEM                                                │
│  ├── src/main/java/         ────────┐                           │
│  │   └── com/example/app/           │                           │
│  │       ├── App.java               │                           │
│  │       ├── Server.java            │                           │
│  │       └── ServerConfig.java      │                           │
│  ├── src/main/antlr4/       ────────┤                           │
│  │   └── Nginx.g4                   │                           │
│  ├── pom.xml                ────────┤                           │
│  ├── Makefile               ────────┤                           │
│  ├── mvnw                   ────────┤                           │
│  ├── .mvn/                  ────────┤                           │
│  ├── test.conf              ────────┤                           │
│  └── public/                ────────┤                           │
│      └── index.html                 │                           │
│                                     │                           │
│                                     ▼                           │
│  BUILD STAGE CONTAINER                                          │
│  /app/                                                          │
│  ├── src/           (copied) ───────┐                           │
│  ├── pom.xml        (copied)        │                           │
│  ├── Makefile       (copied)        │                           │
│  ├── mvnw           (copied)        │                           │
│  ├── .mvn/          (copied)        │                           │
│  └── target/        (generated)     │                           │
│      └── webserv.jar                │                           │
│                                     │                           │
│                                     ▼                           │
│  RUNTIME STAGE CONTAINER                                        │
│  /app/                                                          │
│  ├── app.jar        (from build stage)                          │
│  ├── test.conf      (copied from host)                          │
│  └── public/        (copied from host)                          │
│      └── index.html                                             │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

## 🔍 Layer Benefits

### Why Multi-Stage Build?

**Build Stage (Large ~800MB):**
- Contains full JDK, Maven, build tools
- Compiles your Java code
- Generates ANTLR parsers
- Creates fat JAR with dependencies
- **Discarded after build**

**Runtime Stage (Small ~200MB):**
- Contains only JRE (no compiler)
- Your application JAR
- Configuration files
- **This becomes your final image**

### Docker Layer Caching Optimization

```
┌─────────────────────────────────────────────────────────────────┐
│                    CACHING STRATEGY                             │
│                                                                 │
│  Layer 1: Base image           (Rarely changes)                 │
│  Layer 2: System packages      (Rarely changes)                 │
│  Layer 3: Build files          (Changes when pom.xml changes)   │
│  Layer 4: Maven dependencies   (Cached if pom.xml unchanged)    │
│  Layer 5: Source code          (Changes frequently)             │
│  Layer 6: Compiled application (Rebuilt when code changes)      │
│                                                                 │
│  🚀 Fast rebuilds: Only Layer 5-6 rebuild when you change code  │
└─────────────────────────────────────────────────────────────────┘
```

## 🚢 Usage Commands

```bash
# Build the image
docker build -t webserv .

# Run the container with correct ports
docker run -p 8181:8181 -p 8282:8282 webserv test.conf

# Or use docker-compose (recommended)
docker-compose up --build
```

## 🌐 Docker Networking & Port Configuration

### Port Configuration

Your WebServ application runs on **ports 8181 and 8282**, so Docker needs proper port mapping:

```
┌─────────────────────────────────────────────────────────────────┐
│                    PORT MAPPING FLOW                            │
│                                                                 │
│  Host Machine    │    Docker Container    │    Java Application │
│                  │                        │                     │
│  localhost:8181 ─┼─→ container:8181 ─────┼─→ ServerSocket:8181  │
│  localhost:8282 ─┼─→ container:8282 ─────┼─→ ServerSocket:8282  │
│                  │                        │                     │
└─────────────────────────────────────────────────────────────────┘
```

### Critical Networking Fix

**Problem**: Java ServerSocket binding to `localhost` only accepts internal connections.

**Solution**: Bind to `0.0.0.0` to accept external connections:

```java
// ❌ Wrong - only accepts internal connections
InetSocketAddress bindPoint = new InetSocketAddress("localhost", port);

// ✅ Correct - accepts external connections  
InetSocketAddress bindPoint = new InetSocketAddress("0.0.0.0", port);
```

### Docker Compose Configuration

Create `docker-compose.yml` for easy deployment:

```yaml
version: '3.8'

services:
  webserv:
    build: .
    ports:
      - "8181:8181"    # Map host port 8181 to container port 8181
      - "8282:8282"    # Map host port 8282 to container port 8282
    volumes:
      - ./test.conf:/app/test.conf:ro      # Mount config file (read-only)
      - ./public:/app/public:ro            # Mount static files (read-only)
    environment:
      - JAVA_OPTS=-Xmx256m                # Limit JVM memory usage
    restart: unless-stopped                # Auto-restart on failure
    healthcheck:
      test: ["CMD-SHELL", "curl -f http://localhost:8181/ || curl -f http://localhost:8282/ || exit 1"]
      interval: 30s
      timeout: 10s
      retries: 3
```

### Network Troubleshooting

#### Test Container Connectivity:

```bash
# Check if container is running and ports are mapped
docker ps

# Check port binding on host
netstat -tlnp | grep -E "818[12]"
# or
ss -tlnp | grep -E "818[12]"

# Test connectivity to both ports
curl http://localhost:8181/
curl http://localhost:8282/

# Test with verbose output
curl -v http://localhost:8181/
```

#### Debug Container Networking:

```bash
# Check container logs
docker logs <container-name>

# Check what's listening inside container
docker exec -it <container-name> netstat -tlnp

# Test internal connectivity
docker exec -it <container-name> curl http://localhost:8181/

# Check container IP and network
docker inspect <container-name> | grep -A 20 NetworkSettings
```

#### Common Issues & Solutions:

| Issue | Symptom | Solution |
|-------|---------|----------|
| **Wrong port mapping** | Connection refused | Use `-p 8181:8181 -p 8282:8282` |
| **Java binds to localhost** | Container unreachable | Change Java code to bind to `0.0.0.0` |
| **Firewall blocking** | Timeout errors | Check `iptables` or disable firewall |
| **Config file missing** | App crashes on start | Ensure `COPY test.conf .` in Dockerfile |

### Docker Compose Commands

```bash
# Build and start services
docker-compose up --build

# Run in background (detached)
docker-compose up -d --build

# View live logs
docker-compose logs -f webserv

# Stop all services
docker-compose down

# Restart services
docker-compose restart

# Force rebuild
docker-compose build --no-cache
docker-compose up -d
```

### Testing Your WebServ

Once running, test both endpoints:

```bash
# Test both server ports
curl http://localhost:8181/
curl http://localhost:8282/

# Test with different paths
curl http://localhost:8181/index.html
curl http://localhost:8282/api/status

# Load testing (if apache2-utils installed)
ab -n 100 -c 10 http://localhost:8181/

# Monitor server responses
watch -n 1 'curl -s -o /dev/null -w "%{http_code} %{time_total}s\n" http://localhost:8181/'
```

## 🔧 Key Technologies

- **Base OS**: Oracle Linux 8.5 (build) + Ubuntu 22.04 (runtime)
- **Java**: OpenJDK 17
- **Build Tool**: Apache Maven 3.8.4
- **Parser**: ANTLR4 for nginx config parsing
- **Security**: Non-root user execution
- **Networking**: Multi-port binding (8181, 8282)
- **Orchestration**: Docker Compose for easy deployment

This architecture provides a secure, optimized, and maintainable containerization of your Java web server with proper networking configuration.