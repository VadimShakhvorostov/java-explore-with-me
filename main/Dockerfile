FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/*.jar ewm.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/ewm.jar"]
