package jp.co.dk.crawler.db.gdb;

import org.neo4j.graphdb.RelationshipType;

enum CrawlerRelationshipLabel implements RelationshipType {
	DATA,
	CHILD,
	REQUEST_HEADER,
	RESPONSE_HEADER,
	ANCHOR,
}
