FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/Table-Football-Scorekeeper-API-0.0.1-SNAPSHOT.jar app.jar

ENV DB_PATH=/app/table_football_db.db

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]