#!/bin/bash

source deployment.config

aws cloudformation delete-stack --stack-name "$stack_name"
echo "Stack $stack_name will be destroyed soon"
$SHELL