package jp.co.dk.crawler.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.DATASTOREMANAGER_CAN_NOT_CREATE;
import static jp.co.dk.datastoremanager.message.DataStoreManagerMessage.METHOD_TO_CONVERT_A_RESULT_IS_UNDEFINED;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.AbstractUrl;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.gdb.Cypher;
import jp.co.dk.datastoremanager.gdb.DataBaseNode;
import jp.co.dk.datastoremanager.gdb.DataConvertable;

public class Url extends AbstractUrl {

	public Url(String url, DataStoreManager dataStoreManager) throws PageIllegalArgumentException {
		super(url, dataStoreManager);
	}

	@Override
	public boolean save() throws CrawlerSaveException {
		try {
			jp.co.dk.datastoremanager.gdb.AbstractDataBaseAccessObject dataStore = (jp.co.dk.datastoremanager.gdb.AbstractDataBaseAccessObject)this.dataStoreManager.getDataAccessObject("URL");
			
			class CountComvertable implements DataConvertable {
				private String countname;
				private int    count;
				CountComvertable(String countname){
					this.countname = countname;
				}
				@Override
				public DataConvertable convert(DataBaseNode dataBaseNode) throws DataStoreManagerException {
					this.count = dataBaseNode.getInt(this.countname);
					return this;
				}
				int getCount() {
					return this.count;
				}
			}
			
			class NodeCypher extends jp.co.dk.datastoremanager.gdb.Cypher {
				private String varName;
				NodeCypher(String cypher, String varName) throws DataStoreManagerException {
					super(cypher);
					this.varName = varName;
				}
				String getVarName() {
					return this.varName;
				}
			}
			
			Cypher hostNode = new NodeCypher("(host:HOST{name:?})", "(host)").setParameter(this.getHost());
			CountComvertable hostCount = dataStore.executeWithRetuen(new Cypher("MATCH").append(hostNode).append("RETURN COUNT(host)"), new CountComvertable("COUNT(host)")).get(0);
			if (hostCount.getCount() == 0) dataStore.execute(new Cypher("CREATE").append(hostNode));
			
			List<NodeCypher> pathNodes = new ArrayList<>();
			List<String> pathList = this.getPathList();
			for (int i=0; i<this.getPathList().size(); i++) pathNodes.add((NodeCypher) new NodeCypher("(path" + i + ":PATH{name:?})", "(path" + i + ")").setParameter(pathList.get(i)));
			
			List<Cypher> existNode    = new ArrayList<>();
			existNode.add(hostNode);
			
			Cypher countCypher  = hostNode.clone();
			String lastVarName  = "(host)";
			for (int i=0; i < pathNodes.size(); i++) {
				Cypher tmpCountCypher = countCypher.clone();
				tmpCountCypher.append("-[:CHILD]->").append(pathNodes.get(i));
				CountComvertable pathCount = dataStore.executeWithRetuen(new Cypher("MATCH").append(tmpCountCypher).append("RETURN COUNT(*)"), new CountComvertable("COUNT(*)")).get(0);
				if (pathCount.getCount() == 0) {
					dataStore.execute(new Cypher("MATCH").append(countCypher).append("CREATE").append(lastVarName).append("-[:CHILD]->").append(pathNodes.get(i)));
				}
				countCypher = tmpCountCypher;
				lastVarName = pathNodes.get(i).getVarName();
			}
			
			if (this.getParameter().size() != 0) {
				NodeCypher parameterNode = new NodeCypher("(parameter:PARAMETER{", "(parameter)");
				for (Map.Entry<String, String> param : this.getParameter().entrySet()) 
					parameterNode.append(param.getKey()).append(":").append("?").setParameter(param.getValue());
				parameterNode.append("})");
				
				CountComvertable pathCount = dataStore.executeWithRetuen(
						new Cypher("MATCH").append(countCypher).append("-[:CHILD]->").append(parameterNode).append("RETURN COUNT(*)"),
						new CountComvertable("COUNT(*)")
					).get(0);
				if (pathCount.getCount() == 0) {
					dataStore.execute(new Cypher("MATCH").append(countCypher).append("CREATE").append(lastVarName).append("-[:CHILD]->").append(parameterNode));
				}
			}
			
		} catch (ClassCastException | DataStoreManagerException e) {
			throw new CrawlerSaveException(DATASTOREMANAGER_CAN_NOT_CREATE);
		}
		
		return false;
	}
	
	
}
