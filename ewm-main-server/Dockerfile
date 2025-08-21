FROM openjdk:21-jdk
COPY ./target/*.jar /app/ewm-service.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "ewm-service.jar"]