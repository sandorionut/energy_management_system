FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build
COPY pom.xml .
COPY device-service/pom.xml device-service/pom.xml
COPY device-service/src device-service/src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -DskipTests -f device-service/pom.xml package

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /build/device-service/target/*.jar /app/app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=docker
CMD ["java","-jar","/app/app.jar"]
