package jp.co.dk.crawler.dao.mysql;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.record.PagesRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.database.AbstractDataBaseAccessObject;
import jp.co.dk.datastoremanager.database.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.database.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.database.Sql;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

public class PagesMysqlImpl extends AbstractDataBaseAccessObject implements Pages{
	
	public PagesMysqlImpl(DataBaseAccessParameter parameter) throws DataStoreManagerException {
		super(parameter);
	}
	
	public PagesMysqlImpl(DataBaseDriverConstants driver, String url,String sid, String user, String password) throws DataStoreManagerException {
		super(driver, url, sid, user, password);
	}
	
	public PagesMysqlImpl(DataStore datastore) throws DataStoreManagerException {
		super(datastore);
	}

	@Override
	public void createTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("CREATE TABLE PAGES ");
		sb.append('(');
		sb.append("PROTOCOL        VARCHAR(6)   NOT NULL,");
		sb.append("HOSTNAME        VARCHAR(256) NOT NULL,");
		sb.append("H_PATH          INT          NOT NULL,");
		sb.append("H_PARAM         INT          NOT NULL,");
		sb.append("PATH            LONGBLOB,");
		sb.append("PARAMETER       LONGBLOB,");
		sb.append("REQUEST_HEADER  LONGBLOB,");
		sb.append("RESPONCE_HEADER LONGBLOB,");
		sb.append("DATA            LONGBLOB, ");
		sb.append("CREATE_DATE     DATE, ");
		sb.append("UPDATE_DATE     DATE, ");
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

	@Override
	public PagesRecord select(String protocol, String hostname, int h_path, int param) throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("SELECT * FROM PAGES WHERE PROTOCOL=? AND HOSTNAME=? AND H_PATH=? AND H_PARAM=?");
		Sql sql = new Sql(sb.toString());
		sql.setParameter(protocol);
		sql.setParameter(hostname);
		sql.setParameter(h_path);
		sql.setParameter(param);
		return this.selectSingle(sql, new PagesRecord());
	}

	@Override
	public void insert(String protcol, String host, List<String> path, Map<String, String> parameter, Map<String, String> requestHeader, Map<String, String> responceHeader, byte[] contents, Date createDate, Date updateDate) throws DataStoreManagerException, CrawlerException {
		if (protcol == null || protcol.equals("")) throw new CrawlerException(PARAMETER_IS_NOT_SET, "protocol");
		if (host == null    || host.equals(""))    throw new CrawlerException(PARAMETER_IS_NOT_SET, "host");
		if (path == null) path = new ArrayList<String>();
		if (parameter == null) parameter = new HashMap<String, String>();
		Sql sql = new Sql("INSERT INTO PAGES VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		sql.setParameter(protcol);
		sql.setParameter(host);
		sql.setParameter(path.hashCode());
		sql.setParameter(parameter.hashCode());
		sql.setParameterConvertToBytes(path);
		sql.setParameterConvertToBytes(parameter);
		sql.setParameterConvertToBytes(requestHeader);
		sql.setParameterConvertToBytes(responceHeader);
		sql.setParameter(contents);
		sql.setParameter(createDate);
		sql.setParameter(updateDate);
		this.insert(sql);
		super.dataStore.finishTransaction();
	}

}
