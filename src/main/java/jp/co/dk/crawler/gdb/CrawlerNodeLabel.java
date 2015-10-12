package jp.co.dk.crawler.gdb;

import org.neo4j.graphdb.Label;

enum CrawlerNodeLabel implements Label {
	HOST,
	PATH,
	PARAMETER,
	URL,
	PAGE,
	REQUEST_HEADER,
	RESPONSE_HEADER,
}