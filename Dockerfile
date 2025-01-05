# Use an official Maven image to build the project
FROM maven:3.9.9-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven configuration file and dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code to the container
COPY src ./src

# Build the project
RUN mvn clean package

# Use a lightweight Java runtime to run the application
FROM eclipse-temurin:21-jre

# Set the working directory for the runtime container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/CambridgeForUkraineBot-1.0-SNAPSHOT.jar /app/bot.jar

# Expose the port if your bot listens for webhooks (optional)
EXPOSE 8080

# Run the bot
CMD ["java", "-jar", "/app/bot.jar"]
