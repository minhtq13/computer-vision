docker rm -f elearning_support_fe
docker rmi -f leopepe2012/elearning_support_system:fe-prod
docker build --platform=linux/amd64 -f ./Dockerfile --tag=leopepe2012/elearning_support_system:fe-prod .
#docker build -f ./Dockerfile-mpec --tag=leopepe2012/elearning_support_system:fe-mpec .

# build base image
docker rmi -f leopepe2012/elearning_support_system:fe-react-base
docker build --platform=linux/amd64 -f ./Dockerfile-base --tag=leopepe2012/elearning_support_system:fe-react-base .

# build webserver gateway image
docker rmi -f leopepe2012/elearning_support_system:webserver-gw
docker build --platform=linux/amd64 -f ./Dockerfile-webserver-gw --tag=leopepe2012/elearning_support_system:webserver-gw .