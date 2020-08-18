#FROM openjdk:8-jdk-alpine
FROM bitnami/spark:latest
USER root
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app1.jar

RUN mkdir -p /opt/bitnami/spark/data2
COPY ./schedule.csv /opt/bitnami/spark/data2/

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/opt/bitnami/spark/app1.jar"]
