#! /bin/bash
# run: bash ./build-script-local.sh
# remove current container and image
docker rm -f elearning_support_ai
docker rmi leopepe2012/elearning_support_system:ai-module-new
# rebuild image
docker build --platform=linux/amd64 -f ./Dockerfile --tag=leopepe2012/elearning_support_system:ai-module-new .
# push image to Docker Registry
docker push leopepe2012/elearning_support_system:ai-module-new
# run the container using docker-compose
docker compose -f docker-compose-production.yml up -d
# view logs after running the container
docker logs -f elearning_support_ai --tail 500