package jp.co.dk.crawler.db.gdb;

import org.neo4j.graphdb.Label;

enum CrawlerNodeLabel implements Label {
	PROTOCOL,
	HOST,
	PATH,
	PARAMETER,
	URL,
	PAGE,
	REQUEST_HEADER,
	RESPONSE_HEADER,
}