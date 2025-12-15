# ---------- Build stage ----------
FROM maven:3.9.0-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean install -DskipTests

# ---------- Runtime stage ----------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# install netcat for MySQL wait check
RUN apt-get update && apt-get install -y netcat-openbsd && rm -rf /var/lib/apt/lists/*

COPY --from=build /app/target/thedripcostoreproject-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 4646

# wait for mysql:3306 before starting the app
ENTRYPOINT ["java","-jar","app.jar","--server.address=0.0.0.0","--server.port=4646"]