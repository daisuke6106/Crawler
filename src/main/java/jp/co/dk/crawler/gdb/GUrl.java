package jp.co.dk.crawler.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.RelationshipType;

import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.AbstractUrl;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStore;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.Node;
import jp.co.dk.neo4jdatastoremanager.NodeSelector;
import jp.co.dk.neo4jdatastoremanager.cypher.Cypher;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerCypherException;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerException;

public class GUrl extends AbstractUrl {
	
	/** データストアマネージャ */
	protected Neo4JDataStoreManager dataStoreManager;
	
	public GUrl(String url, Neo4JDataStoreManager dataStoreManager) throws PageIllegalArgumentException {
		super(url);
		this.dataStoreManager = dataStoreManager;
	}
	
	/** 
	 * データストアマネージャを設定する。
	 * 
	 * @param dataStoreManager データストアマネージャー
	 */
	public void setDataStoreManager(Neo4JDataStoreManager dataStoreManager) {
		this.dataStoreManager = dataStoreManager;
	}
	
	@Override
	public boolean save() throws CrawlerSaveException {
		try {
			Neo4JDataStore dataStore = this.dataStoreManager.getDataAccessObject("URL");
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
		} catch (Neo4JDataStoreManagerException | Neo4JDataStoreManagerCypherException e) {
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
