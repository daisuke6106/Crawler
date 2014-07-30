package jp.co.dk.crawler;

import static jp.co.dk.crawler.message.CrawlerMessage.DETASTORETYPE_IS_NOT_SUPPORT;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.CrawlerErrors;
import jp.co.dk.crawler.dao.Documents;
import jp.co.dk.crawler.dao.RedirectErrors;
import jp.co.dk.crawler.dao.Links;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.Urls;
import jp.co.dk.crawler.dao.mysql.CrawlerErrorsMysqlImpl;
import jp.co.dk.crawler.dao.mysql.DocumentsMysqlImpl;
import jp.co.dk.crawler.dao.mysql.LinksMysqlImpl;
import jp.co.dk.crawler.dao.mysql.PagesMysqlImpl;
import jp.co.dk.crawler.dao.mysql.RedirectErrorsMysqlImpl;
import jp.co.dk.crawler.dao.mysql.UrlsMysqlImpl;
import jp.co.dk.datastoremanager.DataAccessObject;
import jp.co.dk.datastoremanager.DataAccessObjectFactory;
import jp.co.dk.datastoremanager.DataStore;
import jp.co.dk.datastoremanager.DataStoreKind;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.database.DataBaseAccessParameter;
import jp.co.dk.datastoremanager.database.DataBaseDriverConstants;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;
import jp.co.dk.message.exception.AbstractMessageException;
import jp.co.dk.test.template.TestCaseTemplate;

public class CrawlerFoundationTest extends TestCaseTemplate{
	/**
	 * アクセス可能なDBアクセスパラメータを設定したDataBaseAccessParameterを返却します。
	 * 
	 * @return DBアクセスパラメータ
	 * @throws DataStoreManagerException 引数が不足していた場合
	 */
	
	protected DataBaseAccessParameter getAccessableDataBaseAccessParameter() throws DataStoreManagerException {
		return new DataBaseAccessParameter(DataStoreKind.MYSQL, DataBaseDriverConstants.MYSQL, "192.168.11.102:3306", "test_db", "test_user", "123456");
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
	
	protected DataStoreManager getAccessableDataStoreManager() {
		try {
			return new DataStoreManager(new DataStoreManagerProperty("properties/test/AccessableDataStoreManager.properties"));
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		return null;
	}
	
	protected DataStoreManager getAccessFaileDataStoreManager() {
		try {
			return new DataStoreManager(new DataStoreManagerProperty("AccessFaileDataStoreManager.properties"));
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		return null;
	}
	
	@Override
	protected void fail(Throwable e) {
		super.fail(e);
	}
	
	@Override
	protected void fail(AbstractMessageException e) {
		super.fail(e);
	}
	

	@Before
	public void createTable() throws DataStoreManagerException {
		DataStoreManager manager = getAccessableDataStoreManager();
		manager.startTrunsaction();
		Links     links     = (Links)manager.getDataAccessObject(CrawlerDaoConstants.LINKS);
		Urls      urls      = (Urls)manager.getDataAccessObject(CrawlerDaoConstants.URLS);
		Pages     pages     = (Pages)manager.getDataAccessObject(CrawlerDaoConstants.PAGES);
		Documents documents = (Documents)manager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
		RedirectErrors    redirectErrors = (RedirectErrors)manager.getDataAccessObject(CrawlerDaoConstants.REDIRECT_ERRORS);
		CrawlerErrors     crawlerErrors  = (CrawlerErrors)manager.getDataAccessObject(CrawlerDaoConstants.CRAWLER_ERRORS);
		links.createTable();
		urls.createTable();
		pages.createTable();
		documents.createTable();
		redirectErrors.createTable();
		crawlerErrors.createTable();
		manager.finishTrunsaction();
	}
	
	@After
	public void dropTable() throws DataStoreManagerException {
		DataStoreManager manager = getAccessableDataStoreManager();
		manager.startTrunsaction();
		Links     links     = (Links)manager.getDataAccessObject(CrawlerDaoConstants.LINKS);
		Urls      urls      = (Urls)manager.getDataAccessObject(CrawlerDaoConstants.URLS);
		Pages     pages     = (Pages)manager.getDataAccessObject(CrawlerDaoConstants.PAGES);
		Documents documents = (Documents)manager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
		RedirectErrors    redirectErrors = (RedirectErrors)manager.getDataAccessObject(CrawlerDaoConstants.REDIRECT_ERRORS);
		CrawlerErrors     crawlerErrors  = (CrawlerErrors)manager.getDataAccessObject(CrawlerDaoConstants.CRAWLER_ERRORS);
		links.dropTable();
		urls.dropTable();
		pages.dropTable();
		documents.dropTable();
		redirectErrors.dropTable();
		crawlerErrors.dropTable();
		manager.finishTrunsaction();
	}
	
}
