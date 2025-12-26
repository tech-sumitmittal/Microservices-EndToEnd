# -------- Stage 1: Build the application (create target jar file) --------
FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn -q -e -DskipTests clean package

# -------- Stage 2: Run the application --------
FROM openjdk:21-ea-oracle

# Create folder for the app and agent
RUN mkdir -p /app/libs

# Copy JAR from builder stage
COPY --from=builder /target/cards-0.0.1-SNAPSHOT.jar /app/cards.jar

# Download OpenTelemetry Java agent directly from GitHub
RUN curl -L --fail -o /app/libs/opentelemetry-javaagent-2.22.0.jar \
    https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.22.0/opentelemetry-javaagent.jar

# Set working directory
WORKDIR /app

# Run the app with the agent
ENTRYPOINT ["java", "-javaagent:/app/libs/opentelemetry-javaagent-2.22.0.jar", "-jar", "cards.jar"]