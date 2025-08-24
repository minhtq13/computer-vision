#!/bin/bash
docker rm -f elearning_support_backend_go
docker rmi -f leopepe2012/elearning_support_system:go-backend
docker build --platform=linux/amd64 -f ./Dockerfile --tag=leopepe2012/elearning_support_system:go-backend .