FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /workspace

COPY pom.xml .
RUN mvn -B dependency:go-offline

COPY src ./src
RUN mvn -B clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -g 10001 deployflow \
    && adduser -D -u 10001 -G deployflow deployflow

COPY --from=build /workspace/target/deployflow-api-*.jar app.jar

RUN chown -R 10001:10001 /app

USER 10001

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]