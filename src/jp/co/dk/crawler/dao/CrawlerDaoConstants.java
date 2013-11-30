package jp.co.dk.crawler.dao;

import jp.co.dk.crawler.dao.mysql.CrawlerErrorsMysqlImpl;
import jp.co.dk.crawler.dao.mysql.DocumentsMysqlImpl;
import jp.co.dk.crawler.dao.mysql.RedirectErrorsMysqlImpl;
import jp.co.dk.crawler.dao.mysql.LinksMysqlImpl;
import jp.co.dk.crawler.dao.mysql.PagesMysqlImpl;
import jp.co.dk.crawler.dao.mysql.UrlsMysqlImpl;
import jp.co.dk.datastoremanager.DataAccessObject;
import jp.co.dk.datastoremanager.DataAccessObjectFactory;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.DataStoreKind;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

public enum CrawlerDaoConstants implements jp.co.dk.datastoremanager.DaoConstants{
	
	/** ドキュメントテーブル */
	DOCUMENTS("DOCUMENTS", new DataAccessObjectFactory() {
		@Override
		public DataAccessObject getDataAccessObject(DataStoreKind kind, DataStore dataStore) throws DataStoreManagerException {
			switch (kind) {
			case ORACLE:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case MYSQL:
				return new DocumentsMysqlImpl(dataStore);
			case POSTGRESQL:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case CSV:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			default :
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, "unkonown");
			}
		}
	}),
	
	/** リンクテーブル */
	LINKS("LINKS", new DataAccessObjectFactory() {
		@Override
		public DataAccessObject getDataAccessObject(DataStoreKind kind, DataStore dataStore) throws DataStoreManagerException {
			switch (kind) {
			case ORACLE:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case MYSQL:
				return new LinksMysqlImpl(dataStore);
			case POSTGRESQL:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case CSV:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			default :
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, "unkonown");
			}
		}
	}),
	
	/** ページテーブル */
	PAGES("PAGES", new DataAccessObjectFactory() {
		@Override
		public DataAccessObject getDataAccessObject(DataStoreKind kind, DataStore dataStore) throws DataStoreManagerException {
			switch (kind) {
			case ORACLE:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case MYSQL:
				return new PagesMysqlImpl(dataStore);
			case POSTGRESQL:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case CSV:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			default :
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, "unkonown");
			}
		}
	}),
	
	/** URLテーブル */
	URLS("URLS", new DataAccessObjectFactory() {
		@Override
		public DataAccessObject getDataAccessObject(DataStoreKind kind, DataStore dataStore) throws DataStoreManagerException {
			switch (kind) {
			case ORACLE:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case MYSQL:
				return new UrlsMysqlImpl(dataStore);
			case POSTGRESQL:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case CSV:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			default :
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, "unkonown");
			}
		}
	}),
	
	/** REDIRECT_ERRORSテーブル */
	REDIRECT_ERRORS("REDIRECT_ERRORS", new DataAccessObjectFactory() {
		@Override
		public DataAccessObject getDataAccessObject(DataStoreKind kind, DataStore dataStore) throws DataStoreManagerException {
			switch (kind) {
			case ORACLE:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case MYSQL:
				return new RedirectErrorsMysqlImpl(dataStore);
			case POSTGRESQL:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case CSV:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			default :
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, "unkonown");
			}
		}
	}),
	
	/** CRAWLER_ERRORSテーブル */
	CRAWLER_ERRORS("CRAWLER_ERRORS", new DataAccessObjectFactory() {
		@Override
		public DataAccessObject getDataAccessObject(DataStoreKind kind, DataStore dataStore) throws DataStoreManagerException {
			switch (kind) {
			case ORACLE:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case MYSQL:
				return new CrawlerErrorsMysqlImpl(dataStore);
			case POSTGRESQL:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case CSV:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			default :
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, "unkonown");
			}
		}
	}),
	;
	
	/** DAO名称 */
	private String name;
	
	/** DAOファクトリクラスインスタンス */
	private DataAccessObjectFactory factory;
	
	private CrawlerDaoConstants(String name, DataAccessObjectFactory factory) {
		this.name    = name;
		this.factory = factory;
	}
	
	@Override
	public DataAccessObjectFactory getDataAccessObjectFactory() {
		return this.factory;
	}

	@Override
	public String getName() {
		return this.name;
	}

}
