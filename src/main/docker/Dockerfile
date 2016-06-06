FROM alpine:3.3

RUN apk --update add openjdk8-jre
CMD ["/usr/bin/java", "-version"]

ARG APP_NAME

COPY $APP_NAME /app.jar

RUN sh -c 'touch /app.jar'

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/app.jar"]