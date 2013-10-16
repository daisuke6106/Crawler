package jp.co.dk.crawler.dao.mysql;

import java.sql.Timestamp;
import java.util.Date;

import jp.co.dk.crawler.dao.Documents;
import jp.co.dk.crawler.dao.record.DocumentsRecord;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.database.AbstractDataBaseAccessObject;
import jp.co.dk.datastoremanager.database.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.database.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.database.Sql;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

public class DocumentsMysqlImpl extends AbstractDataBaseAccessObject implements Documents{
	
	public DocumentsMysqlImpl(DataBaseAccessParameter parameter) throws DataStoreManagerException {
		super(parameter);
	}
	
	public DocumentsMysqlImpl(DataBaseDriverConstants driver, String url,String sid, String user, String password) throws DataStoreManagerException {
		super(driver, url, sid, user, password);
	}
	
	public DocumentsMysqlImpl(DataStore datastore) throws DataStoreManagerException {
		super(datastore);
	}

	@Override
	public void createTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("CREATE TABLE DOCUMENTS ");
		sb.append('(');
		sb.append("FILEID          BIGINT(8) NOT NULL,");
		sb.append("TIMEID          BIGINT(8) NOT NULL,");
		sb.append("FILENAME        TEXT      NOT NULL,");
		sb.append("EXTENTION       TEXT      NOT NULL,");
		sb.append("LASTUPDATE_DATE DATETIME, ");
		sb.append("DATA            LONGBLOB, ");
		sb.append("CREATE_DATE     DATETIME, ");
		sb.append("UPDATE_DATE     DATETIME, ");
		sb.append("PRIMARY KEY(FILEID, TIMEID))");
		Sql sql = new Sql(sb.toString());
		this.createTable(sql);
	}
	
	@Override
	public void dropTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("DROP TABLE DOCUMENTS ");
		Sql sql = new Sql(sb.toString());
		this.dropTable(sql);
	}

	@Override
	public DocumentsRecord select(long fileId, long timeId) throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("SELECT * FROM DOCUMENTS WHERE FILEID=? AND TIMEID=?");
		Sql sql = new Sql(sb.toString());
		sql.setParameter(fileId);
		sql.setParameter(timeId);
		return this.selectSingle(sql, new DocumentsRecord());
	}

	@Override
	public void insert(long fileId, long timeId, String filename, String extention, Date lastUpdateDate, byte[] data, Date createDate, Date updateDate) throws DataStoreManagerException {
		Sql sql = new Sql("INSERT INTO DOCUMENTS VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		sql.setParameter(fileId);
		sql.setParameter(timeId);
		sql.setParameter(filename);
		sql.setParameter(extention);
		sql.setParameter(new Timestamp(lastUpdateDate.getTime()));
		sql.setParameter(data);
		sql.setParameter(new Timestamp(createDate.getTime()));
		sql.setParameter(new Timestamp(updateDate.getTime()));
		this.insert(sql);
	}

}