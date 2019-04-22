#!/usr/bin/env bash

echo "Validating Java version"
java --version | grep "11.0"

if [[ "$?" -ne 0 ]]; then
    echo "ERROR: Incorrect Java Version Found"
    exit 1
fi


echo "Running the tests"
mvn clean test

if [[ "$?" -ne 0 ]]; then
    echo "ERROR: Tests Failed"
    exit 2
fi


echo "Packaging the app"
mvn clean package

if [[ "$?" -ne 0 ]]; then
    echo "ERROR: Package Failed"
    exit 3
fi


echo "Deploying the app"
sh deploy.sh

if [[ "$?" -ne 0 ]]; then
    echo "ERROR: Deployment Failed"
    exit 4
fi
