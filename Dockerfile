# Multi-stage build for Spring Boot with JDK 17 (LTS)
FROM eclipse-temurin:17-jdk-alpine as builder

# Set working directory
WORKDIR /app

# Copy gradle files
COPY gradle gradle
COPY gradlew .
COPY gradlew.bat .
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

# Copy source code
COPY src src

# Build the application
RUN ./gradlew build -x test

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

# Install curl for health checks (Alpine uses apk)
RUN apk add --no-cache curl

# Create app user (Alpine syntax)
RUN addgroup -g 1001 -S appuser && adduser -S appuser -G appuser

# Set working directory
WORKDIR /app

# Copy built jar from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Copy Firebase service account (will be mounted as volume)
RUN mkdir -p /app/config

# Change ownership
RUN chown -R appuser:appuser /app

# Switch to non-root user
USER appuser

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/packages || exit 1

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
