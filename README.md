# First time install and run
Build project with
`mvn clean install`

Application can be started with
`java -jar target/metadata-validator-1.0.0-SNAPSHOT.jar`

All spring boot parameters are available, use property server.port to override port the application is starting on. With default settings, you can call the application by pasting 
 `http://localhost:10009`
to the address line in your browser. This should print _biig *hello* world_

## Building and running with docker
Assuming you have installed docker...

Build docker image
`mvn package docker:build`

Starting docker image
`mvn docker:run`

Open a browser and write
`<ip-to-docker>:10009`

To find your docker machine's IP address, type `env | grep DOCKER_HOST`

Stopping docker image
`mvn docker:stop`

Removing docker image
`mvn docker:remove`
NB! This does not remove the image from disk. To see images, write `docker images`. These can be removed with `docker rmi <id of image>`.