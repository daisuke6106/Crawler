package jp.co.dk.crawler.db.rdb;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.message.BrowzingMessage;
import jp.co.dk.crawler.db.rdb.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.db.rdb.dao.Documents;
import jp.co.dk.crawler.db.rdb.dao.Pages;
import jp.co.dk.crawler.db.rdb.dao.RedirectErrors;
import jp.co.dk.crawler.db.rdb.dao.Urls;
import jp.co.dk.crawler.db.rdb.dao.record.DocumentsRecord;
import jp.co.dk.crawler.db.rdb.dao.record.PagesRecord;
import jp.co.dk.crawler.db.rdb.dao.record.UrlsRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.message.CrawlerMessage;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;
import jp.co.dk.property.exception.PropertyException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CrawlerTest extends CrawlerFoundationTest{

//	@Test
//	public void constractor() {
//		// ========================================正常系========================================
//		// 引数なしでコンストラクタを呼び出した場合、正常にインスタンスが生成されること。
//		try {
//			Crawler crawler = new Crawler("http://www.google.com", getAccessableDataStoreManager());
//		} catch (CrawlerInitException | PageIllegalArgumentException | PageAccessException e) {
//			fail(e);
//		} catch (DataStoreManagerException e) {
//			fail(e);
//		} 
//		
//		// ========================================異常系========================================
//		// データストアマネージャを指定せずにコンストラクタを呼び出した場合、例外が発生すること。
//		try {
//			new Crawler("http://www.google.com", null);
//			fail();
//		} catch (BrowzingException | CrawlerException e) {
//			assertEquals(e.getMessageObj(), CrawlerMessage.DATASTOREMANAGER_IS_NOT_SET);
//		} 
//		
//	}
//	
//	@Test
//	public void saveAll() {
//		// ========================================正常系========================================
//		try {
//			// ＝＝＝＝＝＝＝＝＝＝トランザクション開始＝＝＝＝＝＝＝＝＝＝
//			DataStoreManager manager = getAccessableDataStoreManager();
//			manager.startTrunsaction();
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			// テスト実行
//			Crawler crawler = new Crawler("http://kanasoku.info/articles/27440.html", manager);
//			crawler.saveAll();
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			
//			// ＝＝＝＝＝＝＝＝＝＝トランザクション終了＝＝＝＝＝＝＝＝＝＝
//			manager.finishTrunsaction();
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			
//			System.out.print("");
//			
//		} catch (DataStoreManagerException e) {
//			fail(e);
//		} catch (BrowzingException e) {
//			fail(e);
//		} catch (PropertyException e) {
//			fail(e);
//		} catch (CrawlerException e) {
//			fail(e);
//		}
//	}
//	
//	@Test
//	public void saveImage() {
//		// ========================================正常系========================================
//		try {
//			// ＝＝＝＝＝＝＝＝＝＝トランザクション開始＝＝＝＝＝＝＝＝＝＝
//			DataStoreManager manager = getAccessableDataStoreManager();
//			manager.startTrunsaction();
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			// テスト実行
//			Crawler crawler = new Crawler("http://kanasoku.info/articles/27440.html", manager);
//			crawler.saveImage();
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			
//			// ＝＝＝＝＝＝＝＝＝＝トランザクション終了＝＝＝＝＝＝＝＝＝＝
//			manager.finishTrunsaction();
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			
//			
//		} catch (DataStoreManagerException e) {
//			fail(e);
//		} catch (BrowzingException e) {
//			fail(e);
//		} catch (PropertyException e) {
//			fail(e);
//		} catch (CrawlerException e) {
//			fail(e);
//		}
//	}
//	
//	@Test
//	public void saveScript() {
//		// ========================================正常系========================================
//		try {
//			// ＝＝＝＝＝＝＝＝＝＝トランザクション開始＝＝＝＝＝＝＝＝＝＝
//			DataStoreManager manager = getAccessableDataStoreManager();
//			manager.startTrunsaction();
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			// テスト実行
//			Crawler crawler = new Crawler("http://kanasoku.info/articles/27440.html", manager);
//			crawler.saveScript();
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			
//			// ＝＝＝＝＝＝＝＝＝＝トランザクション終了＝＝＝＝＝＝＝＝＝＝
//			manager.finishTrunsaction();
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			
//		} catch (DataStoreManagerException e) {
//			fail(e);
//		} catch (BrowzingException e) {
//			fail(e);
//		} catch (PropertyException e) {
//			fail(e);
//		} catch (CrawlerException e) {
//			fail(e);
//		}
//	}
//	
//	@Test
//	public void saveLink() {
//		// ========================================正常系========================================
//		try {
//			// ＝＝＝＝＝＝＝＝＝＝トランザクション開始＝＝＝＝＝＝＝＝＝＝
//			DataStoreManager manager = getAccessableDataStoreManager();
//			manager.startTrunsaction();
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			// テスト実行
//			Crawler crawler = new Crawler("http://www.htmq.com/html5/script.shtml", manager);
//			crawler.saveLink();
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			
//			// ＝＝＝＝＝＝＝＝＝＝トランザクション終了＝＝＝＝＝＝＝＝＝＝
//			manager.finishTrunsaction();
//			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
//			
//		} catch (DataStoreManagerException e) {
//			fail(e);
//		} catch (BrowzingException e) {
//			fail(e);
//		} catch (PropertyException e) {
//			fail(e);
//		} catch (CrawlerException e) {
//			fail(e);
//		}
//	}
	
}
