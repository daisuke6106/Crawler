package jp.co.dk.crawler.db.rdb.dao.mysql;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.db.rdb.dao.Pages;
import jp.co.dk.crawler.db.rdb.dao.record.CountRecord;
import jp.co.dk.crawler.db.rdb.dao.record.PagesRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.AbstractDataBaseAccessObject;
import jp.co.dk.datastoremanager.rdb.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.rdb.Sql;

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
		sb.append("HOSTNAME         VARCHAR(255) NOT NULL,");
		sb.append("H_PATH           INT          NOT NULL,");
		sb.append("H_PARAM          INT          NOT NULL,");
		sb.append("FILEID           CHAR(64)     NOT NULL,");
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
		return this.selectSingle(sql, new CountRecord("RESULT")).getCount();
	}
	
	@Override
	public List<PagesRecord> select(String protcol, String host, List<String> path, Map<String, String> parameter) throws DataStoreManagerException {
		if (path == null) path = new ArrayList<String>();
		if (parameter == null) parameter = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder("SELECT * FROM PAGES WHERE PROTOCOL=? AND HOSTNAME=? AND H_PATH=? AND H_PARAM=? ORDER BY TIMEID DESC");
		Sql sql = new Sql(sb.toString());
		sql.setParameter(protcol);
		sql.setParameter(host);
		sql.setParameter(path.hashCode());
		sql.setParameter(parameter.hashCode());
		return this.selectMulti(sql, new PagesRecord());
	}
	

	@Override
	public PagesRecord select(String protcol, String host, List<String> path, Map<String, String> parameter, String fileId, long timeId) throws DataStoreManagerException {
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
	public PagesRecord selectLastest(String protcol, String host, List<String> path, Map<String, String> parameter) throws DataStoreManagerException {
		if (path == null) path = new ArrayList<String>();
		if (parameter == null) parameter = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder("SELECT * FROM PAGES WHERE PROTOCOL=? AND HOSTNAME=? AND H_PATH=? AND H_PARAM=? ORDER BY TIMEID");
		Sql sql = new Sql(sb.toString());
		sql.setParameter(protcol);
		sql.setParameter(host);
		sql.setParameter(path.hashCode());
		sql.setParameter(parameter.hashCode());
		List<PagesRecord> result = this.selectMulti(sql, new PagesRecord());
		if (result.size()==0) return null;
		return result.get(0);
	}
	@Override
	public void insert(String protcol, String host, List<String> path, Map<String, String> parameter, Map<String, String> requestHeader, Map<String, List<String>> responceHeader, String httpStatusCode, String httpVersion, String fileid, long timeid, Date createDate, Date updateDate) throws DataStoreManagerException, CrawlerException {
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
