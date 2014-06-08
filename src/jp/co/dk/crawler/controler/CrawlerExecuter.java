package jp.co.dk.crawler.controler;

import java.util.List;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.crawler.Crawler;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.CrawlerErrors;
import jp.co.dk.crawler.dao.Documents;
import jp.co.dk.crawler.dao.RedirectErrors;
import jp.co.dk.crawler.dao.Links;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.Urls;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;
import jp.co.dk.document.Element;

public class CrawlerExecuter {
	
	/**
	 * 環境作成を実行する。<p/>
	 * プロパティに定義されている接続先のデータベースに対して、以下のテーブルを作成する。<br/>
	 * ・LINKS<br/>
	 * ・URLS<br/>
	 * ・PAGES<br/>
	 * ・DOCUMENTS<br/>
	 * ・REDIRECT_ERRORS<br/>
	 * ・CRAWLER_ERRORS<br/>
	 * 
	 * @throws DataStoreManagerException テーブルの作成に失敗した場合
	 */
	public void createEnvironment() throws DataStoreManagerException {
		DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty();
		DataStoreManager dataStoreManager = new DataStoreManager(dataStoreManagerProperty);
		dataStoreManager.startTrunsaction();
		Links          links          = (Links)         dataStoreManager.getDataAccessObject(CrawlerDaoConstants.LINKS);
		Urls           urls           = (Urls)          dataStoreManager.getDataAccessObject(CrawlerDaoConstants.URLS);
		Pages          pages          = (Pages)         dataStoreManager.getDataAccessObject(CrawlerDaoConstants.PAGES);
		Documents      documents      = (Documents)     dataStoreManager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
		RedirectErrors redirectErrors = (RedirectErrors)dataStoreManager.getDataAccessObject(CrawlerDaoConstants.REDIRECT_ERRORS);
		CrawlerErrors  crawlerErrors  = (CrawlerErrors) dataStoreManager.getDataAccessObject(CrawlerDaoConstants.CRAWLER_ERRORS);
		links.createTable();
		urls.createTable();
		pages.createTable();
		documents.createTable();
		redirectErrors.createTable();
		crawlerErrors.createTable();
		dataStoreManager.finishTrunsaction();
	}
	
	
//	public void crawl(String url, CrawlingControler crawlingRule) throws CrawlerException, BrowzingException, DataStoreManagerException {
//		DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty();
//		DataStoreManager dataStoreManager = new DataStoreManager(dataStoreManagerProperty);
//		dataStoreManager.startTrunsaction();
//		
//		try {
//			Crawler crawler = new Crawler(url, dataStoreManager);
//			crawler.save();
//			List<Element> refsElements = crawlingRule.getRefsElements(crawler.getPage());
//			for (Element element : refsElements) {
//				
//			}
//		} catch (CrawlerException | BrowzingException e) {
//			throw e;
//		}
//		dataStoreManager.finishTrunsaction();
//	}
//	
//	/**
//	 * 
//	 * @param url
//	 * @throws DataStoreManagerException
//	 * @throws CrawlerException
//	 * @throws BrowzingException
//	 */
//	public void save(String url) throws DataStoreManagerException, CrawlerException, BrowzingException {
//		DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty();
//		DataStoreManager dataStoreManager = new DataStoreManager(dataStoreManagerProperty);
//		dataStoreManager.startTrunsaction();
//		Crawler crawler = new Crawler(url, dataStoreManager);
//		crawler.saveAll();
//		dataStoreManager.finishTrunsaction();
//	}
	
}
