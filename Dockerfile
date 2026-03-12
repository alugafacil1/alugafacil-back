# Estágio de Build
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copia apenas o pom primeiro (cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o código
COPY src ./src

# Build
RUN mvn clean package -DskipTests

# ===============================
# Stage 2 - Runtime
# ===============================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Ajuste o nome do jar conforme o seu projeto
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${PORT}"]