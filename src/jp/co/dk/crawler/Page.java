package jp.co.dk.crawler;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.Documents;
import jp.co.dk.crawler.dao.Links;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.Urls;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

public class Page extends jp.co.dk.browzer.Page{
	
	/** データストアマネージャ */
	protected DataStoreManager dataStoreManager;
	
	public Page(String url, DataStoreManager dataStoreManager) throws BrowzingException {
		super(url);
		this.dataStoreManager = dataStoreManager;
	}
	
	public void save() throws CrawlerException, DataStoreManagerException {
		Links     links     = (Links)     dataStoreManager.getDataAccessObject(CrawlerDaoConstants.LINKS);
		Urls      urls      = (Urls)      dataStoreManager.getDataAccessObject(CrawlerDaoConstants.URLS);
		Pages     pages     = (Pages)     dataStoreManager.getDataAccessObject(CrawlerDaoConstants.PAGES);
		Documents documents = (Documents) dataStoreManager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
		
		
	}

}

