FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY target/*.jar url-shortner.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "url-shortner.jar"]