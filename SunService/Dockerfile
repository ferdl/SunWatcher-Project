# Stufe 1: Build (Java Code kompilieren)
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY pom.xml .
# Abhängigkeiten vorab laden (beschleunigt spätere Builds)
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Stufe 2: Runtime (Das eigentliche Image)
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
# Kopiere die JAR aus der Build-Stufe
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]