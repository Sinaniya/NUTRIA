FROM openjdk:8-jdk-alpine

MAINTAINER Danijel Dordevic "danijel.fon@gmail.com"

EXPOSE 8080

WORKDIR /usr/local/bin/

COPY maven/fc-server.jar fc-server.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=local", "-jar", "fc-server.jar"]