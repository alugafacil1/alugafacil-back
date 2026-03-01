# ===== BUILD =====
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# ===== RUNTIME =====
FROM eclipse-temurin:21-jre

WORKDIR /app

ARG JAR_FILE=target/alugafacil-backend-0.0.1-SNAPSHOT.jar
COPY --from=build /app/${JAR_FILE} app.jar

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]