package jp.co.dk.crawler.db.rdb;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageHeaderImproperException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.db.rdb.RPage;
import jp.co.dk.crawler.db.rdb.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.db.rdb.dao.Documents;
import jp.co.dk.crawler.db.rdb.dao.Pages;
import jp.co.dk.crawler.db.rdb.dao.Urls;
import jp.co.dk.crawler.db.rdb.dao.record.DocumentsRecord;
import jp.co.dk.crawler.db.rdb.dao.record.PagesRecord;
import jp.co.dk.crawler.db.rdb.dao.record.UrlsRecord;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

import org.junit.Test;

public class PageTest extends CrawlerFoundationTest{

	@Test
	public void constactor() throws DataStoreManagerException {
		DataStoreManager manager = getMysqlAccessableDataStoreManager();
		try {
			RPage page = new RPage("http://www.google.com", manager);
		} catch (BrowzingException e) {
			manager.finishTrunsaction();
			fail(e);
		}
	}
	
	@Test
	public void save() throws DataStoreManagerException {
		DataStoreManager manager = getMysqlAccessableDataStoreManager();
		try {
			// ＝＝＝＝＝＝＝＝＝＝トランザクション開始＝＝＝＝＝＝＝＝＝＝
			manager.startTrunsaction();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			RPage page = new RPage("http://ja.wikipedia.org/wiki/HyperText_Markup_Language", manager);
			page.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			String             protocol  = page.getProtocol();
			String             host      = page.getHost();
			List<String>       pathList  = page.getPathList();
			Map<String,String> parameter = page.getParameter().getParameter();
			String             fileId    = page.getFileId();
			long               timeId    = page.getTimeId();
			Urls      urls      = (Urls)manager.getDataAccessObject(CrawlerDaoConstants.URLS);
			Pages     pages     = (Pages)manager.getDataAccessObject(CrawlerDaoConstants.PAGES);
			Documents documents = (Documents)manager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
			UrlsRecord      urlRecord       = urls.select(protocol, host, pathList, parameter);
			PagesRecord     pagesRecord     = pages.select(protocol, host, pathList, parameter, fileId, timeId);
			DocumentsRecord documentsRecord = documents.select(fileId, timeId);
			
			// ＝＝＝＝＝＝＝＝＝＝トランザクション終了＝＝＝＝＝＝＝＝＝＝
			manager.finishTrunsaction();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
		} catch ( PageIllegalArgumentException | PageAccessException | CrawlerSaveException e) {
			manager.finishTrunsaction();
			fail(e);
		} catch (DataStoreManagerException e) {
			manager.finishTrunsaction();
			fail(e);
		}
	}
	
	@Test
	public void getCount() throws DataStoreManagerException {
		DataStoreManager manager = getMysqlAccessableDataStoreManager();
		try {
			// ＝＝＝＝＝＝＝＝＝＝トランザクション開始＝＝＝＝＝＝＝＝＝＝
			manager.startTrunsaction();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			RPage page1 = new RPage("http://www.google.com", manager);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertEquals(page1.getCount(), 0);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			RPage page2 = new RPage("http://www.ugtop.com/spill.shtml", manager);
			page2.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertEquals(page2.getCount(), 1);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			RPage page3 = new RPage("http://www.google.com", manager);
			page3.save();
			page3.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertEquals(page3.getCount(), 1);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			RPage page4 = new RPage("http://gigazine.net/news/20131103-four-elements-of-company-building/", manager);
			page4.save();
			page4.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertEquals(page4.getCount(), 1);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝トランザクション終了＝＝＝＝＝＝＝＝＝＝
			manager.finishTrunsaction();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
		} catch ( PageIllegalArgumentException | PageAccessException | CrawlerSaveException e) {
			manager.finishTrunsaction();
			fail(e);
		} catch (DataStoreManagerException e) {
			manager.finishTrunsaction();
			fail(e);
		}
	}
	
	@Test
	public void isSaved() throws DataStoreManagerException {
		DataStoreManager manager = getMysqlAccessableDataStoreManager();
		try {
			// ＝＝＝＝＝＝＝＝＝＝トランザクション開始＝＝＝＝＝＝＝＝＝＝
			manager.startTrunsaction();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			RPage page1 = new RPage("http://www.google.com", manager);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertThat(page1.isSaved(), is(false));
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			RPage page2 = new RPage("http://www.htmq.com/html/head.shtml", manager);
			page2.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertThat(page2.isSaved(), is(true));
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			RPage page3 = new RPage("http://www.google.com", manager);
			page3.save();
			page3.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertThat(page3.isSaved(), is(true));
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝トランザクション終了＝＝＝＝＝＝＝＝＝＝
			manager.finishTrunsaction();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
		} catch ( PageIllegalArgumentException | PageAccessException | CrawlerSaveException e) {
			manager.finishTrunsaction();
			fail(e);
		} catch (DataStoreManagerException e) {
			manager.finishTrunsaction();
			fail(e);
		}
	}
	
