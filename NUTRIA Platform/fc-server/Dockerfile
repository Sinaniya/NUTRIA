FROM maven:3.3-jdk-8  as build
WORKDIR /workspace/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src
RUN mvn package -Dmaven.test.skip=true

FROM openjdk:8-jdk-alpine
WORKDIR /workspace/app
COPY --from=build /workspace/app/target/fc-server.jar .
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "fc-server.jar"]
