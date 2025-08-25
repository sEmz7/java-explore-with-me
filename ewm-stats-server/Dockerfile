FROM openjdk:21-jdk
COPY stats-server/target/*.jar /app/stats-server.jar
WORKDIR /app
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar stats-server.jar"]