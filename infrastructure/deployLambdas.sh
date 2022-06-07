#!/bin/bash

source deployment.config

# shellcheck disable=SC2154
stackStatus=$(aws cloudformation describe-stacks --stack-name "$stack_name" --query Stacks[0].StackStatus --output text)

if [ "$stackStatus" != "CREATE_COMPLETE" ]; then
  echo "Infrastructure is deployed correctly. Updating resources is in progress..."
  [ -e "$deploymentStatusFile" ] && rm "$deploymentStatusFile"
  printf "Infrastructure was not deployed correctly. Updating resources is unavailable.\nCheck the result of $deploymentScriptFile file execution" >> "$deploymentStatusFile"
    exit 1
fi
mvn clean install -e

  # shellcheck disable=SC2154
for lambda_name in $lambdas
do
  :
  echo "Updating $lambda_name function code..."
  aws lambda update-function-code --function-name $lambda_name --zip-file fileb://./bot/target/"$bot_jar"
done

#$SHELL