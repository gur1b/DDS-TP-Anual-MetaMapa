FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiamos el jar generado por Spring Boot
COPY target/metamapa-web-1.0-SNAPSHOT.jar app.jar

# La app Spring Boot escucha en este puerto
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]