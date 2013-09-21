package jp.co.dk.crawler.dao.impl;

import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.database.AbstractDataBaseAccessObject;
import jp.co.dk.datastoremanager.database.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.database.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.database.Sql;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

public class PagesImple extends AbstractDataBaseAccessObject implements Pages{
	
	protected PagesImple(DataBaseAccessParameter parameter) throws DataStoreManagerException {
		super(parameter);
	}
	
	protected PagesImple(DataBaseDriverConstants driver, String url,String sid, String user, String password) throws DataStoreManagerException {
		super(driver, url, sid, user, password);
	}
	
	protected PagesImple(DataStore datastore) throws DataStoreManagerException {
		super(datastore);
	}

	@Override
	public void createTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("CREATE TABLE PAGES ");
		sb.append('(');
		sb.append("PROTOCOL VARCHAR(6),");
		sb.append("HOSTNAME VARCHAR(256),");
		
		sb.append("DATA LONGBLOB(1073741824),");
		sb.append(')');
		Sql sql = new Sql(sb.toString());
		this.createTable(sql);
	}

}
