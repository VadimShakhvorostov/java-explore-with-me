FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/*.jar stat-server.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "/app/stat-server.jar"]
