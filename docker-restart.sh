#!/usr/bin/env bash
source local.env

#==============================Declarations
app_name=biblio-api
image_name=pford68/biblio-api
version=${1:-latest}

#==============================Main
docker stop ${app_name}
docker rm ${app_name}
docker build --build-arg DB_USER=${DB_USER} --build-arg DB_PWD=${DB_PWD} -t ${image_name}:${version} .
docker run --name ${app_name} -p 3306:3306 -p 8081:8080  -d ${image_name}:${version}
