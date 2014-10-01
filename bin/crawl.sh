#!/bin/sh
CURRENT=$(cd $(dirname $0) && pwd)
java -classpath "${CURRENT}/../*" jp.co.dk.crawler.controler.CrawlerMain $*

