FROM amazoncorretto:22.0.1-al2023-headless
WORKDIR .
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/url-shortener.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/url-shortener.jar"]
