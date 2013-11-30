package jp.co.dk.crawler.controler;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.browzer.html.element.A;
import jp.co.dk.crawler.Crawler;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.Documents;
import jp.co.dk.crawler.dao.Errors;
import jp.co.dk.crawler.dao.Links;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.Urls;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;
import jp.co.dk.document.Element;
import jp.co.dk.document.ElementSelector;
import jp.co.dk.document.html.HtmlElement;
import jp.co.dk.document.html.constant.HtmlElementName;

public class CrawlerControler {
	
	/**
	 * 環境作成を実行する。<p/>
	 * プロパティに定義されている接続先のデータベースに対して、以下のテーブルを作成する。<br/>
	 * ・LINKS<br/>
	 * ・URLS<br/>
	 * ・PAGES<br/>
	 * ・DOCUMENTS<br/>
	 * ・ERRORS<br/>
	 * 
	 * @throws DataStoreManagerException テーブルの作成に失敗した場合
	 */
	public void createEnvironment() throws DataStoreManagerException {
		DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty();
		DataStoreManager dataStoreManager = new DataStoreManager(dataStoreManagerProperty);
		dataStoreManager.startTrunsaction();
		Links     links     = (Links)    dataStoreManager.getDataAccessObject(CrawlerDaoConstants.LINKS);
		Urls      urls      = (Urls)     dataStoreManager.getDataAccessObject(CrawlerDaoConstants.URLS);
		Pages     pages     = (Pages)    dataStoreManager.getDataAccessObject(CrawlerDaoConstants.PAGES);
		Documents documents = (Documents)dataStoreManager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
		Errors    errors    = (Errors)   dataStoreManager.getDataAccessObject(CrawlerDaoConstants.ERRORS);
		links.createTable();
		urls.createTable();
		pages.createTable();
		documents.createTable();
		errors.createTable();
		dataStoreManager.finishTrunsaction();
	}
	
	
	public void crawl(String url) throws DataStoreManagerException {
		DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty();
		DataStoreManager dataStoreManager = new DataStoreManager(dataStoreManagerProperty);
		dataStoreManager.startTrunsaction();
		
		try {
			Crawler crawler = new Crawler(url, dataStoreManager);
			jp.co.dk.crawler.Page page = (jp.co.dk.crawler.Page)crawler.getPage();
			jp.co.dk.document.File file = page.getDocument();
			jp.co.dk.document.html.HtmlDocument htmlDocument = (jp.co.dk.document.html.HtmlDocument)file;
			htmlDocument.getChildElement(new ElementSelector() {
				@Override
				public boolean judgment(Element element) {
					if (!(element instanceof HtmlElement)) return false;
					HtmlElement htmlElement = (HtmlElement)element;
					if (htmlElement instanceof A) {
						A anchor = (A) htmlElement;
						String beforeHost = anchor.getPage().getHost();
						String nextHost   = anchor.getHost();
						if (beforeHost.equals(nextHost)) return true;
					}
					return false;
				}
			});
		} catch (CrawlerException | BrowzingException e) {
			e.printStackTrace();
		}
		
		dataStoreManager.finishTrunsaction();
	}
	
	public void save(String url) throws DataStoreManagerException, CrawlerException, BrowzingException {
		DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty();
		DataStoreManager dataStoreManager = new DataStoreManager(dataStoreManagerProperty);
		dataStoreManager.startTrunsaction();
		Crawler crawler = new Crawler(url, dataStoreManager);
		crawler.saveAll();
		dataStoreManager.finishTrunsaction();
	}
	
}
