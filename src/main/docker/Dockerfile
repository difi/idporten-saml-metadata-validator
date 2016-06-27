FROM alpine:3.3

RUN apk --update add openjdk8-jre
CMD ["/usr/bin/java", "-version"]

ARG APP_NAME

COPY $APP_NAME /app.war

RUN sh -c 'touch /app.war'

EXPOSE 10009

ENTRYPOINT ["java", "-jar", "/app.war"]