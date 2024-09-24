# Build stage
FROM maven:3.8.5-openjdk-17 AS build
# Set working directory
WORKDIR /app
COPY . .
#Install depndency and build the application
RUN mvn clean package -DskipTests
# Package stage
#Use minimal JRE image for the final image
FROM openjdk:17-jdk-slim
# Set working directory in the final container
WORKDIR /app
#Copy the jar file from the builder stage
COPY --from=build /app/target/medipro-0.0.1-SNAPSHOT.jar  medipro-auth.jar
#Expose port 8282 for the application
EXPOSE 8282
#Command to run the springboot application
ENTRYPOINT ["java", "-jar", "medipro-auth.jar"]