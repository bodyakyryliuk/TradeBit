FROM openjdk:19-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ../../binance-service/target/binance-service-0.0.1-SNAPSHOT.jar app.jar
ENV API_GATEWAY_HOST="api-gateway:8080"\
    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE="http://eureka-server:8761/eureka/"\
    KEYCLOAK_ISSUER_URI="http://keycloak:8080/auth/realms/tradebit-realm"\
    SPRING_DATASOURCE_PASSWORD="root"\
    SPRING_DATASOURCE_URL="jdbc:postgresql://postgres-custom:5432/binance_db"\
    SPRING_DATASOURCE_USERNAME="postgres"\
    APP_ENCRYPTION_SECRET_KEY="y68Th7Qg1LYju5hiXgFC21hSXcb93SrVfNlzAu7PvlFlsUCZ7O6kcuxXhzpZXEVj"

ENTRYPOINT ["java","-jar","/app.jar"]