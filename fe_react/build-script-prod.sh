#! /bin/bash
# remove current container and image
docker rm -f elearning_support_fe_react
docker rmi -f leopepe2012/elearning_support_system:fe-react
# rebuild image
docker build --platform=linux/amd64 -f ./Dockerfile --tag=leopepe2012/elearning_support_system:fe-react .
# push image to Docker Registry
docker push leopepe2012/elearning_support_fe:fe-react
# run the container using docker-compose
docker compose -f docker-compose-production.yml up -d
# view logs after running the container
docker logs -f elearning_support_fe_react --tail 500