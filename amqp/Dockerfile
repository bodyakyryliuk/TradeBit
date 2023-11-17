FROM openjdk:19-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ./target/amqp-0.0.1-SNAPSHOT-rabbit-common.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

#sha256:409baf56dc400731567f2218045f96aacb176228f660655d9a9e42c6e76a6056