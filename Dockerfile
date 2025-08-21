# Multi-stage build for Spring Boot application
FROM openjdk:17-jdk-slim as builder

# Set working directory
WORKDIR /app

# Copy gradle files
COPY gradlew .
COPY gradle/wrapper/ gradle/wrapper/
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src/ src/

# Build application
RUN ./gradlew build -x test --no-daemon

# Runtime stage
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy built jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]
