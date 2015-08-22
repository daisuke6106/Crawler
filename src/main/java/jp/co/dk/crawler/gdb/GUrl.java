package jp.co.dk.crawler.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.DATASTOREMANAGER_CAN_NOT_CREATE;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.AbstractUrl;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.gdb.neo4j.Neo4JDataStore;
import jp.co.dk.crawler.gdb.neo4j.Node;
import jp.co.dk.crawler.gdb.neo4j.NodeSelector;
import jp.co.dk.crawler.gdb.neo4j.cypher.Cypher;
import jp.co.dk.crawler.gdb.neo4j.exception.CrawlerNeo4JCypherException;

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
				endnode.addLabel();
				endnode.setProperty("name", this.getHost());
			}
			for(String path : this.getPathList()){
				if (endnode.getOutGoingNodes().stream().filter(pathNode->pathNode.getProperty("name").equals(path)).count() == 0) {
					endnode = dataStore.createNode();
					endnode.setProperty("name", path);
				}
			}
			
			
			
//			List<NodeCypher> pathNodes = new ArrayList<>();
//			List<String> pathList = this.getPathList();
//			for (int i=0; i<this.getPathList().size(); i++) pathNodes.add((NodeCypher) new NodeCypher("(path" + i + ":PATH{name:?})", "(path" + i + ")").setParameter(pathList.get(i)));
//			
//			List<Cypher> existNode    = new ArrayList<>();
//			existNode.add(hostNode);
//			
//			Cypher countCypher  = hostNode.clone();
//			String lastVarName  = "(host)";
//			for (int i=0; i < pathNodes.size(); i++) {
//				Cypher tmpCountCypher = countCypher.clone();
//				tmpCountCypher.append("-[:CHILD]->").append(pathNodes.get(i));
//				CountComvertable pathCount = dataStore.executeWithRetuen(new Cypher("MATCH").append(tmpCountCypher).append("RETURN COUNT(*)"), new CountComvertable("COUNT(*)")).get(0);
//				if (pathCount.getCount() == 0) {
//					dataStore.execute(new Cypher("MATCH").append(countCypher).append("CREATE").append(lastVarName).append("-[:CHILD]->").append(pathNodes.get(i)));
//				}
//				countCypher = tmpCountCypher;
//				lastVarName = pathNodes.get(i).getVarName();
//			}
//			
//			if (this.getParameter().size() != 0) {
//				NodeCypher parameterNode = new NodeCypher("(parameter:PARAMETER{", "(parameter)");
//				Set<Map.Entry<String, String>> params = this.getParameter().entrySet();
//				int index = 0;
//				for (Map.Entry<String, String> param : params) { 
//					parameterNode.append(param.getKey()).append(":").append("?").setParameter(param.getValue());
//					if (index != params.size()-1) parameterNode.append(",");
//					index++;
//				}
//				parameterNode.append("})");
//				
//				CountComvertable pathCount = dataStore.executeWithRetuen(
//						new Cypher("MATCH").append(countCypher).append("-[:CHILD]->").append(parameterNode).append("RETURN COUNT(*)"),
//						new CountComvertable("COUNT(*)")
//					).get(0);
//				if (pathCount.getCount() == 0) {
//					dataStore.execute(new Cypher("MATCH").append(countCypher).append("CREATE").append(lastVarName).append("-[:CHILD]->").append(parameterNode));
//				}
//			}
			
		} catch (CrawlerNeo4JCypherException e) {
			throw new CrawlerSaveException(DATASTOREMANAGER_CAN_NOT_CREATE, e);
		}
		
		return false;
	}
	
}
