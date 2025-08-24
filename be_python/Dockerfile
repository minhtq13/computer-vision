FROM leopepe2012/elearning_support_system:ai-module-base
ARG WORK_DIR=/usr/local/app
COPY . ${WORK_DIR}
WORKDIR /usr/local/app
ENTRYPOINT ["uwsgi", "--ini", "wsgi.ini"]