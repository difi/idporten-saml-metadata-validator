FROM alpine:3.3

RUN apk --update add openjdk8-jre
CMD ["/usr/bin/java", "-version"]

COPY idporten-saml-metadata-validator-1.0.0-SNAPSHOT.jar metadata-validator.jar

RUN sh -c 'touch /metadata-validator.jar'

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/metadata-validator.jar"]