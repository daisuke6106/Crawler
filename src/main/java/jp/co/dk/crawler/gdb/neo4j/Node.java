package jp.co.dk.crawler.gdb.neo4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.gdb.neo4j.exception.CrawlerNeo4JException;
import jp.co.dk.crawler.gdb.neo4j.message.CrawlerNeo4JMessage;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Relationship;

public class Node {
	
	protected org.neo4j.graphdb.Node node;
	
	Node(org.neo4j.graphdb.Node node) {
		this.node = node;
	}
	
	public List<Node> getOutGoingNodes(NodeSelector selector) {
		List<Node> nodeList = new ArrayList<Node>();
		Iterator<Relationship> relationshipList = this.node.getRelationships(Direction.OUTGOING).iterator();
		while (relationshipList.hasNext()) {
			Relationship relationship = relationshipList.next();
			org.neo4j.graphdb.Node node = relationship.getEndNode();
			if (selector.isSelect(node)) {
				nodeList.add(new Node(node));
			}
		}
		return nodeList;
	}
	
	public List<Node> getOutGoingNodes() {
		List<Node> nodeList = new ArrayList<Node>();
		Iterator<Relationship> relationshipList = this.node.getRelationships(Direction.OUTGOING).iterator();
		while (relationshipList.hasNext()) {
			Relationship relationship = relationshipList.next();
			nodeList.add(new Node(relationship.getEndNode()));
		}
		return nodeList;
	}
	
	public void addLabel(org.neo4j.graphdb.Label label) {
		this.node.addLabel(label);
	}
	
	public void setProperty(Map<String, Object> properties) throws CrawlerNeo4JException {
		for (Map.Entry<String, Object> property : properties.entrySet()) {
			String key = property.getKey();
			Object val = property.getValue();
			if (val == null) throw new CrawlerNeo4JException(CrawlerNeo4JMessage.PARAMETER_IS_FRAUD, key, "null");
			if (val instanceof String) {
				this.setProperty(key, (String)val);
			} else if (val instanceof Integer){
				this.setProperty(key, ((Integer)val).intValue());
			} else if (val instanceof Boolean) {
				this.setProperty(key, ((Boolean)val).booleanValue());
			} else {
				throw new CrawlerNeo4JException(CrawlerNeo4JMessage.PARAMETER_IS_FRAUD, key, val.toString());
			}
		}
	}
	
	public void setProperty(String key, String value) {
		this.node.setProperty(key, value);
	}
	
	public void setProperty(String key, int value) {
		this.node.setProperty(key, new Integer(value));
	}
	
	public void setProperty(String key, boolean value) {
		this.node.setProperty(key, new Boolean(value));
	}
	
	public String getProperty(String key) {
		return (String)this.node.getProperty(key);
	}
}
