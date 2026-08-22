# Step 1: Build the application using Maven
FROM maven:4.0.0-rc-4-eclipse-temurin-25-alpine AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Step 2: Create a lightweight image for the application
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/*.jar discovery-server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "discovery-server.jar"]