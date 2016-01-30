#!/bin/sh
CURRENT=$(cd $(dirname $0) && pwd)
MAIN_JAR="${CURRENT}/../crawler_0.1.0a.jar"
LIB_JAR="${CURRENT}/../lib/*"
MESSAGE_DIR="${CURRENT}/../messages"
PROPERTY_DIR="${CURRENT}/../properties"
java -classpath "${MAIN_JAR}:${LIB_JAR}:${MESSAGE_DIR}:${PROPERTY_DIR}" jp.co.dk.crawler.gdb.controler.CrawlerPreparationMain $*

