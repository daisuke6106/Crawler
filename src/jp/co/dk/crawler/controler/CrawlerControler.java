package jp.co.dk.crawler.controler;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.crawler.Crawler;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;

public class CrawlerControler {
	
	public void save(String url) throws DataStoreManagerException, CrawlerException, BrowzingException {
		DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty();
		DataStoreManager dataStoreManager = new DataStoreManager(dataStoreManagerProperty);
		Crawler crawler = new Crawler(url, dataStoreManager);
		crawler.saveAll();
	}
	
}
