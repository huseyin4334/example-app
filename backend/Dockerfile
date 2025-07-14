# Build stage
FROM maven:3.8.5-openjdk-17 AS build
COPY . /home/build
WORKDIR /home/build
USER root
RUN --mount=type=cache,target=/root/.m2 mvn clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim AS runtime
WORKDIR /app
COPY --from=build /home/build/route-planer/rp-container/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
