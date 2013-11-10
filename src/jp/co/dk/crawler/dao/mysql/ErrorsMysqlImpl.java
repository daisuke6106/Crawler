package jp.co.dk.crawler.dao.mysql;

import java.sql.Timestamp;
import java.util.Date;

import jp.co.dk.crawler.dao.Errors;
import jp.co.dk.crawler.dao.record.DocumentsRecord;
import jp.co.dk.crawler.dao.record.ErrorsRecord;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.database.AbstractDataBaseAccessObject;
import jp.co.dk.datastoremanager.database.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.database.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.database.Sql;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

public class ErrorsMysqlImpl extends AbstractDataBaseAccessObject implements Errors{
	
	public ErrorsMysqlImpl(DataBaseAccessParameter parameter) throws DataStoreManagerException {
		super(parameter);
	}
	
	public ErrorsMysqlImpl(DataBaseDriverConstants driver, String url,String sid, String user, String password) throws DataStoreManagerException {
		super(driver, url, sid, user, password);
	}
	
	public ErrorsMysqlImpl(DataStore datastore) throws DataStoreManagerException {
		super(datastore);
	}

	@Override
	public void createTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("CREATE TABLE ERRORS ");
		sb.append('(');
		sb.append("FILEID             BIGINT(8) NOT NULL,");
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
		StringBuilder sb = new StringBuilder("DROP TABLE ERRORS ");
		Sql sql = new Sql(sb.toString());
		this.dropTable(sql);
	}
	
	@Override
	public ErrorsRecord select(long fileId, long timeId) throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("SELECT * FROM ERRORS WHERE FILEID=? AND TIMEID=?");
		Sql sql = new Sql(sb.toString());
		sql.setParameter(fileId);
		sql.setParameter(timeId);
		return this.selectSingle(sql, new ErrorsRecord());
	}

	@Override
	public void insert(long fileId, long timeId, String message, StackTraceElement[] stackTraceElements, Date createDate, Date updateDate) throws DataStoreManagerException{
		Sql sql = new Sql("INSERT INTO ERRORS VALUES (?, ?, ?, ?, ?, ?)");
		sql.setParameter(fileId);
		sql.setParameter(timeId);
		sql.setParameter(message);
		sql.setParameter(stackTraceElements);
		sql.setParameter(new Timestamp(createDate.getTime()));
		sql.setParameter(new Timestamp(updateDate.getTime()));
		this.insert(sql);
	}

}