FROM java:openjdk-8

ADD idporten-saml-metadata-validator-1.0.0-SNAPSHOT.jar metadata-validator.jar

RUN sh -c 'touch /metadata-validator.jar'

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/metadata-validator.jar"]