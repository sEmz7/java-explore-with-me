FROM openjdk:21-jdk
COPY ./target/*.jar /app/stats-server.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "stats-server.jar"]