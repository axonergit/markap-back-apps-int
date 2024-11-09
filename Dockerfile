# Etapa de construcción
FROM maven:3.8.4-openjdk-17 AS build
WORKDIR /app
COPY ./MarkapBE .
RUN mvn clean package -DskipTests

# Etapa para ejecutar
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/MarkapBE-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
