package jp.co.dk.crawler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageHeaderImproperException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.Documents;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.Urls;
import jp.co.dk.crawler.dao.record.DocumentsRecord;
import jp.co.dk.crawler.dao.record.PagesRecord;
import jp.co.dk.crawler.dao.record.UrlsRecord;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

import org.junit.Test;

public class PageTest extends CrawlerFoundationTest{

	@Test
	public void constactor() throws DataStoreManagerException {
		DataStoreManager manager = getAccessableDataStoreManager();
		try {
			Page page = new Page("http://www.google.com", manager);
		} catch (BrowzingException e) {
			manager.finishTrunsaction();
			fail(e);
		}
	}
	
	@Test
	public void save() throws DataStoreManagerException {
		DataStoreManager manager = getAccessableDataStoreManager();
		try {
			// ＝＝＝＝＝＝＝＝＝＝トランザクション開始＝＝＝＝＝＝＝＝＝＝
			manager.startTrunsaction();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			Page page = new Page("http://ja.wikipedia.org/wiki/HyperText_Markup_Language", manager);
			page.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			String             protocol  = page.getProtocol();
			String             host      = page.getHost();
			List<String>       pathList  = page.getPathList();
			Map<String,String> parameter = page.getParameter();
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
		DataStoreManager manager = getAccessableDataStoreManager();
		try {
			// ＝＝＝＝＝＝＝＝＝＝トランザクション開始＝＝＝＝＝＝＝＝＝＝
			manager.startTrunsaction();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			Page page1 = new Page("http://www.google.com", manager);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertEquals(page1.getCount(), 0);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			Page page2 = new Page("http://www.ugtop.com/spill.shtml", manager);
			page2.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertEquals(page2.getCount(), 1);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			Page page3 = new Page("http://www.google.com", manager);
			page3.save();
			page3.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertEquals(page3.getCount(), 1);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			Page page4 = new Page("http://gigazine.net/news/20131103-four-elements-of-company-building/", manager);
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
		DataStoreManager manager = getAccessableDataStoreManager();
		try {
			// ＝＝＝＝＝＝＝＝＝＝トランザクション開始＝＝＝＝＝＝＝＝＝＝
			manager.startTrunsaction();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			Page page1 = new Page("http://www.google.com", manager);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertThat(page1.isSaved(), is(false));
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			Page page2 = new Page("http://www.htmq.com/html/head.shtml", manager);
			page2.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertThat(page2.isSaved(), is(true));
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			Page page3 = new Page("http://www.google.com", manager);
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
		DataStoreManager manager = getAccessableDataStoreManager();
		try {
			// ＝＝＝＝＝＝＝＝＝＝トランザクション開始＝＝＝＝＝＝＝＝＝＝
			manager.startTrunsaction();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			Page page1 = new Page("http://www.google.com", manager);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertThat(page1.isLatest(), is(false));
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			// 最終更新日付けが設定されていないページの場合
			Page page2 = new Page("http://www.htmq.com/html/head.shtml", manager);
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
			Page page3 = new Page("http://www.google.com", manager);
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
		DataStoreManager manager = getAccessableDataStoreManager();
		try {
			Page page = new Page("http://www.google.com", getAccessableDataStoreManager());
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
		DataStoreManager manager = getAccessableDataStoreManager();
		try {
			Page page = new Page("http://www.google.com", getAccessableDataStoreManager());
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
		DataStoreManager manager = getAccessableDataStoreManager();
		// ==========正常にファイルIDが返却されること==========
		// プロトコル＋ホスト名
		try {
			Page page = new Page("http://www.google.com", getAccessableDataStoreManager());
			assertEquals(page.getFileId(), "51cbb9cc73a2dda8a4bd2dab5087bbe8b0af3d3c04f0f636e4a1144d1b4d0305");
		} catch (BrowzingException e) {
			manager.finishTrunsaction();
			fail(e);
		}
		
		// プロトコル＋ホスト名＋パス
		try {
			Page page = new Page("http://www.google.com/doodles/about", getAccessableDataStoreManager());
			
			// 比較値を生成
			String protocol              = "http";
			String host                  = "www.google.com";
			List<String> pathList        = new ArrayList<String>();
			pathList.add("doodles");
			pathList.add("about");
			String filename              = "index.html";
			Map<String,String> parameter = new ParameterMap();
			BigDecimal result = new BigDecimal(protocol.hashCode());
			result = result.multiply(new BigDecimal(host.hashCode()));
			result = result.multiply(new BigDecimal(pathList.hashCode()));
			result = result.multiply(new BigDecimal(filename.hashCode()));
			result = result.multiply(new BigDecimal(parameter.hashCode()));
			
			assertEquals(page.getFileId(), result.longValue());
		} catch (BrowzingException e) {
			manager.finishTrunsaction();
			fail(e);
		}
		
		// プロトコル＋ホスト名＋パス＋ファイル名
		try {
			Page page = new Page("http://www.atmarkit.co.jp/ait/articles/1310/09/news127.html", getAccessableDataStoreManager());
			
			// 比較値を生成
			String protocol              = "http";
			String host                  = "www.atmarkit.co.jp";
			List<String> pathList        = new ArrayList<String>();
			pathList.add("ait");
			pathList.add("articles");
			pathList.add("1310");
			pathList.add("09");
			pathList.add("news127.html");
			String filename              = "news127.html";
			Map<String,String> parameter = new ParameterMap();
			BigDecimal result = new BigDecimal(protocol.hashCode());
			result = result.multiply(new BigDecimal(host.hashCode()));
			result = result.multiply(new BigDecimal(pathList.hashCode()));
			result = result.multiply(new BigDecimal(filename.hashCode()));
			result = result.multiply(new BigDecimal(parameter.hashCode()));
			
			assertEquals(page.getFileId(), result.longValue());
		} catch (BrowzingException e) {
			manager.finishTrunsaction();
			fail(e);
		}
		
		// プロトコル＋ホスト名＋パラメータ
		try {
			Page page = new Page("https://www.google.co.jp/?gws_rd=cr&ei=XVplUvTHH86FkwXcvoCACQ#q=test", getAccessableDataStoreManager());
			
			// 比較値を生成
			String protocol              = "https";
			String host                  = "www.google.co.jp";
			List<String> pathList        = new ArrayList<String>();
			String filename              = "index.html";
			Map<String,String> parameter = new ParameterMap();
			parameter.put("gws_rd", "cr");
			parameter.put("ei", "XVplUvTHH86FkwXcvoCACQ");
			BigDecimal result = new BigDecimal(protocol.hashCode());
			result = result.multiply(new BigDecimal(host.hashCode()));
			result = result.multiply(new BigDecimal(pathList.hashCode()));
			result = result.multiply(new BigDecimal(filename.hashCode()));
			result = result.multiply(new BigDecimal(parameter.hashCode()));
			
			assertEquals(page.getFileId(), result.longValue());
		} catch (BrowzingException e) {
			manager.finishTrunsaction();
			fail(e);
		}
		
		// プロトコル＋ホスト名＋パス＋パラメータ
		try {
			Page page = new Page("http://dailynews.yahoo.co.jp/fc/domestic/typhoons/?id=6094644", getAccessableDataStoreManager());
			
			// 比較値を生成
			String protocol              = "http";
			String host                  = "dailynews.yahoo.co.jp";
			List<String> pathList        = new ArrayList<String>();
			pathList.add("fc");
			pathList.add("domestic");
			pathList.add("typhoons");
			String filename              = "index.html";
			Map<String,String> parameter = new ParameterMap();
			parameter.put("id", "6094644");
			BigDecimal result = new BigDecimal(protocol.hashCode());
			result = result.multiply(new BigDecimal(host.hashCode()));
			result = result.multiply(new BigDecimal(pathList.hashCode()));
			result = result.multiply(new BigDecimal(filename.hashCode()));
			result = result.multiply(new BigDecimal(parameter.hashCode()));
			
			assertEquals(page.getFileId(), result.longValue());
		} catch (BrowzingException e) {
			manager.finishTrunsaction();
			fail(e);
		}
	}
}
