#! /bin/bash
# remove current container and image
docker rm -f elearning_support_fe_next
docker rmi -f leopepe2012/elearning_support_system:fe-next
# rebuild image
docker build --platform=linux/amd64 -f ./Dockerfile --tag=leopepe2012/elearning_support_system:fe-next .
# push image to Docker Registry
docker push leopepe2012/elearning_support_system:fe-next
# run the container using docker-compose
docker compose -f docker-compose-production.yml up -d
# view logs after running the container
docker logs -f elearning_support_fe_next --tail 500