FROM alpine:3.3

ENV APP_NAME nothing_given

RUN apk --update add openjdk8-jre
CMD ["/usr/bin/java", "-version"]

COPY idporten-saml-metadata-validator-1.0.0-SNAPSHOT.jar $APP_NAME

RUN sh -c 'touch /$APP_NAME'

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/$APP_NAME"]