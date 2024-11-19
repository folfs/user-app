# Use an OpenJDK 17 base image
FROM openjdk:17-jdk-slim AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY target/user-app-0.0.1-SNAPSHOT.jar /app/user-app-0.0.1-SNAPSHOT.jar

# Expose the REST API port
EXPOSE 8080

# Define the command to run the user-app JAR file
ENTRYPOINT ["java", "-jar", "/app/user-app-0.0.1-SNAPSHOT.jar"]
 
