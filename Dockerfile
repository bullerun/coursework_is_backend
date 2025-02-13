FROM openjdk:23-jdk-slim
WORKDIR /app
COPY build/libs/*.jar /app/app.jar
COPY .env /app/.env
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 11488