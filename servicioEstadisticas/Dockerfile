FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/servicioEstadisticas-1.0-SNAPSHOT.jar app.jar

EXPOSE 8090

ENTRYPOINT ["java", "-jar", "app.jar"]