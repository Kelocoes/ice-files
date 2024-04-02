#!/bin/bash

JAR_FILE=".\client\build\libs\client.jar"

NUM_EXECUTIONS=100

for ((i=1; i<=$NUM_EXECUTIONS; i++))
do
    java -jar $JAR_FILE &
done

echo "Se han iniciado $NUM_EXECUTIONS ejecuciones en segundo plano."
