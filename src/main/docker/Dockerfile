FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp

ADD metadata-validator-1.0.0-SNAPSHOT.jar metadata-validator.jar

RUN sh -c 'touch /app.jar'

ENTRYPOINT ["java", "-jar", "/metadata-validator.jar"]