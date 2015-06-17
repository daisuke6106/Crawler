package jp.co.dk.crawler.dao.gdb;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.logger.Logger;
import jp.co.dk.logger.LoggerFactory;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExtendedExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;

class Url extends jp.co.dk.browzer.Url {
	
	/** グラフデータベースサービス */
	protected GraphDatabaseService graphDB;
	
	/** ロガーインスタンス */
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	
	public Url(String url, GraphDatabaseService graphDB) throws PageIllegalArgumentException {
		super(url);
		this.graphDB    = graphDB;
	}
	
	void save() {
		org.neo4j.graphdb.Node hostNode = this.createHostNode(this.getHost());
		org.neo4j.graphdb.Node pathNode = hostNode;
		for (String path : this.pathList) {
			pathNode = this.createPath(pathNode, path);
		}
		org.neo4j.graphdb.Node parameterNode = this.createParameter(pathNode, this.getParameter());
	}
	
	org.neo4j.graphdb.Node createHostNode(String hostname) {
		org.neo4j.graphdb.Node hostNode = null;
		ResourceIterator<Node> hostNodes = this.graphDB.findNodes(UrlNodeLabel.HOST);
		while (hostNodes.hasNext()) {
			Node tmpHostNode = hostNodes.next();
			if (tmpHostNode.getProperty("name").equals(hostname)) hostNode = tmpHostNode;
		}
		if (hostNode == null) {
			hostNode = this.graphDB.createNode(UrlNodeLabel.HOST);
			hostNode.setProperty("name", hostname);
			return hostNode;
		} else {
			return hostNode;
		}
	}
	
	org.neo4j.graphdb.Node createPath(org.neo4j.graphdb.Node parentNode, String path) {
		org.neo4j.graphdb.Node childNode = null;
		for (org.neo4j.graphdb.Relationship pathRelationship : parentNode.getRelationships(UrlRelationshipTypes.HAS)) {
			org.neo4j.graphdb.Node tmpPathNode = pathRelationship.getEndNode();
			if (tmpPathNode.getProperty("name").equals(path)) childNode = tmpPathNode;
		}
		if (childNode == null) {
			childNode = this.graphDB.createNode(UrlNodeLabel.PATH);
			childNode.setProperty("name", path);
			parentNode.createRelationshipTo(childNode, UrlRelationshipTypes.HAS);
			return childNode;
		} else {
			return childNode;
		}
	}
	
	org.neo4j.graphdb.Node createParameter(org.neo4j.graphdb.Node pathNode, Map<String, String> parameterMap) {
		if (parameterMap.size() == 0) return pathNode;
		org.neo4j.graphdb.Node parameterNode = null;
		for (org.neo4j.graphdb.Relationship parameterRelationship : pathNode.getRelationships(UrlRelationshipTypes.PARAMETER)) {
			org.neo4j.graphdb.Node tmpParameterNode = parameterRelationship.getEndNode();
			Map<String, String> tmpParameterMap = new HashMap<String, String>();
			for (String key : tmpParameterNode.getPropertyKeys()) {
				tmpParameterMap.put(key, (String)tmpParameterNode.getProperty(key));
			}
			if (tmpParameterMap.equals(parameterMap)) parameterNode = tmpParameterNode;
		}
		if (parameterNode == null) {
			parameterNode = this.graphDB.createNode(UrlNodeLabel.PARAMETER);
			for (Entry<String, String> paramEntry : parameterMap.entrySet()) {
				parameterNode.setProperty(paramEntry.getKey(),paramEntry.getValue());
			}
			pathNode.createRelationshipTo(parameterNode, UrlRelationshipTypes.PARAMETER);
			return parameterNode;
		} else {
			return parameterNode;
		}
	}
}

enum UrlNodeLabel implements Label {
	/** ホスト */
	HOST,
	/** パス */
	PATH,
	/** パラメータ */
	PARAMETER,
}
enum UrlRelationshipTypes implements RelationshipType {
	/** HAS */
	HAS,
	/** PARAMETER */
	PARAMETER,
}