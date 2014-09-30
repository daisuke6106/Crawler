#!/bin/sh
java -classpath \
"${0%/*}/../crawler_all.jar:./lib/*" \
jp.co.dk.crawler.controler.CrawlerMain  $*
