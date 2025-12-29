FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/cargadorProxy-1.0-SNAPSHOT.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]