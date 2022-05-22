#!/bin/bash
source deployment.config
source deployLambdas.sh

echo "Copying 3rd party dependency layer jar to s3..."
aws s3 cp ./bot/target/bot-layer-dependencies.jar s3://layer-bucket-scheduler/bot-layer-dependencies.jar

echo "Publishing new version of layer..."
layer_name=$(aws lambda publish-layer-version \
    --layer-name $lambda_layer_name \
    --content S3Bucket=$layer_bucket_name,S3Key=bot-layer-dependencies.jar \
    --compatible-runtimes $runtime --query LayerVersionArn --output text)

for lambda_name in $lambdas
do
  :
  echo "Attaching new version of layer to $lambda_name function..."
  aws lambda update-function-configuration --function-name $lambda_name --layers $layer_name
done
$SHELL


