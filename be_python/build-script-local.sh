#! /bin/bash
# run: bash ./build-script-local.sh
# remove current container and image
docker rm -f elearning_support_ai
docker rmi leopepe2012/elearning_support_system:ai-module
# rebuild image
docker build --platform=linux/amd64 -f ./Dockerfile --tag=leopepe2012/elearning_support_system:ai-module .
# run the container using docker-compose
docker compose -f docker-compose-local.yml up -d
# view logs after running the container
docker logs -f elearning_support_ai --tail 500