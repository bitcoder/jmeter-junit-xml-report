#!/bin/bash

if [ $# == 1 ];
then
 TESTSUITE=$1
else
 TESTSUITE="jmeter"
fi
JAR=./target/jmeter-junit-xml-converter-0.0.1-SNAPSHOT-jar-with-dependencies.jar

java -jar $JAR results.jtl junit.xml $TESTSUITE
