# Node build
FROM node:18.20.2-alpine as build
MAINTAINER chiendao1808
WORKDIR /app
COPY package.json .
RUN npm install
COPY . .
RUN npm run build:production
EXPOSE 3000

# Nginx
FROM nginx:stable-alpine3.17
COPY --from=build /app/build /usr/share/nginx/html
COPY --from=build /app/server/nginx-prod.conf /etc/nginx/nginx.conf
#-- using in local dev
#COPY --from=build /app/server/nginx-local.conf /etc/nginx/nginx.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

