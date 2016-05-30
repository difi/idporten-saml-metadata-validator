FROM frolvlad/alpine-oraclejdk8:slim

ADD metadata-validator-1.0.0-SNAPSHOT.jar metadata-validator.jar

RUN sh -c 'touch /metadata-validator.jar'

EXPOSE 9000

ENTRYPOINT ["java", "-jar", "/metadata-validator.jardo"]