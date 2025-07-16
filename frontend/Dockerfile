# base image
FROM node:16.13.0 AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . /app
RUN npm run build

### STAGE 2: Run ###
FROM nginxinc/nginx-unprivileged:1.25-alpine

USER root

RUN apk -U upgrade

COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /app/dist/ /usr/share/nginx/html

WORKDIR /app
RUN chown -R 1001:1001 /app && chmod -R 755 /app && \
        chown -R 1001:1001 /var/cache/nginx && \
        chown -R 1001:1001 /var/log/nginx && \
        chown -R 1001:1001 /etc/nginx/conf.d
RUN touch /var/run/nginx.pid && \
        chown -R 1001:1001 /var/run/nginx.pid

USER 1001