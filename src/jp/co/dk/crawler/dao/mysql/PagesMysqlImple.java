package jp.co.dk.crawler.dao.mysql;

import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.database.AbstractDataBaseAccessObject;
import jp.co.dk.datastoremanager.database.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.database.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.database.Sql;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

public class PagesMysqlImple extends AbstractDataBaseAccessObject implements Pages{
	
	public PagesMysqlImple(DataBaseAccessParameter parameter) throws DataStoreManagerException {
		super(parameter);
	}
	
	public PagesMysqlImple(DataBaseDriverConstants driver, String url,String sid, String user, String password) throws DataStoreManagerException {
		super(driver, url, sid, user, password);
	}
	
	public PagesMysqlImple(DataStore datastore) throws DataStoreManagerException {
		super(datastore);
	}

	@Override
	public void createTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("CREATE TABLE PAGES ");
		sb.append('(');
		sb.append("PROTOCOL        VARCHAR(6)   NOT NULL ,");
		sb.append("HOSTNAME        VARCHAR(256) ,");
		sb.append("H_PATH          INT          ,");
		sb.append("H_PARAM         INT          ,");
		sb.append("PATH            LONGBLOB,");
		sb.append("PARAMETER       LONGBLOB,");
		sb.append("REQUEST_HEADER  LONGBLOB,");
		sb.append("RESPONCE_HEADER LONGBLOB,");
		sb.append("DATA            LONGBLOB, ");
		sb.append("PRIMARY KEY(PROTOCOL, HOSTNAME, H_PATH, H_PARAM))");
		Sql sql = new Sql(sb.toString());
		this.createTable(sql);
	}
	
	@Override
	public void dropTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("DROP TABLE PAGES ");
		Sql sql = new Sql(sb.toString());
		this.createTable(sql);
	}

}
