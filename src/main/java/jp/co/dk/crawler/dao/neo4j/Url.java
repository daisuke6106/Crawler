package jp.co.dk.crawler.dao.neo4j;

import jp.co.dk.browzer.exception.PageIllegalArgumentException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.ResourceIterator;

class Url extends jp.co.dk.browzer.Url {
	
	/** グラフデータベースサービス */
	protected GraphDatabaseService graphDB;
	
	public Url(String url, GraphDatabaseService graphDB) throws PageIllegalArgumentException {
		super(url);
		this.graphDB = graphDB;
	}
	
	void save() {
		org.neo4j.graphdb.Node hostNode = this.createHostNode(this.getHost());
		org.neo4j.graphdb.Node pathNode = hostNode;
		for (String path : this.pathList) {
			pathNode = this.createPath(pathNode, path);
		}
	}
	
	org.neo4j.graphdb.Node createHostNode(String hostname) {
		org.neo4j.graphdb.Node hostNode = null;
		ResourceIterator<org.neo4j.graphdb.Node> nodes = this.graphDB.findNodes(UrlNodeLabel.HOST);
		while (nodes.hasNext()) {
			org.neo4j.graphdb.Node tmpHostNode = nodes.next();
			if (tmpHostNode.getProperty("name").equals(hostname)) hostNode = tmpHostNode;
		}
		if (hostNode == null){
			hostNode = this.graphDB.createNode(UrlNodeLabel.HOST);
			hostNode.setProperty("name", hostname);
		}
		return hostNode;
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
	
	org.neo4j.graphdb.Node createFileNode(org.neo4j.graphdb.Node pathNode, String fileName) {
		org.neo4j.graphdb.Node fileNode = null;
		for (org.neo4j.graphdb.Relationship pathRelationship : pathNode.getRelationships(UrlRelationshipTypes.HAS)) {
			org.neo4j.graphdb.Node tmpFileNode = pathRelationship.getEndNode();
			if (tmpFileNode.getProperty("name").equals(fileName)) fileNode = tmpFileNode;
		}
		if (fileNode == null) {
			fileNode = this.graphDB.createNode(UrlNodeLabel.FILE);
			pathNode.createRelationshipTo(fileNode, UrlRelationshipTypes.HAS);
			return fileNode;
		} else {
			return fileNode;
		}
	}
}

enum UrlNodeLabel implements Label {
	/** ホスト */
	HOST,
	/** パス */
	PATH,
	/** ファイル */
	FILE,
	/** パラメータ */
	PARAMETER,
}
enum UrlRelationshipTypes implements RelationshipType {
	/** HAS */
	HAS,
}