package jp.co.dk.crawler;

import jp.co.dk.datastoremanager.DataStoreKind;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.database.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.database.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;
import jp.co.dk.message.exception.AbstractMessageException;
import jp.co.dk.test.template.TestCaseTemplate;

public class TestCrawlerFoundation extends TestCaseTemplate{
	/**
	 * アクセス可能なDBアクセスパラメータを設定したDataBaseAccessParameterを返却します。
	 * 
	 * @return DBアクセスパラメータ
	 * @throws DataStoreManagerException 引数が不足していた場合
	 */
	
	protected DataBaseAccessParameter getAccessableDataBaseAccessParameter() throws DataStoreManagerException {
		return new DataBaseAccessParameter(DataStoreKind.MYSQL, DataBaseDriverConstants.MYSQL, "192.168.11.101:3306", "test_db", "test_user", "123456");
	}
	
	/**
	 * アクセス不可能なDBアクセスパラメータを設定したDataBaseAccessParameterを返却します。
	 * 
	 * @return DBアクセスパラメータ
	 * @throws DataStoreManagerException 引数が不足していた場合
	 */
	protected DataBaseAccessParameter getAccessFaileDataBaseAccessParameter() throws DataStoreManagerException {
		return new DataBaseAccessParameter(DataStoreKind.MYSQL, DataBaseDriverConstants.MYSQL, "255.255.255.255:3306", "test_db", "test_user", "123456");
	}
	
	protected DataStoreManager getAccessableDataStoreManager()  throws DataStoreManagerException {
		return new DataStoreManager(getAccessableDataStoreManagerProperty());
	}
	
	protected DataStoreManager getAccessFaileDataStoreManager()  throws DataStoreManagerException {
		return new DataStoreManager(getAccessFaileDataStoreManagerProperty());
	}
	
	protected DataStoreManagerProperty getAccessableDataStoreManagerProperty() throws DataStoreManagerException {
		return new DataStoreManagerProperty("properties/test/AccessableDataStoreManager.properties");
	}
	
	protected DataStoreManagerProperty getAccessFaileDataStoreManagerProperty() throws DataStoreManagerException{
		return new DataStoreManagerProperty("AccessFaileDataStoreManager.properties");
	}
	
	@Override
	protected void fail(Throwable e) {
		super.fail(e);
	}
	
	@Override
	protected void fail(AbstractMessageException e) {
		super.fail(e);
	}
}
