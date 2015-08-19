package jp.co.dk.crawler.gdb.neo4j;

public class Node {
	
	protected org.neo4j.graphdb.Node node;
	
	Node(org.neo4j.graphdb.Node node) {
		this.node = node;
	}
	
	public void setProperty(String key, String value) {
		this.node.setProperty(key, value);
	}
	
	public String getProperty(String key) {
		return (String)this.node.getProperty(key);
	}
}
