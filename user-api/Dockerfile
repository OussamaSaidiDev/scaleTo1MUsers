FROM maven:3.9-eclipse-temurin-8 AS build

COPY target/user-api-1.1.0.jar /
EXPOSE 8080

CMD ["java", "-jar", "user-api-1.1.0.jar"]