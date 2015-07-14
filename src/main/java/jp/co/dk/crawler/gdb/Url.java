package jp.co.dk.crawler.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.DATASTOREMANAGER_CAN_NOT_CREATE;
import static jp.co.dk.datastoremanager.message.DataStoreManagerMessage.METHOD_TO_CONVERT_A_RESULT_IS_UNDEFINED;

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
			
			Cypher createData = new Cypher("MATCH(host:HOST{name:{1}}) RETURN COUNT(host)").setParameter(this.getHost());
			CountComvertable hostCount = dataStore.executeWithRetuen(createData, new CountComvertable("COUNT(host)")).get(0);
			if (hostCount.getCount() == 0) {
				Cypher createHost = new Cypher("CREATE(host:HOST{name:{1}})").setParameter(this.getHost());
				dataStore.execute(createHost);
			}
			
			Cypher pathData = new Cypher("MATCH(host:HOST{name:{1}})").setParameter(this.getHost());
			for (int i=0; i<this.getPathList().size(); i++) {
				StringBuilder pathStr = new StringBuilder("-[:CHILD]->(path").append(i).append(":PATH{name:{").append(i+2).append("}})");  
				Cypher clonedPathData = pathData.clone().append(pathStr.toString()).setParameter(this.getPathList().get(i));
				CountComvertable pathCount = dataStore.executeWithRetuen(clonedPathData.clone().append("RETURN COUNT(*)"), new CountComvertable("COUNT(*)")).get(0);
				if (pathCount.getCount() == 0) {
					Cypher createPathData ;
					if (i==0) {
						createPathData = pathData.clone().append("CREATE(host)");
					} else {
						createPathData = pathData.clone().append("CREATE(path" + (i-1) + ")");
					}
					createPathData.append(pathStr.toString()).setParameter(this.getPathList().get(i));
					dataStore.execute(createPathData);
				}
				pathData = clonedPathData;
			}
			
			Cypher parameter = new Cypher("(");
			Map<String, String> parameterMap = this.getParameter();
			int index = 0;
			for (Map.Entry<String, String> param : parameterMap.entrySet()) {
				parameter.append(param.getKey()).append(":").append("{").append(Integer.toString(index)).append("},").setParameter(param.getValue());
				index++;
			}
			
		} catch (ClassCastException | DataStoreManagerException e) {
			throw new CrawlerSaveException(DATASTOREMANAGER_CAN_NOT_CREATE);
		}
		
		return false;
	}
	
	
}
