# url-shortener
Url shortener for ENLAB

## How to run
1. build JAR locally (mvn clean install)
2. run docker compose up -d
3. the API will be available on localhost:8080

## Known bugs
- Sometimes the url_shortener database isn't automatically created by spring boot. This results in an exception on startup of the url-shortener spring boot application. In this case the database needs to be created manually
