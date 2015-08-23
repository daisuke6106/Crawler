package jp.co.dk.crawler.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.DATASTOREMANAGER_CAN_NOT_CREATE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.AbstractUrl;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.gdb.neo4j.Neo4JDataStore;
import jp.co.dk.crawler.gdb.neo4j.Node;
import jp.co.dk.crawler.gdb.neo4j.NodeSelector;
import jp.co.dk.crawler.gdb.neo4j.cypher.Cypher;
import jp.co.dk.crawler.gdb.neo4j.exception.CrawlerNeo4JCypherException;
import jp.co.dk.crawler.gdb.neo4j.exception.CrawlerNeo4JException;

public class GUrl extends AbstractUrl {
	
	/** データストアマネージャ */
	protected Neo4JDataStore dataStore;
	
	public GUrl(String url, Neo4JDataStore dataStore) throws PageIllegalArgumentException {
		super(url);
		this.dataStore = dataStore;
	}
	
	/** 
	 * データストアマネージャを設定する。
	 * 
	 * @param dataStoreManager データストアマネージャー
	 */
	public void setDataStoreManager(Neo4JDataStore dataStore) {
		this.dataStore = dataStore;
	}
	
	@Override
	public boolean save() throws CrawlerSaveException {
		try {
			Node endnode = dataStore.selectNode(new Cypher("MATCH(host:HOST{name:?})RETURN host").setParameter(this.getHost()));
			if (endnode == null) {
				endnode = dataStore.createNode();
				endnode.addLabel(CrawlerNodeLabel.HOST);
				endnode.setProperty("name", this.getHost());
			}
			for(String path : this.getPathList()){
				List<Node> findNodes = endnode.getOutGoingNodes().stream().filter(pathNode->pathNode.getProperty("name").equals(path)).collect(Collectors.toList());
				if (findNodes.size() == 0) {
					Node newEndnode = dataStore.createNode();
					newEndnode.addLabel(CrawlerNodeLabel.PATH);
					newEndnode.setProperty("name", path);
					endnode.addOutGoingRelation(CrawlerRelationshipLabel.CHILD, newEndnode);
					endnode = newEndnode;
				} else {
					endnode = findNodes.get(0);
				}
			}
			Map<String, Object> parameter = new HashMap<String, Object>(this.getParameter());
			if (parameter.size() != 0) {
				int parameterID = parameter.hashCode();
				if (endnode.getOutGoingNodes(new NodeSelector() {
						@Override
						public boolean isSelect(org.neo4j.graphdb.Node node) {
							int id = ((Integer)node.getProperty("parameter_id")).intValue();
							if (id == parameterID) return true;
							return false;
						}
					}).size() == 0
				) {
					Node newEndnode = dataStore.createNode();
					newEndnode.addLabel(CrawlerNodeLabel.PARAMETER);
					newEndnode.setProperty("parameter_id", parameter.hashCode());
					newEndnode.setProperty(parameter);
					endnode.addOutGoingRelation(CrawlerRelationshipLabel.CHILD, newEndnode);
					endnode = newEndnode;
				}
			}
		} catch (CrawlerNeo4JException | CrawlerNeo4JCypherException e) {
			throw new CrawlerSaveException(DATASTOREMANAGER_CAN_NOT_CREATE, e);
		}
		return false;
	}
	
}

enum CrawlerNodeLabel implements Label {
	HOST,
	PATH,
	PARAMETER,
	PAGE,
}

enum CrawlerRelationshipLabel implements RelationshipType {
	CHILD
}
