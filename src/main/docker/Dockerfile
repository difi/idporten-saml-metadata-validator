FROM java:openjdk-8-jdk-alpine

ARG APP_NAME

COPY $APP_NAME /app.war

RUN sh -c 'touch /app.war'

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/app.war"]