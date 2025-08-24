# Maven build stage
FROM maven:3.8.1-jdk-11-slim AS mavenBuild
COPY . .
RUN mvn dependency:go-offline -B
RUN mvn clean package -Dprod -DskipTests

# Docker build and export image stage
FROM openjdk:11.0.16-jdk
MAINTAINER chiendao1808
WORKDIR /usr/local/app
ARG WORK_DIR=/usr/local/app
# Copy resources
ARG SOURCE_RESOURCE_DIR=src/main/resources
ARG TARGET_APP_RESOURCE_DIR=./appResources
# Copy app resources
COPY ${SOURCE_RESOURCE_DIR}/upload ${TARGET_APP_RESOURCE_DIR}/upload
COPY ${SOURCE_RESOURCE_DIR}/excelTemplate ${TARGET_APP_RESOURCE_DIR}/excelTemplate
COPY ${SOURCE_RESOURCE_DIR}/wordTemplate ${TARGET_APP_RESOURCE_DIR}/wordTemplate
COPY ${SOURCE_RESOURCE_DIR}/config ${TARGET_APP_RESOURCE_DIR}/config
COPY ${SOURCE_RESOURCE_DIR}/db/migration ${TARGET_APP_RESOURCE_DIR}/db/migration
COPY ${SOURCE_RESOURCE_DIR}/*.yml ${TARGET_APP_RESOURCE_DIR}
COPY ${SOURCE_RESOURCE_DIR}/*.properties ${TARGET_APP_RESOURCE_DIR}
COPY ./*.json .
# Copy built .jar file
ARG JAR_FILE=./target/*.jar
COPY --from=mavenBuild ${JAR_FILE} ${WORK_DIR}/e_learning_support_backend.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "e_learning_support_backend.jar"]
