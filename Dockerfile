FROM openjdk:11
MAINTAINER philip.ford@giantmachines.com
WORKDIR /
ARG DB_USER=mysql
ARG DB_PWD=changeit
ARG DB_NAME=biblio_prod
ARG DB_HOST=host.docker.internal
ARG APP_VERSION

# Configure
ENV DB_USER $DB_USER
ENV DB_PWD $DB_PWD
ENV DB_DBNAME $DB_DBNAME
ENV DB_HOST $DB_HOST
ENV DB_URL jdbc:mysql://${DB_HOST}/${DB_NAME}?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC
ENV APP_VERSION $APP_VERSION

COPY target/biblio-${APP_VERSION}.jar .

# Start server
EXPOSE 3306
EXPOSE 8080
CMD java -jar \
    -Dspring.profiles.active=production,secure \
    -Dspring.datasource.url=${DB_URL} \
    -Dspring.datasource.username=${DB_USER} \
    -Dspring.datasource.password=${DB_PWD} \
    biblio-${APP_VERSION}.jar
