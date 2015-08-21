package jp.co.dk.crawler.gdb.neo4j;

public interface NodeSelector {
	boolean isSelect(org.neo4j.graphdb.Node node);
}
