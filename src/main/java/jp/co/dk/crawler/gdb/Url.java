package jp.co.dk.crawler.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.DATASTOREMANAGER_CAN_NOT_CREATE;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.AbstractUrl;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.gdb.Cypher;

public class Url extends AbstractUrl {

	public Url(String url, DataStoreManager dataStoreManager) throws PageIllegalArgumentException {
		super(url, dataStoreManager);
	}

	@Override
	public boolean save() throws CrawlerSaveException {
		try {
			jp.co.dk.datastoremanager.gdb.AbstractDataBaseAccessObject dataStore = (jp.co.dk.datastoremanager.gdb.AbstractDataBaseAccessObject)this.dataStoreManager.getDataAccessObject("URL");
			Cypher createData = new Cypher("MERGE(:HOST{url:{1})").setParameter(this.getHost());
			for (int i=0; i<this.getPathList().size() ;i++) createData.append("-[:PATH]->(:HOST{url:{").append(Integer.toString(i + 2)).append("})").setParameter(this.getPathList().get(i));
			dataStore.execute(createData);
		} catch (ClassCastException | DataStoreManagerException e) {
			throw new CrawlerSaveException(DATASTOREMANAGER_CAN_NOT_CREATE);
		}
		
		return false;
	}
	
}
