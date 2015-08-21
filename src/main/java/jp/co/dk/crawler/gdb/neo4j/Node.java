package jp.co.dk.crawler.gdb.neo4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
	
	public void setProperty(String key, String value) {
		this.node.setProperty(key, value);
	}
	
	public String getProperty(String key) {
		return (String)this.node.getProperty(key);
	}
}
