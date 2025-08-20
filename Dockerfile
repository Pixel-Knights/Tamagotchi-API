FROM maven:3.9.6-eclipse-temurin-22 AS build
LABEL authors="Joel Campos"

COPY src /app/src
COPY pom.xml /app

WORKDIR /app
RUN mvn clean install

FROM eclipse-temurin:22-jre-alpine

COPY --from=build /app/target/tamagochi-0.0.1-SNAPSHOT.jar /app/app.jar

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]

