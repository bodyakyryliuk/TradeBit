FROM openjdk:19-jdk-alpine
VOLUME /tmp
ARG JAR_FILE=target/*.jar
COPY ../../identity-service/target/identity-service-0.0.1-SNAPSHOT.jar app.jar
ENV API_GATEWAY_HOST="api-gateway:8080"\
    EUREKA_CLIENT_SERVICEURL_DEFAULTZONE="http://eureka-server:8761/eureka/" \
    KEYCLOAK_AUTH_SERVER_URL="http://keycloak:8080/auth" \
    KEYCLOAK_HOST="keycloak:8080" \
    KEYCLOAK_REALM="tradebit-realm" \
    KEYCLOAK_RESOURCE="tradebit" \
    SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER_URI="http://keycloak:8080/auth/realms/tradebit-realm" \
    SPRING_DATASOURCE_PASSWORD="root" \
    SPRING_DATASOURCE_URL="jdbc:postgresql://postgres-custom:5432/users" \
    SPRING_DATASOURCE_USERNAME="postgres" \
    SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_ID="546788920756-s93if7kirtuhr4bvtuel8strhgk739t9.apps.googleusercontent.com" \
    SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_GOOGLE_CLIENT_SECRET="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" \
    SPRING_RABBITMQ_HOST="rabbitmq"

ENTRYPOINT ["java","-jar","/app.jar"]