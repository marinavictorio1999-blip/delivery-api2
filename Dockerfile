# Etapa de Build
FROM maven:3.9.10-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests
 
# Etapa de Execução
FROM eclipse-temurin:21
WORKDIR /app
COPY --from=build /app/target/*.jar ./delivery-api.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "delivery-api.jar"]