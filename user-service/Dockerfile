FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build

COPY pom.xml .
COPY user-service/pom.xml user-service/pom.xml

COPY user-service/src user-service/src

RUN --mount=type=cache,target=/root/.m2 \
    mvn -B -DskipTests -f user-service/pom.xml package

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /build/user-service/target/*.jar /app/app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=docker
CMD ["java","-jar","/app/app.jar"]
