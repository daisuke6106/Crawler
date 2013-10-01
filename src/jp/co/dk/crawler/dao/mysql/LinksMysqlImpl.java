package jp.co.dk.crawler.dao.mysql;

import static jp.co.dk.crawler.message.CrawlerMessage.PARAMETER_IS_NOT_SET;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.dao.Links;
import jp.co.dk.crawler.dao.record.LinksRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.database.AbstractDataBaseAccessObject;
import jp.co.dk.datastoremanager.database.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.database.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.database.Sql;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

public class LinksMysqlImpl extends AbstractDataBaseAccessObject implements Links{
	
	public LinksMysqlImpl(DataBaseAccessParameter parameter) throws DataStoreManagerException {
		super(parameter);
	}
	
	public LinksMysqlImpl(DataBaseDriverConstants driver, String url,String sid, String user, String password) throws DataStoreManagerException {
		super(driver, url, sid, user, password);
	}
	
	public LinksMysqlImpl(DataStore datastore) throws DataStoreManagerException {
		super(datastore);
	}
	
	@Override
	public void createTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("CREATE TABLE LINKS ");
		sb.append('(');
		sb.append("FROM_PROTOCOL        VARCHAR(6)   NOT NULL,");
		sb.append("FROM_HOSTNAME        VARCHAR(256) NOT NULL,");
		sb.append("FROM_H_PATH          INT          NOT NULL,");
		sb.append("FROM_H_PARAM         INT          NOT NULL,");
		sb.append("TO_PROTOCOL        VARCHAR(6)   NOT NULL,");
		sb.append("TO_HOSTNAME        VARCHAR(256) NOT NULL,");
		sb.append("TO_H_PATH          INT          NOT NULL,");
		sb.append("TO_H_PARAM         INT          NOT NULL,");
		sb.append("CREATE_DATE     DATETIME, ");
		sb.append("UPDATE_DATE     DATETIME, ");
		sb.append("PRIMARY KEY(FROM_PROTOCOL, FROM_HOSTNAME, FROM_H_PATH, FROM_H_PARAM))");
		Sql sql = new Sql(sb.toString());
		this.createTable(sql);
	}
	
	@Override
	public void dropTable() throws DataStoreManagerException {
		StringBuilder sb = new StringBuilder("DROP TABLE LINKS ");
		Sql sql = new Sql(sb.toString());
		this.dropTable(sql);
	}
	
	@Override
	public LinksRecord select(String protcol, String host, List<String> path, Map<String, String> parameter) throws DataStoreManagerException {
		if (path == null) path = new ArrayList<String>();
		if (parameter == null) parameter = new HashMap<String, String>();
		StringBuilder sb = new StringBuilder("SELECT * FROM LINKS WHERE FROM_PROTOCOL=? AND FROM_HOSTNAME=? AND FROM_H_PATH=? AND FROM_H_PARAM=?");
		Sql sql = new Sql(sb.toString());
		sql.setParameter(protcol);
		sql.setParameter(host);
		sql.setParameter(path.hashCode());
		sql.setParameter(parameter.hashCode());
		return this.selectSingle(sql, new LinksRecord());
	}

	@Override
	public void insert(String from_protcol, String from_host, List<String> from_path, Map<String, String> from_parameter, String to_protcol, String to_host, List<String> to_path, Map<String, String> to_parameter, Date createDate, Date updateDate) throws DataStoreManagerException, CrawlerException {
		if (from_protcol == null || from_protcol.equals("")) throw new CrawlerException(PARAMETER_IS_NOT_SET, "from_protocol");
		if (from_host == null    || from_host.equals(""))    throw new CrawlerException(PARAMETER_IS_NOT_SET, "from_host");
		if (from_path == null) from_path = new ArrayList<String>();
		if (from_parameter == null) from_parameter = new HashMap<String, String>();
		if (to_protcol == null || to_protcol.equals("")) throw new CrawlerException(PARAMETER_IS_NOT_SET, "to_protocol");
		if (to_host == null    || to_host.equals(""))    throw new CrawlerException(PARAMETER_IS_NOT_SET, "to_host");
		if (to_path == null) to_path = new ArrayList<String>();
		if (to_parameter == null) to_parameter = new HashMap<String, String>();
		Sql sql = new Sql("INSERT INTO LINKS VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		sql.setParameter(from_protcol);
		sql.setParameter(from_host);
		sql.setParameter(from_path.hashCode());
		sql.setParameter(from_parameter.hashCode());
		sql.setParameter(to_protcol);
		sql.setParameter(to_host);
		sql.setParameter(to_path.hashCode());
		sql.setParameter(to_parameter.hashCode());
		sql.setParameter(new Timestamp(createDate.getTime()));
		sql.setParameter(new Timestamp(updateDate.getTime()));
		this.insert(sql);
	}

}
