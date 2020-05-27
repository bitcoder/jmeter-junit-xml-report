#!/bin/bash

#curl -H "Content-Type: multipart/form-data" -u admin:admin -F "file=@junit.xml" http://192.168.56.102/rest/raven/1.0/import/execution/junit?projectKey=CALC
curl -H "Content-Type: multipart/form-data" -u admin:admin -F "file=@alternate_junit.xml" http://192.168.56.102/rest/raven/1.0/import/execution/junit?projectKey=CALC
