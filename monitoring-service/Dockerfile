FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build

COPY pom.xml .
COPY monitoring-service/pom.xml monitoring-service/pom.xml

COPY monitoring-service/src monitoring-service/src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -DskipTests -f monitoring-service/pom.xml package

FROM eclipse-temurin:25-jre
WORKDIR /app

COPY --from=build /build/monitoring-service/target/*.jar /app/app.jar

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=docker

CMD ["java", "-jar", "/app/app.jar"]
