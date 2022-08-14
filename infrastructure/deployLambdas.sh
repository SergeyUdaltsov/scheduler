#!/bin/bash

source ${WORKSPACE}/infrastructure/deployment.config

mvn clean install -e

  # shellcheck disable=SC2154
for lambda_name in $lambdas
do
  :
  echo "Updating $lambda_name function code..."
  aws lambda update-function-code --function-name $lambda_name --zip-file fileb://./bot/target/"$bot_jar" --region $home_region
done

#$SHELL