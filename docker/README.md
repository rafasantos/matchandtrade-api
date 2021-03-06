Docker Image for Match and Trade API
====================================

Building a New Image
--------------------
When a new `matchandtrade-api` version is released we need to update
the docker image to reflect the newly released version.

```
# Build 'matchandtrade-api'
mvn clean verify

# Copy the executable jar file to 'docker/mocked-oauth-server.jar'
cp target/matchandtrade-api-1.4-SNAPSHOT-exec.jar docker/matchandtrade-api-exec.jar

# Build the docker image with a new tag
sudo docker build -t rafaelsantosbra/matchandtrade-api:0.0-SNAPSHOT docker/

# Start the docker image: 'docker-compose up'
sudo docker-compose --file docker/docker-compose.yml up

# Verify if the application started correctly.
curl http://localhost:8080/matchandtrade-api/v1/trades

# Sign-in to dockerhub
sudo docker login

# Push the new tag.
sudo docker push rafaelsantosbra/matchandtrade-api:0.0-SNAPSHOT

```
