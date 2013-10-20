package jp.co.dk.crawler.dao.mysql;

import static jp.co.dk.crawler.message.CrawlerMessage.PARAMETER_IS_NOT_SET;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.dao.Urls;
import jp.co.dk.crawler.dao.record.UrlsRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.database.AbstractDataBaseAccessObject;
import jp.co.dk.datastoremanager.database.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.database.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.database.Sql;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

public class UrlsMysqlImpl extends AbstractDataBaseAccessObject implements Urls{
	
	public UrlsMysqlImpl(DataBaseAccessParameter parameter) throws DataStoreManagerException {
		super(parameter);
	}
	
	public UrlsMysqlImpl(DataBaseDriverConstants driver, String url,String sid, String user, String password) throws DataStoreManagerException {
		super(driver, url, sid, user, password);
	}
	
	public UrlsMysqlImpl(DataStore datastore) throws DataStoreManagerException {
		super(datastore);
	}
	
	@Override
	public void createTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("CREATE TABLE URLS ");
		sb.append('(');
		sb.append("PROTOCOL        VARCHAR(6)     NOT NULL,");
		sb.append("HOSTNAME        VARCHAR(256)   NOT NULL,");
		sb.append("H_PATH          INT            NOT NULL,");
		sb.append("H_FILENAME      INT            NOT NULL,");
		sb.append("H_PARAM         INT            NOT NULL,");
		sb.append("URL             TEXT           NOT NULL,");
		sb.append("FILEID          BIGINT(8)      NOT NULL,");
		sb.append("CREATE_DATE     DATETIME, ");
		sb.append("UPDATE_DATE     DATETIME, ");
		sb.append("PRIMARY KEY(PROTOCOL, HOSTNAME, H_PATH, H_FILENAME, H_PARAM))");
		Sql sql = new Sql(sb.toString());
		this.createTable(sql);
	}
	
	@Override
	public void dropTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("DROP TABLE URLS ");
		Sql sql = new Sql(sb.toString());
		this.dropTable(sql);
	}
	
	@Override
	public UrlsRecord select(String protcol, String host, List<String> path, String filename, Map<String, String> parameter) throws DataStoreManagerException {
		if (path == null) path = new ArrayList<String>();
		if (parameter == null) parameter = new HashMap<String, String>();
		if (filename == null) filename = "";
		StringBuilder sb = new StringBuilder("SELECT * FROM URLS WHERE PROTOCOL=? AND HOSTNAME=? AND H_PATH=? AND H_FILENAME=? AND H_PARAM=?");
		Sql sql = new Sql(sb.toString());
		sql.setParameter(protcol);
		sql.setParameter(host);
		sql.setParameter(path.hashCode());
		sql.setParameter(filename.hashCode());
		sql.setParameter(parameter.hashCode());
		return this.selectSingle(sql, new UrlsRecord());
	}

	@Override
	public void insert(String protcol, String host, List<String> path, String filename, Map<String, String> parameter, String url, long fileid, Date createDate, Date updateDate) throws DataStoreManagerException, CrawlerException {
		if (protcol == null || protcol.equals("")) throw new CrawlerException(PARAMETER_IS_NOT_SET, "protocol");
		if (host == null    || host.equals(""))    throw new CrawlerException(PARAMETER_IS_NOT_SET, "host");
		if (path == null) path = new ArrayList<String>();
		if (filename == null) filename = "";
		if (parameter == null) parameter = new HashMap<String, String>();
		Sql sql = new Sql("INSERT INTO URLS VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		sql.setParameter(protcol);
		sql.setParameter(host);
		sql.setParameter(path.hashCode());
		sql.setParameter(filename.hashCode());
		sql.setParameter(parameter.hashCode());
		sql.setParameter(url);
		sql.setParameter(fileid);
		sql.setParameter(new Timestamp(createDate.getTime()));
		sql.setParameter(new Timestamp(updateDate.getTime()));
		this.insert(sql);
	}

}
