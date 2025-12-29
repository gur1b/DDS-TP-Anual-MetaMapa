FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/cargadorEstatica-1.0-SNAPSHOT.jar app.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]