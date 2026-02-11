#our docker file based image is maven with openjdk 17
# we use maven to run our webserv project and openjdk 17 to run our java code




# ┌─────────────────────────────────────────────────────────────────────┐
# │                    DOCKER BASE IMAGE WORKFLOW                       │
# │                                                                     │
# │  FROM maven:3.8.4-openjdk-17 AS build                               │
# └─────────────────────────────────────────────────────────────────────┘

# ┌─────────────────────────────────────────────────────────────────────┐
# │                         LAYER STACK                                 │
# │                                                                     │
# │ ┌─────────────────────────────────────────────────────────────────┐ │
# │ │              maven:3.8.4-openjdk-17 (BUILD STAGE)               │ │
# │ │  ┌─────────────────────────────────────────────────────────────┐│ │
# │ │  │ • Maven 3.8.4 Build Tool                                    ││ │
# │ │  │ • mvn command available                                     ││ │
# │ │  │ • Maven plugins & dependencies management                   ││ │
# │ │  └─────────────────────────────────────────────────────────────┘│ │
# │ │                               ↑                                 │ │
# │ │  ┌─────────────────────────────────────────────────────────────┐│ │
# │ │  │ • OpenJDK 17 (Java Development Kit)                         ││ │
# │ │  │ • java, javac commands                                      ││ │
# │ │  │ • JVM Runtime Environment                                   ││ │
# │ │  └─────────────────────────────────────────────────────────────┘│ │
# │ │                               ↑                                 │ │
# │ │  ┌─────────────────────────────────────────────────────────────┐│ │
# │ │  │ • Debian Linux (bullseye-slim)                              ││ │
# │ │  │ • Basic Unix commands (ls, cd, mkdir, etc.)                 ││ │
# │ │  │ • Package manager (apt)                                     ││ │
# │ │  │ • File system (/usr, /var, /home, etc.)                     ││ │
# │ │  └─────────────────────────────────────────────────────────────┘│ │
# │ └─────────────────────────────────────────────────────────────────┘ │
# └─────────────────────────────────────────────────────────────────────┘

# ┌─────────────────────────────────────────────────────────────────────┐
# │                      WHAT HAPPENS WHEN YOU RUN                      │
# │                                                                     │
# │  Docker pulls image → Creates container → You get:                  │
# │                                                                     │
# │  ✅ Complete Linux environment (Debian)                             │
# │  ✅ Java 17 ready to compile & run                                  │
# │  ✅ Maven ready to build your project                               │
# │  ✅ All tools needed for your webserv build                         │
# │                                                                     │
# │  Ready for: mvn clean compile, mvn package, etc.                    │
# └─────────────────────────────────────────────────────────────────────┘
FROM maven:3.8.4-openjdk-17 AS build

WORKDIR /app

# Install GNU Make and other build tools (use microdnf for Oracle Linux)
RUN microdnf update -y && microdnf install -y \
    make \
    gcc \
    gcc-c++ \
    && microdnf clean all

# Copy Maven Wrapper and build files
COPY mvnw .
COPY .mvn ./.mvn
COPY pom.xml .
COPY Makefile .
COPY test.conf .

# Make mvnw executable
RUN chmod +x ./mvnw

# Install dependencies (now make command is available)
RUN make dep

# Download Maven dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the project
RUN make package

# Runtime stage
FROM eclipse-temurin:17-jre

WORKDIR /app
COPY --from=build /app/target/webserv.jar app.jar

# Copy config file and public directory
COPY test.conf .
COPY public ./public

# Create non-root user
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser
USER appuser

EXPOSE 8181 8282

ENTRYPOINT ["java", "-jar", "app.jar", "test.conf"]