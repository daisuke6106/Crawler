#!/bin/sh
CURRENT=$(cd $(dirname $0) && pwd)
java -classpath "${CURRENT}/../lib/*:${CURRENT}/../classes" jp.co.dk.crawler.gdb.controler.CrawlerMain $*

