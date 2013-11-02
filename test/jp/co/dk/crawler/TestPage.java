package jp.co.dk.crawler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.Documents;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.Urls;
import jp.co.dk.crawler.dao.record.DocumentsRecord;
import jp.co.dk.crawler.dao.record.PagesRecord;
import jp.co.dk.crawler.dao.record.UrlsRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

import org.junit.Test;

public class TestPage extends TestCrawlerFoundation{

	@Test
	public void constactor() {
		try {
			Page page = new Page("http://www.google.com", getAccessableDataStoreManager());
		} catch (BrowzingException e) {
			fail(e);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void save() {
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
			FakePage page = new FakePage("http://www.google.com", getAccessableDataStoreManager());
			page.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			String             protocol  = page.getProtocol();
			String             host      = page.getHost();
			List<String>       pathList  = page.getPathList();
			String             filename  = page.getFileName();
			Map<String,String> parameter = page.getParameter();
			
			long               fileId    = page.getFileId();
			long               timeId    = page.getTimeId();
			
			manager.startTrunsaction();
			UrlsRecord      urlRecord       = urls.select(protocol, host, pathList, filename, parameter);
			PagesRecord     pagesRecord     = pages.select(protocol, host, pathList, filename, parameter, fileId, timeId);
			DocumentsRecord documentsRecord = documents.select(fileId, timeId);
			manager.finishTrunsaction();
			
		} catch (BrowzingException e) {
			fail(e);
		} catch (DataStoreManagerException e) {
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
	
	@Test
	public void getCount() {
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
			Page page1 = new Page("http://www.google.com", getAccessableDataStoreManager());
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertEquals(page1.getCount(), 0);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			Page page2 = new Page("http://www.studyinghttp.net/header", getAccessableDataStoreManager());
			page2.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertEquals(page2.getCount(), 1);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			Page page3 = new Page("http://www.google.com", getAccessableDataStoreManager());
			page3.save();
			page3.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertEquals(page3.getCount(), 2);
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
		} catch (BrowzingException e) {
			fail(e);
		} catch (DataStoreManagerException e) {
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
	
	@Test
	public void isSaved() {
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
			Page page1 = new Page("http://www.google.com", getAccessableDataStoreManager());
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertThat(page1.isSaved(), is(false));
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			Page page2 = new Page("http://www.studyinghttp.net/header", getAccessableDataStoreManager());
			page2.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertThat(page2.isSaved(), is(true));
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト実行
			Page page3 = new Page("http://www.google.com", getAccessableDataStoreManager());
			page3.save();
			page3.save();
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
			// テスト結果確認
			assertThat(page3.isSaved(), is(true));
			// ＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝＝
		} catch (BrowzingException e) {
			fail(e);
		} catch (DataStoreManagerException e) {
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
	
	@Test
	public void getFileId() {
		
		// ==========正常にファイルIDが返却されること==========
		// プロトコル＋ホスト名
		try {
			Page page = new Page("http://www.google.com", getAccessableDataStoreManager());
			
			// 比較値を生成
			String protocol              = "http";
			String host                  = "www.google.com";
			List<String> pathList        = new ArrayList<String>();
			String filename              = "default.html";
			Map<String,String> parameter = new ParameterMap();
			BigDecimal result = new BigDecimal(protocol.hashCode());
			result = result.multiply(new BigDecimal(host.hashCode()));
			result = result.multiply(new BigDecimal(pathList.hashCode()));
			result = result.multiply(new BigDecimal(filename.hashCode()));
			result = result.multiply(new BigDecimal(parameter.hashCode()));
			
			assertEquals(page.getFileId(), result.longValue());
		} catch (BrowzingException e) {
			fail(e);
		} catch (DataStoreManagerException e) {
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
			String filename              = "default.html";
			Map<String,String> parameter = new ParameterMap();
			BigDecimal result = new BigDecimal(protocol.hashCode());
			result = result.multiply(new BigDecimal(host.hashCode()));
			result = result.multiply(new BigDecimal(pathList.hashCode()));
			result = result.multiply(new BigDecimal(filename.hashCode()));
			result = result.multiply(new BigDecimal(parameter.hashCode()));
			
			assertEquals(page.getFileId(), result.longValue());
		} catch (BrowzingException e) {
			fail(e);
		} catch (DataStoreManagerException e) {
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
			fail(e);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// プロトコル＋ホスト名＋パラメータ
		try {
			Page page = new Page("https://www.google.co.jp/?gws_rd=cr&ei=XVplUvTHH86FkwXcvoCACQ#q=test", getAccessableDataStoreManager());
			
			// 比較値を生成
			String protocol              = "https";
			String host                  = "www.google.co.jp";
			List<String> pathList        = new ArrayList<String>();
			String filename              = "default.html";
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
			fail(e);
		} catch (DataStoreManagerException e) {
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
			String filename              = "default.html";
			Map<String,String> parameter = new ParameterMap();
			parameter.put("id", "6094644");
			BigDecimal result = new BigDecimal(protocol.hashCode());
			result = result.multiply(new BigDecimal(host.hashCode()));
			result = result.multiply(new BigDecimal(pathList.hashCode()));
			result = result.multiply(new BigDecimal(filename.hashCode()));
			result = result.multiply(new BigDecimal(parameter.hashCode()));
			
			assertEquals(page.getFileId(), result.longValue());
		} catch (BrowzingException e) {
			fail(e);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
}


class FakePage extends Page {
	
	long timeId;
	
	public FakePage(String url, DataStoreManager dataStoreManager) throws BrowzingException {
		super(url, dataStoreManager);
		this.timeId = new Date().getTime();
	}
	
	public long getTimeId() {
		return timeId;
	}
}