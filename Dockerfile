# Estágio de Build
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# 1. Copia APENAS o pom.xml primeiro
COPY pom.xml .

# 2. Baixa as dependências (Isso ficará em cache se o pom.xml não mudar)
RUN mvn dependency:go-offline

# 3. Copia o código-fonte (src)
COPY src ./src

# 4. Compila o projeto (agora é rápido, pois as libs já estão lá)
RUN mvn clean package -DskipTests

# Estágio Final (igual ao anterior)
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]