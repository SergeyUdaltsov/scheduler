#!/bin/bash
source deployment.config

echo "Creating deployment bucket..."
#aws s3api create-bucket --bucket "$deploymentBucket" --region "$home_region" \
#--create-bucket-configuration LocationConstraint="$home_region"
#
#echo "Copying deployment resources to s3..."
#aws s3 cp ./substacks/apis.yml s3://"$deploymentBucket"/apis.yml
#aws s3 cp ./substacks/dbTables.yml s3://"$deploymentBucket"/dbTables.yml
#aws s3 cp ./substacks/lambdas.yml s3://"$deploymentBucket"/lambdas.yml
#aws s3 cp ./substacks/layers.yml s3://"$deploymentBucket"/layers.yml
#aws s3 cp ./substacks/queues.yml s3://"$deploymentBucket"/queues.yml
#aws s3 cp ./substacks/roles.yml s3://"$deploymentBucket"/roles.yml
#aws s3 cp ./substacks/buckets.yml s3://"$deploymentBucket"/buckets.yml

aws cloudformation create-stack --stack-name "$stack_name" --template-body file://"$parent_stack" \
--capabilities CAPABILITY_NAMED_IAM \
--parameters ParameterKey=DeploymentBucketUrl,ParameterValue="$deploymentBucket"

$SHELL