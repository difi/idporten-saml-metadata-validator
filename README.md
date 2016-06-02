# First time install and run
Build project with
`mvn clean install`

Application can be started with
`java -jar target/metadata-validator-1.0.0-SNAPSHOT.jar`

All spring boot parameters are available, use property server.port to override port the application is starting on. With default settings, you can call the application by pasting 
 `http://localhost:9000`
to the address line in your browser. This should print _biig *hello* world_

## Building and running with docker
Assuming you have installed docker...

Build docker image
`mvn package docker:build`

Starting docker image
`docker run --name meta -d -p 9000:9000 -t docker-registry.dmz.local/idporten-saml-metadata-validator`

Open a browser and write
`<ip-to-docker>:9000`

To find your docker machine's IP address, type `env | grep DOCKER_HOST`