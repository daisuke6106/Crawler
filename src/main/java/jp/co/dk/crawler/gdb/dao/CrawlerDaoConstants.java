package jp.co.dk.crawler.gdb.dao;

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
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case POSTGRESQL:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case CSV:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case NEO4J:
				
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
