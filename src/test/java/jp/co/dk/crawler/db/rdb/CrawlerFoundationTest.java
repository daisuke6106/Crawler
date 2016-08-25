package jp.co.dk.crawler.db.rdb;

import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerException;
import jp.co.dk.neo4jdatastoremanager.property.Neo4JDataStoreManagerProperty;
import jp.co.dk.test.template.TestCaseTemplate;

public class CrawlerFoundationTest extends TestCaseTemplate{
	
	protected static DataStoreManager getMysqlAccessableDataStoreManager() throws DataStoreManagerException {
		return new DataStoreManager(new DataStoreManagerProperty("properties/test/mysql/AccessableDataStoreManager.properties"));
	}
	
	protected static DataStoreManager getMysqlAccessFaileDataStoreManager() throws DataStoreManagerException {
		return new DataStoreManager(new DataStoreManagerProperty("properties/test/mysql/AccessFaileDataStoreManager.properties"));
	}
	
	protected static Neo4JDataStoreManager getNeo4JAccessableDataStoreManager() throws Neo4JDataStoreManagerException {
		return new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty("properties/test/neo4j/AccessableDataStoreManager.properties"));
	}
	
	protected static Neo4JDataStoreManager getNeo4JAccessFaileDataStoreManager() throws Neo4JDataStoreManagerException{
		return new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty("properties/test/neo4j/AccessFaileDataStoreManager.properties"));
	}
	
	@Override
	protected void fail(Throwable e) {
		super.fail(e);
	}
	
//	@Before
//	public void createTable() throws DataStoreManagerException {
//		DataStoreManager manager = getMysqlAccessableDataStoreManager();
//		manager.startTrunsaction();
//		Links     links     = (Links)manager.getDataAccessObject(CrawlerDaoConstants.LINKS);
//		Urls      urls      = (Urls)manager.getDataAccessObject(CrawlerDaoConstants.URLS);
//		Pages     pages     = (Pages)manager.getDataAccessObject(CrawlerDaoConstants.PAGES);
//		Documents documents = (Documents)manager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
//		RedirectErrors    redirectErrors = (RedirectErrors)manager.getDataAccessObject(CrawlerDaoConstants.REDIRECT_ERRORS);
//		CrawlerErrors     crawlerErrors  = (CrawlerErrors)manager.getDataAccessObject(CrawlerDaoConstants.CRAWLER_ERRORS);
//		links.createTable();
//		urls.createTable();
//		pages.createTable();
//		documents.createTable();
//		redirectErrors.createTable();
//		crawlerErrors.createTable();
//		manager.finishTrunsaction();
//	}
//	
//	@After
//	public void dropTable() throws DataStoreManagerException {
//		DataStoreManager manager = getMysqlAccessableDataStoreManager();
//		manager.startTrunsaction();
//		Links     links     = (Links)manager.getDataAccessObject(CrawlerDaoConstants.LINKS);
//		Urls      urls      = (Urls)manager.getDataAccessObject(CrawlerDaoConstants.URLS);
//		Pages     pages     = (Pages)manager.getDataAccessObject(CrawlerDaoConstants.PAGES);
//		Documents documents = (Documents)manager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
//		RedirectErrors    redirectErrors = (RedirectErrors)manager.getDataAccessObject(CrawlerDaoConstants.REDIRECT_ERRORS);
//		CrawlerErrors     crawlerErrors  = (CrawlerErrors)manager.getDataAccessObject(CrawlerDaoConstants.CRAWLER_ERRORS);
//		links.dropTable();
//		urls.dropTable();
//		pages.dropTable();
//		documents.dropTable();
//		redirectErrors.dropTable();
//		crawlerErrors.dropTable();
//		manager.finishTrunsaction();
//	}
	
}
