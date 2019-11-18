#!/usr/bin/env bash

#============================== Variables
script_name=docker-build
app_name=biblio-api
image_name=pford68/biblio-api
version=`mvn help:evaluate -Dexpression=project.version -q -DforceStdout`
db_host=localhost:3306


#============================== Functions
function usage {
    echo -n "${script_name} [OPTION]... [COMMAND]

     Running without commands builds the docker image.

     Options:
      --DB_USER=<username>    (required) Database username
      --DB_PWD=<pwd>          (required) Database password
      --DB_HOST=<url>         Database host.  Defaults to localhost:3306.
      -h, --help              Display this help and exit

      Commands:
      version               Shows the application version, which will be used for the image tag.
    "
   exit 1
}

function show_version {
   echo ${version}
   exit 1
}

function read_args() {
    for i in "$@"; do
       case ${i} in
          --DB_USER=*)
             db_user=${i#*=}
             ;;
          --DB_PWD=*)
             db_pwd=${i#*=}
             ;;
          --DB_HOST=*)
             db_host=${i#*=}
             ;;
          version)
             show_version
             ;;
          -h | --help)
             usage
             ;;
       esac
    done
}


#============================== Main
read_args $@

if [[ -z ${db_user} ]] || [[ -z ${db_pwd} ]]; then
    usage
fi

docker build --build-arg DB_USER=${db_user} \
   --build-arg DB_PWD=${db_pwd} \
   --build-arg DB_HOST=${db_host} \
   --build-arg APP_VERSION=${version} \
   -t ${image_name}:${version} .
