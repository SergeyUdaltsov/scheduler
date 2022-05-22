#!/bin/bash

source deployment.config

mvn clean install -e

for lambda_name in $lambdas
do
  :
  echo "Updating $lambda_name function code..."
  aws lambda update-function-code --function-name $lambda_name --zip-file fileb://bot/target/original-bot-1.0-SNAPSHOT.jar
done

#$SHELL