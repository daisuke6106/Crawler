package jp.co.dk.crawler.db.rdb.dao.mysql;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.db.rdb.dao.CrawlerErrors;
import jp.co.dk.crawler.db.rdb.dao.record.CountRecord;
import jp.co.dk.crawler.db.rdb.dao.record.CrawlerErrorsRecord;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.database.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.rdb.Sql;

public class CrawlerErrorsMysqlImpl extends jp.co.dk.datastoremanager.rdb.AbstractDataBaseAccessObject implements CrawlerErrors{
	
	public CrawlerErrorsMysqlImpl(DataBaseAccessParameter parameter) throws DataStoreManagerException {
		super(parameter);
	}
	
	public CrawlerErrorsMysqlImpl(jp.co.dk.datastoremanager.DataBaseDriverConstants driver, String url,String sid, String user, String password) throws DataStoreManagerException {
		super(driver, url, sid, user, password);
	}
	
	public CrawlerErrorsMysqlImpl(DataStore datastore) throws DataStoreManagerException {
		super(datastore);
	}

	@Override
	public void createTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("CREATE TABLE CRAWLER_ERRORS ");
		sb.append('(');
		sb.append("PROTOCOL         VARCHAR(6)   NOT NULL,");
		sb.append("HOSTNAME         VARCHAR(255) NOT NULL,");
		sb.append("H_PATH           INT          NOT NULL,");
		sb.append("H_PARAM          INT          NOT NULL,");
		sb.append("MESSAGE            TEXT    , ");
		sb.append("STACKTRACEELEMENTS LONGBLOB, ");
		sb.append("CREATE_DATE        DATETIME, ");
		sb.append("UPDATE_DATE        DATETIME, ");
		sb.append("PRIMARY KEY(PROTOCOL, HOSTNAME, H_PATH, H_PARAM))");
		Sql sql = new Sql(sb.toString());
		this.createTable(sql);
	}
	
	@Override
	public void dropTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("DROP TABLE CRAWLER_ERRORS ");
		Sql sql = new Sql(sb.toString());
		this.dropTable(sql);
	}
	
	@Override
	public CrawlerErrorsRecord select(String protcol, String host, List<String> path, Map<String, String> parameter) throws DataStoreManagerException {
		if (path == null) path = new ArrayList<String>();
		if (parameter == null) parameter = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder("SELECT * FROM CRAWLER_ERRORS WHERE PROTOCOL=? AND HOSTNAME=? AND H_PATH=? AND H_PARAM=?");
		Sql sql = new Sql(sb.toString());
		sql.setParameter(protcol);
		sql.setParameter(host);
		sql.setParameter(path.hashCode());
		sql.setParameter(parameter.hashCode());
		return this.selectSingle(sql, new CrawlerErrorsRecord());
	}
	
	@Override
	public int count(String protcol, String host, List<String> path,Map<String, String> parameter) throws DataStoreManagerException {
		if (path == null) path = new ArrayList<String>();
		if (parameter == null) parameter = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder("SELECT COUNT(*) AS RESULT FROM CRAWLER_ERRORS WHERE PROTOCOL=? AND HOSTNAME=? AND H_PATH=? AND H_PARAM=?");
		Sql sql = new Sql(sb.toString());
		sql.setParameter(protcol);
		sql.setParameter(host);
		sql.setParameter(path.hashCode());
		sql.setParameter(parameter.hashCode());
		return this.selectSingle(sql, new CountRecord("RESULT")).getCount();
	}
	
	@Override
	public void insert(String protcol, String host, List<String> path, Map<String, String> parameter, String message, StackTraceElement[] stackTraceElements, Date createDate, Date updateDate) throws DataStoreManagerException{
		Sql sql = new Sql("INSERT INTO CRAWLER_ERRORS VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		sql.setParameter(protcol);
		sql.setParameter(host);
		sql.setParameter(path.hashCode());
		sql.setParameter(parameter.hashCode());
		sql.setParameter(message);
		sql.setParameter(stackTraceElements);
		sql.setParameter(new Timestamp(createDate.getTime()));
		sql.setParameter(new Timestamp(updateDate.getTime()));
		this.insert(sql);
	}
}
