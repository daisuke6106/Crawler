package jp.co.dk.crawler.rdb.dao.mysql;

import java.sql.Timestamp;
import java.util.Date;

import jp.co.dk.crawler.rdb.dao.RedirectErrors;
import jp.co.dk.crawler.rdb.dao.record.CountRecord;
import jp.co.dk.crawler.rdb.dao.record.RedirectErrorsRecord;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.database.AbstractDataBaseAccessObject;
import jp.co.dk.datastoremanager.database.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.database.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.database.Sql;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

public class RedirectErrorsMysqlImpl extends AbstractDataBaseAccessObject implements RedirectErrors{
	
	public RedirectErrorsMysqlImpl(DataBaseAccessParameter parameter) throws DataStoreManagerException {
		super(parameter);
	}
	
	public RedirectErrorsMysqlImpl(DataBaseDriverConstants driver, String url,String sid, String user, String password) throws DataStoreManagerException {
		super(driver, url, sid, user, password);
	}
	
	public RedirectErrorsMysqlImpl(DataStore datastore) throws DataStoreManagerException {
		super(datastore);
	}

	@Override
	public void createTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("CREATE TABLE REDIRECT_ERRORS ");
		sb.append('(');
		sb.append("FILEID             CHAR(64)  NOT NULL,");
		sb.append("TIMEID             BIGINT(8) NOT NULL,");
		sb.append("MESSAGE            TEXT    , ");
		sb.append("STACKTRACEELEMENTS LONGBLOB, ");
		sb.append("CREATE_DATE        DATETIME, ");
		sb.append("UPDATE_DATE        DATETIME, ");
		sb.append("PRIMARY KEY(FILEID, TIMEID))");
		Sql sql = new Sql(sb.toString());
		this.createTable(sql);
	}
	
	@Override
	public void dropTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("DROP TABLE REDIRECT_ERRORS ");
		Sql sql = new Sql(sb.toString());
		this.dropTable(sql);
	}
	
	@Override
	public RedirectErrorsRecord select(String fileId, long timeId) throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("SELECT * FROM REDIRECT_ERRORS WHERE FILEID=? AND TIMEID=?");
		Sql sql = new Sql(sb.toString());
		sql.setParameter(fileId);
		sql.setParameter(timeId);
		return this.selectSingle(sql, new RedirectErrorsRecord());
	}
	
	@Override
	public int count(String fileId, long timeId) throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("SELECT COUNT(*) AS RESULT FROM REDIRECT_ERRORS WHERE FILEID=? AND TIMEID=?");
		Sql sql = new Sql(sb.toString());
		sql.setParameter(fileId);
		sql.setParameter(timeId);
		return this.selectSingle(sql, new CountRecord("RESULT")).getCount();
	}
	
	@Override
	public void insert(String fileId, long timeId, String message, StackTraceElement[] stackTraceElements, Date createDate, Date updateDate) throws DataStoreManagerException{
		Sql sql = new Sql("INSERT INTO REDIRECT_ERRORS VALUES (?, ?, ?, ?, ?, ?)");
		sql.setParameter(fileId);
		sql.setParameter(timeId);
		sql.setParameter(message);
		sql.setParameter(stackTraceElements);
		sql.setParameter(new Timestamp(createDate.getTime()));
		sql.setParameter(new Timestamp(updateDate.getTime()));
		this.insert(sql);
	}
}