	@Test
	public void isLastest() throws DataStoreManagerException {
		DataStoreManager manager = getMysqlAccessableDataStoreManager();
		try {
			// ＝＝＝＝＝＝＝＝＝＝トランザクション開始＝＝＝＝＝＝＝＝＝＝
			manager.startTrunsaction();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			RPage page1 = new RPage("http://www.google.com", manager);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertThat(page1.isLatest(), is(false));
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			// 最終更新日付けが設定されていないページの場合
			RPage page2 = new RPage("http://www.htmq.com/html/head.shtml", manager);
			if (page2.getResponseHeader().getLastModified() == null) {
				// 保存を実行
				page2.save();
				// テスト結果確認
				assertThat(page2.isLatest(), is(true));
				// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			} else {
				fail();
			}
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			RPage page3 = new RPage("http://www.google.com", manager);
			page3.save();
			page3.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertThat(page3.isLatest(), is(true));
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝トランザクション終了＝＝＝＝＝＝＝＝＝＝
			manager.finishTrunsaction();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
		} catch ( PageIllegalArgumentException | PageAccessException | PageHeaderImproperException | CrawlerSaveException e) {
			manager.finishTrunsaction();
			fail(e);
		} catch (DataStoreManagerException e) {
			manager.finishTrunsaction();
			fail(e);
		}
	}
	
	@Test
	public void sameDate() throws DataStoreManagerException {
		DataStoreManager manager = getMysqlAccessableDataStoreManager();
		try {
			RPage page = new RPage("http://www.google.com", getMysqlAccessableDataStoreManager());
			// NULL２つ設定した場合、trueが返却されること。
			assertThat(page.sameDate(null, null), is(true));
			
			// 片方のみ値を設定した場合、falseが返却されること。
			assertThat(page.sameDate(new Date(), null), is(false));
			assertThat(page.sameDate(null, new Date()), is(false));
			
			// 同じ日付けオブジェクトを渡した場合、trueが返却されること。
			Date date = new Date();
			assertThat(page.sameDate(date, date), is(true));
			
			assertThat(page.sameDate(new Date(date.getTime()), new Date(date.getTime()+1)), is(false));
		} catch (BrowzingException e) {
			manager.finishTrunsaction();
			fail(e);
		}
		
	}
	
	@Test
	public void sameBytes() throws DataStoreManagerException {
		DataStoreManager manager = getMysqlAccessableDataStoreManager();
		try {
			RPage page = new RPage("http://www.google.com", getMysqlAccessableDataStoreManager());
			// NULL２つ設定した場合、trueが返却されること。
			assertThat(page.sameBytes(null, null), is(true));
			
			// 片方のみ値を設定した場合、falseが返却されること。
			byte[] bytes = {1,2,3};
			assertThat(page.sameBytes(bytes, null), is(false));
			assertThat(page.sameBytes(null, bytes), is(false));
			
			// 同じ日付けオブジェクトを渡した場合、trueが返却されること。
			byte[] bytes1 = {1,2,3};
			byte[] bytes2 = {1,2,3};
			assertThat(page.sameBytes(bytes1, bytes2), is(true));
			
			bytes2[0] = 0;
			assertThat(page.sameBytes(bytes1, bytes2), is(false));
		} catch (BrowzingException e) {
			manager.finishTrunsaction();
			fail(e);
		}
		
	}
	
	@Test
	public void getFileId() throws DataStoreManagerException {
		DataStoreManager manager = getMysqlAccessableDataStoreManager();
		// ==========正常にファイルIDが返却されること==========
		try {
			RPage page = new RPage("http://ja.wikipedia.org/wiki/HyperText_Markup_Language", getMysqlAccessableDataStoreManager());
			assertEquals(page.getFileId(), "74782a262a7cdd3c38a52135793c29f5450f4722c125bdacdfc549a6194bc0d6");
		} catch (BrowzingException e) {
			manager.finishTrunsaction();
			fail(e);
		}
	}
}
