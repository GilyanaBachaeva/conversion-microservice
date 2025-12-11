#!/bin/bash

echo "Starting Eureka Server..."
./gradlew :eureka-server:bootRun > eureka.log 2>&1 &
EUREKA_PID=$!

echo "Starting File API..."
./gradlew :file-api:bootRun > file-api.log 2>&1 &
FILE_API_PID=$!

echo "Starting PDF Converter..."
./gradlew :pdf-converter:bootRun > pdf-converter.log 2>&1 &
PDF_PID=$!

echo "Starting Flow Manager..."
./gradlew :flow-manager:bootRun > flow-manager.log 2>&1 &
FLOW_PID=$!

echo "All services started! Press Ctrl+C to stop."

trap "kill $EUREKA_PID $FILE_API_PID $PDF_PID $FLOW_PID 2>/dev/null; exit" INT TERM

wait