# Container Build Deploy

This is a universal Pipeline for building and deployment any container from a prepared `Dockerfile` and `docker-compose` files on a remote host.

## Parameters

Set a `Dockerfile` URL to build the image and a `docker-compose` URL to run the container. Setting variables (which will be passed to the `.env` file) is supported, as well as a list of commands to run before building.

![](/container-build-deploy/img/params.jpg)

## Build and start container

The example uses a prepared [Docker Agent](/jenkins-agent/README.md) image that I use in my home environment.

![](/container-build-deploy/img/build.jpg)