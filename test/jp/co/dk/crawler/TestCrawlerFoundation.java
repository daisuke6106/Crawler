package jp.co.dk.crawler;

import jp.co.dk.datastoremanager.DataStoreKind;
import jp.co.dk.datastoremanager.database.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.database.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
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
		return new DataBaseAccessParameter(DataStoreKind.MYSQL, DataBaseDriverConstants.MYSQL, "192.168.11.13:3306", "test_db", "test_user", "123456");
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
	
	@Override
	protected void fail(Throwable e) {
		e.printStackTrace();
		super.fail(e);
	}
	
	@Override
	protected void fail(AbstractMessageException e) {
		e.printStackTrace();
		super.fail(e);
	}
}
