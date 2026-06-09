FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
COPY docker/HealthCheck.java /tmp/HealthCheck.java
RUN mvn -B clean package -DskipTests
RUN javac -d /healthcheck /tmp/HealthCheck.java


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup --gid 10001 --system deployflow \
    && adduser --uid 10001 --system --disabled-password --no-create-home \
        --ingroup deployflow deployflow

COPY --from=build --chown=10001:10001 /workspace/target/deployflow-api-*.jar app.jar
COPY --from=build --chown=10001:10001 /healthcheck /app/healthcheck

USER 10001:10001

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
