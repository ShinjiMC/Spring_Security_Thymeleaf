FROM maven:3.8.4-openjdk-17 AS builder
COPY ./src /build/src
COPY ./pom.xml /build
WORKDIR /build
RUN mvn clean package
FROM openjdk:17-jdk-alpine
COPY --from=builder /build/target/*.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
