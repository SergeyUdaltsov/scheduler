#!/bin/bash
source ${WORKSPACE}/infrastructure/deployment.config
source ${WORKSPACE}/infrastructure/deployLambdas.sh

echo "Copying 3rd party dependency layer jar to s3..."
aws s3 cp ./bot/target/bot-layer-dependencies.jar s3://"$deploymentBucket"/bot-layer-dependencies.jar

echo "Publishing new version of layer..."
layer_name=$(aws lambda publish-layer-version \
    --layer-name "$lambda_layer_name" \
    --content S3Bucket="$deploymentBucket",S3Key=bot-layer-dependencies.jar \
    --compatible-runtimes "$runtime" --query LayerVersionArn --output text --region $home_region)

for lambda_name in $lambdas
do
  :
  echo "Attaching new version of layer to $lambda_name function..."
  aws lambda update-function-configuration --function-name $lambda_name --layers $layer_name --region $home_region
done
$SHELL


