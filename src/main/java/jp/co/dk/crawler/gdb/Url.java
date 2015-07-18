package jp.co.dk.crawler.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.DATASTOREMANAGER_CAN_NOT_CREATE;
import static jp.co.dk.datastoremanager.message.DataStoreManagerMessage.METHOD_TO_CONVERT_A_RESULT_IS_UNDEFINED;

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
			
			Cypher hostNode = new Cypher("(host:HOST{name:?})").setParameter(this.getHost());
			CountComvertable hostCount = dataStore.executeWithRetuen(new Cypher("MATCH").append(hostNode).append("RETURN COUNT(host)"), new CountComvertable("COUNT(host)")).get(0);
			if (hostCount.getCount() == 0) dataStore.execute(new Cypher("CREATE").append(hostNode));
			
			List<String> pathList = this.getPathList();
			for (int i=0; i<pathList.size(); i++) {
				Cypher pathNode = new Cypher("(path").append(Integer.toString(i)).append(":PATH{name:?})").setParameter(pathList.get(i));
				hostNode.append("-[:CHILD]->").append(pathNode);
				CountComvertable pathCount = dataStore.executeWithRetuen(new Cypher("MATCH").append(pathNode).append("RETURN COUNT(*)"), new CountComvertable("COUNT(*)")).get(0);
				if (pathCount.getCount() == 0) {
					
				}
			}
			
//			for (int i=0; i<this.getPathList().size(); i++) {
//				StringBuilder pathStr = new StringBuilder("-[:CHILD]->(path").append(i).append(":PATH{name:{").append(i+2).append("}})");
//				Cypher clonedPathData = pathData.clone().append(pathStr.toString()).setParameter(this.getPathList().get(i));
//				CountComvertable pathCount = dataStore.executeWithRetuen(clonedPathData.clone().append("RETURN COUNT(*)"), new CountComvertable("COUNT(*)")).get(0);
//				if (pathCount.getCount() == 0) {
//					Cypher createPathData ;
//					if (i==0) {
//						createPathData = pathData.clone().append("CREATE(host)");
//					} else {
//						createPathData = pathData.clone().append("CREATE(path" + (i-1) + ")");
//					}
//					createPathData.append(pathStr.toString()).setParameter(this.getPathList().get(i));
//					dataStore.execute(createPathData);
//				}
//				pathData = clonedPathData;
//			}
			
			Cypher parameterData = new Cypher("MATCH(host:HOST{name:{1}})").setParameter(this.getHost());
			for (int i=0; i<this.getPathList().size(); i++) parameterData.append("-[:CHILD]->(path").append(Integer.toString(i)).append(":PATH{name:{").append(Integer.toString(i+2)).append("}})").setParameter(this.getPathList().get(i));
			Cypher clonedParameterData = parameterData.clone();
			clonedParameterData.append("(");
			Map<String, String> parameterMap = this.getParameter();
			int index = this.getPathList().size();
			for (Map.Entry<String, String> param : parameterMap.entrySet()) {
				clonedParameterData.append(param.getKey()).append(":").append("{").append(Integer.toString(index)).append("},").setParameter(param.getValue());
				index++;
			}
			clonedParameterData.append(")RETURN COUNT(*)");
			CountComvertable parameterCount = dataStore.executeWithRetuen(parameterData.clone().append("RETURN COUNT(*)"), new CountComvertable("COUNT(*)")).get(0);
			index = this.getPathList().size();
			if (parameterCount.getCount() == 0) {
				parameterData.append("CREATE(");
				for (Map.Entry<String, String> param : parameterMap.entrySet()) {
					clonedParameterData.append(param.getKey()).append(":").append("{").append(Integer.toString(index)).append("},").setParameter(param.getValue());
					index++;
				}
			}
			
		} catch (ClassCastException | DataStoreManagerException e) {
			throw new CrawlerSaveException(DATASTOREMANAGER_CAN_NOT_CREATE);
		}
		
		return false;
	}
	
	
}
