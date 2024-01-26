FROM openjdk:19-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ../../bot-service/target/bot-service-0.0.1-SNAPSHOT.jar app.jar
ENV API_GATEWAY_HOST="http://api-gateway:8080"\
    APP_ENCRYPTION_SECRET_KEY="y68Th7Qg1LYju5hiXgFC21hSXcb93SrVfNlzAu7PvlFlsUCZ7O6kcuxXhzpZXEVj "\
    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE="http://eureka-server:8761/eureka/" \
    KEYCLOAK_AUTH_SERVER_URL="http://keycloak:8080/auth" \
    KEYCLOAK_CLIENT_ID="bot-client" \
    KEYCLOAK_CLIENT_SECRET="rhr1gFAeQUNmBF32QHsSnf4oYl8B9wDR"\
    KEYCLOAK_ISSUER_URI="http://keycloak:8080/auth/realms/tradebit-realm"\
    KEYCLOAK_REALM="tradebit-realm" \
    SPRING_DATASOURCE_PASSWORD="root" \
    SPRING_DATASOURCE_URL="jdbc:postgresql://postgres-custom:5432/trading_bot_db" \
    SPRING_DATASOURCE_USERNAME="postgres"\
    SPRING_RABBITMQ_ADDRESSES="rabbitmq:5672"


ENTRYPOINT ["java","-jar","app.jar"]