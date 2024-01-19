# Stage 1: Build stage
FROM openjdk:17 as builder

WORKDIR /app

COPY ./project .

RUN cd /app && ./mvnw clean package -DskipTests

FROM openjdk:17.0-oracle

WORKDIR /app

COPY --from=builder /app/target/*.jar .

CMD ["java", "-jar", "/app/wordReplacer-0.0.1-SNAPSHOT.jar"]
