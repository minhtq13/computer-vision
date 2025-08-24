#! /bin/bash
# copy source be_python to be_java
#cp -rf ../../be_python ./src/main/resources # linux
# remove current container and image
docker rm -f elearning_support_backend
docker rmi -f leopepe2012/elearning_support_system:java-backend
# rebuild image
docker build --platform=linux/amd64 -f ./Dockerfile --tag=leopepe2012/elearning_support_system:java-backend .
# push image to Docker Registry
docker push leopepe2012/elearning_support_system:java-backend
# run the container using docker-compose
docker compose -f docker-compose-production.yml up -d
# view logs after running the container
docker logs -f elearning_support_backend --tail 500