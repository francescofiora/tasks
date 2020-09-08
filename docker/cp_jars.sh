#!/bin/bash

if [ -f ../task-api/build/libs/task-api-1.0-SNAPSHOT.jar ]; then
	cp ../task-api/build/libs/task-api-1.0-SNAPSHOT.jar ./tasks-api/
fi

if [ -f ../task-executor/build/libs/task-executor-1.0-SNAPSHOT.jar ]; then
	cp ../task-executor/build/libs/task-executor-1.0-SNAPSHOT.jar ./tasks-executor/
fi
