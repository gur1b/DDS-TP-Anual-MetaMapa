FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copiamos el jar del core (nombre exacto)
COPY target/core-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar

# El core expone dos puertos: 8081 (API) y 8082 (API admin)
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]