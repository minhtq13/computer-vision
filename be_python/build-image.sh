docker rm -f elearning_support_ai
docker rmi leopepe2012/elearning_support_system:ai-module-new
# rebuild image
docker build --platform=linux/amd64 -f ./Dockerfile --tag=leopepe2012/elearning_support_system:ai-module-new .