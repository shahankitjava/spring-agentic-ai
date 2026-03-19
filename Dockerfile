# Use a lightweight OpenJDK image as the base
FROM eclipse-temurin:24-jdk

# Set the working directory inside the container
WORKDIR /app

# Expose the port your Spring Boot application listens on (e.g., 8080)
EXPOSE 8080

# Copy the Spring Boot JAR file into the container
# Replace 'your-application.jar' with the actual name of your built JAR file
COPY target/*.jar app.jar

# Define the command to run your Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]