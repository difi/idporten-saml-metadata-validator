# First time install and run
First of all, git, java and maven must be installed on your machine. When you have made sure of this, you can clone the project and run
`mvn clean install`

Since this is a spring boot application, standard jar and all, start up the application with
`java -jar target/metadata-validator-1.0.0-SNAPSHOT.jar`

All spring boot parameters are available, use property server.port to override port the application is starting on. With default settings, you can call the application by pasting 
 `http://localhost:9000`
to the address line in your browser. This should print _biig *hello* world_

## Setting up docker
The project is set up with a Dockerfile and build plugin for that file. First: Make sure you have installed and set up docker machine properly. You can test this by finding a command line and write 
`docker ps`
If you get some bad error, you either need to install a docker machine, or it is not set up properly.
### Installing docker on Windows
https://docs.docker.com/windows/step_one/
### Installing docker on linux/nix
https://docs.docker.com/linux/step_one/
### Install docker on mac
https://docs.docker.com/engine/installation/mac/

## Running application on local docker
When docker is set up properly, you can run the maven docker build
`mvn package docker:build`

To start the docker image, try
`docker run -p 9000:9000 -t difi/metadata-validator`

Now you are all set to go, with your own metadata-validator ready for you. Write localhost:9000 in your browser to confirm.