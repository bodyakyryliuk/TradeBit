FROM openjdk:19-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ./target/email-0.0.1-SNAPSHOT.jar app.jar
ENV SPRING_MAIL_USERNAME="tradebitapplication@gmail.com" \
    SPRING_MAIL_PASSWORD="fnhnkacqrxmupygm" \
    SPRING_RABBITMQ_ADDRESSES="rabbitmq:5672" \
    SPRING_RABBITMQ_HOST="rabbitmq"
ENTRYPOINT ["java","-jar","/app.jar"]


