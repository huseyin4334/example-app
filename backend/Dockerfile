# Build stage
FROM maven:3.8.5-openjdk-17 AS build
COPY . /home/build
RUN mkdir /home/.m2
WORKDIR /home/.m2
USER root
RUN --mount=type=cache,target=/root/.m2 mvn -f /home/build/pom.xml clean package -DskipTests

# Runtime stage
FROM openjdk:17-jdk-slim AS runtime
WORKDIR /app
COPY --from=build /home/build/app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
