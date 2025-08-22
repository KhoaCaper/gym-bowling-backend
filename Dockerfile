# Use Java 17
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy gradle files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

# Download dependencies
RUN ./gradlew dependencies --no-daemon

# Copy source code
COPY src src

# Build the application
RUN ./gradlew build -x test --no-daemon

# Expose port
EXPOSE 8080

# Set default profile to render
ENV SPRING_PROFILES_ACTIVE=render

# Run the application
CMD ["java", "-jar", "build/libs/gym-bowling-backend-0.0.1-SNAPSHOT.jar"]
