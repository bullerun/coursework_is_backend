FROM openjdk:23-jdk-slim
WORKDIR /app
COPY build/libs/backend-0.0.1-SNAPSHOT-plain.jar /app/backend-0.0.1-SNAPSHOT-plain.jar
COPY .env /app/.env
ENTRYPOINT ["java", "-jar", "backend-0.0.1-SNAPSHOT-plain.jar"]
EXPOSE 11488