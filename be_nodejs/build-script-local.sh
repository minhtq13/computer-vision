#! /bin/bash
# remove current container and image
docker rm -f elearning_support_backend_nodejs
docker rmi -f leopepe2012/elearning_support_system:backend-nodejs
# rebuild image
docker build -f ./Dockerfile --tag=leopepe2012/elearning_support_system:backend-nodejs .
# run the container using docker-compose
docker compose -f docker-compose-local.yml up -d
# view logs after running the container
docker logs -f elearning_support_be_nodejs --tail 500