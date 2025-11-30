# -------- Stage 1: Build the application (create target jar file) --------

FROM maven:3.9.6-eclipse-temurin-21 AS builder

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn -q -e -DskipTests clean package




# -------- Stage 2: Run the application --------

#Start with a base image containing Java runtime
FROM openjdk:21-ea-oracle

# Copy JAR from builder stage
COPY --from=builder /target/configserver-0.0.1-SNAPSHOT.jar configserver-0.0.1-SNAPSHOT.jar

# execute the application
ENTRYPOINT ["java", "-jar", "configserver-0.0.1-SNAPSHOT.jar"]