FROM alpine:3.3

RUN apk --update add openjdk8-jre

ARG APP_NAME

COPY $APP_NAME /app.war

RUN sh -c 'touch /app.war'

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/app.war"]