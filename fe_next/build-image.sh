docker rm -f elearning_support_fe
docker rmi -f leopepe2012/elearning_support_system:fe-prod
docker build --platform=linux/amd64 -f ./Dockerfile --tag=leopepe2012/elearning_support_system:fe-prod .
# build base image
docker rmi -f leopepe2012/elearning_support_system:fe-next-base
docker build --platform=linux/amd64 -f ./Dockerfile-base --tag=leopepe2012/elearning_support_system:fe-next-base .