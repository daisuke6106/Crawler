package jp.co.dk.crawler.dao;

import jp.co.dk.crawler.dao.mysql.PagesImple;
import jp.co.dk.datastoremanager.DataAccessObject;
import jp.co.dk.datastoremanager.DataAccessObjectFactory;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.DataStoreKind;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

public enum DaoConstants implements jp.co.dk.datastoremanager.DaoConstants{
	
	/** ページテーブル */
	PAGES("PAGES", new DataAccessObjectFactory() {
		@Override
		public DataAccessObject getDataAccessObject(DataStoreKind kind, DataStore dataStore) throws DataStoreManagerException {
			switch (kind) {
			case ORACLE:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case MYSQL:
				return new PagesImple(dataStore);
			case POSTGRESQL:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			case CSV:
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, kind.toString());
			default :
				throw new DataStoreManagerException(DETASTORETYPE_IS_NOT_SUPPORT, "unkonown");
			}
		}
		
	})
	;
	
	/** DAO名称 */
	private String name;
	
	/** DAOファクトリクラスインスタンス */
	private DataAccessObjectFactory factory;
	
	private DaoConstants(String name, DataAccessObjectFactory factory) {
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
