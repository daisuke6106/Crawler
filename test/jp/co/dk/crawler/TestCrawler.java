package jp.co.dk.crawler;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.browzer.message.BrowzingMessage;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.Documents;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.Urls;
import jp.co.dk.crawler.dao.record.DocumentsRecord;
import jp.co.dk.crawler.dao.record.PagesRecord;
import jp.co.dk.crawler.dao.record.UrlsRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.message.CrawlerMessage;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;
import jp.co.dk.property.exception.PropertyException;

import org.junit.Test;

public class TestCrawler extends TestCrawlerFoundation{

	@Test
	public void constractor() {
		// ========================================正常系========================================
		// 引数なしでコンストラクタを呼び出した場合、正常にインスタンスが生成されること。
		try {
			Crawler crawler = new Crawler("http://www.google.com", getAccessableDataStoreManager());
		} catch (BrowzingException | CrawlerException | DataStoreManagerException e) {
			fail(e);
		} 
		
		// ========================================異常系========================================
		try {
			Crawler crawler = new Crawler("http://www.google.com", null);
		} catch (BrowzingException | CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.DETASTORETYPE_IS_NOT_SET);
		} 
		
	}
	
	@Test
	public void saveImage() {
		// ========================================正常系========================================
		// 引数なしでコンストラクタを呼び出した場合、正常にインスタンスが生成されること。
		
		try {
			// ＝＝＝＝＝＝＝＝＝＝準備＝＝＝＝＝＝＝＝＝＝
			// テーブルを作成
			DataStoreManager manager = getAccessableDataStoreManager();
			manager.startTrunsaction();
			Urls      urls      = (Urls)manager.getDataAccessObject(CrawlerDaoConstants.URLS);
			Pages     pages     = (Pages)manager.getDataAccessObject(CrawlerDaoConstants.PAGES);
			Documents documents = (Documents)manager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
			urls.createTable();
			pages.createTable();
			documents.createTable();
			manager.finishTrunsaction();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			Crawler crawler = new Crawler("http://kanasoku.info/articles/27440.html", getAccessableDataStoreManager());
			crawler.saveImage();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (BrowzingException e) {
			fail(e);
		} catch (PropertyException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		} finally {
			// ＝＝＝＝＝＝＝＝＝＝後片付け＝＝＝＝＝＝＝＝＝＝
			// テーブルを削除
			DataStoreManager manager;
			try {
				manager = getAccessableDataStoreManager();
				manager.startTrunsaction();
				Urls      urls      = (Urls)manager.getDataAccessObject(CrawlerDaoConstants.URLS);
				Pages     pages     = (Pages)manager.getDataAccessObject(CrawlerDaoConstants.PAGES);
				Documents documents = (Documents)manager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
				urls.dropTable();
				pages.dropTable();
				documents.dropTable();
				manager.finishTrunsaction();
			} catch (DataStoreManagerException e) {
				fail(e);
			}
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
		}
		
	}
}
