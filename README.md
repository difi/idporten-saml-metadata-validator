# First time install and run
First of all, git, java and maven must be installed on your machine. When you have made sure of this, you can clone the project and run
`mvn clean install`

Application can be started with
`java -jar target/metadata-validator-1.0.0-SNAPSHOT.jar`

All spring boot parameters are available, use property server.port to override port the application is starting on. With default settings, you can call the application by pasting 
 `http://localhost:9000`
to the address line in your browser. This should print _biig *hello* world_

## Building and running with docker
Assuming you have installed docker...

Run maven docker plugin
`mvn package docker:build`

Starting docker image
`docker run --name meta -d -p 9000:9000 -t difi/metadata-validator`

Open a browser and write
`<ip-to-docker>:9000`

To find your docker machine's IP address, type `env | grep DOCKER` and use IP address in `DOCKER_HOST`