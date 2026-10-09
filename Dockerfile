# Estágio 1: Compilação do JAR
FROM gradle:8.5-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar -x test

# Estágio 2: Alvo para rodar a aplicação final (target)
FROM eclipse-temurin:21-jdk-alpine AS target
WORKDIR /app
COPY --from=build /app/build/libs/*.jar  /app/usuario.jar
EXPOSE 8080
CMD ["java", "-jar", "/app/usuario.jar"]

# Estágio 3: Alvo para rodar os testes (tester)
FROM gradle:8.5-jdk21 AS tester
WORKDIR /app
COPY . .
CMD ["./gradlew", "test", "--no-daemon"]
