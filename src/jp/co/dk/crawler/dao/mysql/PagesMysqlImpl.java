package jp.co.dk.crawler.dao.mysql;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.record.PagesRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataConvertable;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.Record;
import jp.co.dk.datastoremanager.database.AbstractDataBaseAccessObject;
import jp.co.dk.datastoremanager.database.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.database.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.database.DataBaseRecord;
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
		sb.append("PROTOCOL         VARCHAR(6)   NOT NULL,");
		sb.append("HOSTNAME         VARCHAR(256) NOT NULL,");
		sb.append("H_PATH           INT          NOT NULL,");
		sb.append("H_PARAM          INT          NOT NULL,");
		sb.append("FILEID           BIGINT(8)    NOT NULL,");
		sb.append("TIMEID           BIGINT(8)    NOT NULL,");
		sb.append("PATH             LONGBLOB,");
		sb.append("PATH_COUNT       INT,");
		sb.append("PARAMETER        LONGBLOB,");
		sb.append("PARAMETER_COUNT  INT,");
		sb.append("REQUEST_HEADER   LONGBLOB,");
		sb.append("RESPONCE_HEADER  LONGBLOB,");
		sb.append("HTTP_STATUS_CODE CHAR(3),");
		sb.append("HTTP_VERSION     CHAR(3),");
		sb.append("CREATE_DATE      DATETIME, ");
		sb.append("UPDATE_DATE      DATETIME, ");
		sb.append("PRIMARY KEY(PROTOCOL, HOSTNAME, H_PATH, H_PARAM, FILEID, TIMEID))");
		Sql sql = new Sql(sb.toString());
		this.createTable(sql);
	}
	
	@Override
	public void dropTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("DROP TABLE PAGES ");
		Sql sql = new Sql(sb.toString());
		this.dropTable(sql);
	}
	
	@Override
	public int count(String protcol, String host, List<String> path, Map<String, String> parameter) throws DataStoreManagerException {
		if (path == null) path = new ArrayList<String>();
		if (parameter == null) parameter = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder("SELECT COUNT(*) AS RESULT FROM PAGES WHERE PROTOCOL=? AND HOSTNAME=? AND H_PATH=? AND H_PARAM=?");
		Sql sql = new Sql(sb.toString());
		sql.setParameter(protcol);
		sql.setParameter(host);
		sql.setParameter(path.hashCode());
		sql.setParameter(parameter.hashCode());
		return this.selectSingle(sql, new DataConvertable(){
			private int count;
			@Override
			public DataConvertable convert(DataBaseRecord arg0)	throws DataStoreManagerException {
				count = arg0.getInt("RESULT");
				return this;
			}
			@Override
			public DataConvertable convert(Record arg0)	throws DataStoreManagerException {
				this.count = arg0.getInt(1);
				return this;
			}
		}).count;
	}
	
	@Override
	public List<PagesRecord> select(String protcol, String host, List<String> path, Map<String, String> parameter) throws DataStoreManagerException {
		if (path == null) path = new ArrayList<String>();
		if (parameter == null) parameter = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder("SELECT * FROM PAGES WHERE PROTOCOL=? AND HOSTNAME=? AND H_PATH=? AND H_PARAM=?");
		Sql sql = new Sql(sb.toString());
		sql.setParameter(protcol);
		sql.setParameter(host);
		sql.setParameter(path.hashCode());
		sql.setParameter(parameter.hashCode());
		return this.selectMulti(sql, new PagesRecord());
	}
	

	@Override
	public PagesRecord select(String protcol, String host, List<String> path, Map<String, String> parameter, long fileId, long timeId) throws DataStoreManagerException {
		if (path == null) path = new ArrayList<String>();
		if (parameter == null) parameter = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder("SELECT * FROM PAGES WHERE PROTOCOL=? AND HOSTNAME=? AND H_PATH=? AND H_PARAM=? AND FILEID=? AND TIMEID=?");
		Sql sql = new Sql(sb.toString());
		sql.setParameter(protcol);
		sql.setParameter(host);
		sql.setParameter(path.hashCode());
		sql.setParameter(parameter.hashCode());
		sql.setParameter(fileId);
		sql.setParameter(timeId);
		return this.selectSingle(sql, new PagesRecord());
	}
	
	@Override
	public void insert(String protcol, String host, List<String> path, Map<String, String> parameter, Map<String, String> requestHeader, Map<String, List<String>> responceHeader, String httpStatusCode, String httpVersion, long fileid, long timeid, Date createDate, Date updateDate) throws DataStoreManagerException, CrawlerException {
		if (protcol == null || protcol.equals("")) throw new CrawlerException(PARAMETER_IS_NOT_SET, "protocol");
		if (host == null    || host.equals(""))    throw new CrawlerException(PARAMETER_IS_NOT_SET, "host");
		if (path == null) path = new ArrayList<String>();
		if (parameter == null) parameter = new HashMap<String, String>();
		Sql sql = new Sql("INSERT INTO PAGES VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		sql.setParameter(protcol);
		sql.setParameter(host);
		sql.setParameter(path.hashCode());
		sql.setParameter(parameter.hashCode());
		sql.setParameter(fileid);
		sql.setParameter(timeid);
		sql.setParameterConvertToBytes(path);
		sql.setParameter(path.size());
		sql.setParameterConvertToBytes(parameter);
		sql.setParameter(parameter.size());
		sql.setParameterConvertToBytes(requestHeader);
		sql.setParameterConvertToBytes(responceHeader);
		sql.setParameter(httpStatusCode);
		sql.setParameter(httpVersion);
		sql.setParameter(new Timestamp(createDate.getTime()));
		sql.setParameter(new Timestamp(updateDate.getTime()));
		this.insert(sql);
	}


}
