package jp.co.dk.crawler.dao.mysql;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.TestCrawlerFoundation;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.Urls;
import jp.co.dk.crawler.dao.record.UrlsRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.message.CrawlerMessage;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.message.DataStoreManagerMessage;

import org.junit.Test;

public class TestUrlsMysqlImpl extends TestCrawlerFoundation{

	@Test
	public void createTable_dropTable() {
		try {
			Urls pages = new UrlsMysqlImpl(super.getAccessableDataBaseAccessParameter());
			pages.dropTable();
			pages.createTable();
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void insert() throws DataStoreManagerException {
		
		DataStoreManager manager = getAccessableDataStoreManager();
		manager.startTrunsaction();
		Urls pages = (Urls)manager.getDataAccessObject(CrawlerDaoConstants.URLS);
		
		// プロトコル
		String protcol   = "http";
		// ホスト名
		String host      = "google.com";
		// パスリスト
		List<String> path = new ArrayList<String>();
		path.add("doodles");
		path.add("finder");
		path.add("2013");
		path.add("All%20doodles");
		// ファイル名
		String filename = "filename.txt";
		// パラメータマップ
		Map<String, String> parameter = new HashMap<String,String>();
		parameter.put("Parameter_key", "Parameter_value");
		// URL
		String url = "http://google.com/doodles/finder/2013/All%20doodles?Parameter_key=Parameter_value";
		// ファイルID
		long fileid = 1234567890L;
		// 作成日時
		Date createDate = new Date();
		// 更新日時
		Date updateDate = new Date();
		
		// ========================================正常系========================================
		
		// 引数に正常値を渡した場合、正常に登録できること。
		try {
			// 登録処理を実行
			pages.insert(protcol, host, path, filename, parameter, url, fileid, createDate, updateDate);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// 引数に空のパスリスト、パラメータマップを渡した場合、正常に登録できること。
		try {
			// 登録処理を実行
			pages.insert(protcol, host, new ArrayList<String>(), "", new HashMap<String, String>(), url, fileid, createDate, updateDate);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// ========================================異常系========================================
		// すでにレコードが存在する場合、異常が発生すること。
		try {
			// 登録処理を実行
			pages.insert(protcol, host, path, filename, parameter, url, fileid, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.FAILE_TO_EXECUTE_SQL);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// プロトコル名がnullの場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert(null, host, path, filename, parameter, url, fileid, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		// ホスト名がnullの場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert(protcol, null, path, filename, parameter, url, fileid, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		// プロトコル名が空の場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert("", host, path, filename, parameter, url, fileid, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		// ホスト名が空の場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert(protcol, "", path, filename, parameter, url, fileid, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		manager.finishTrunsaction();
	}
	
	@Test
	public void select() throws DataStoreManagerException {
		
		DataStoreManager manager = getAccessableDataStoreManager();
		manager.startTrunsaction();
		Urls pages = (Urls)manager.getDataAccessObject(CrawlerDaoConstants.URLS);
		
		// プロトコル
		String protcol   = "http";
		// ホスト名
		String host      = "google.com";
		// パスリスト
		List<String> path = new ArrayList<String>();
		path.add("doodles");
		path.add("finder");
		path.add("2013");
		path.add("All%20doodles");
		// ファイル名
		String filename = "filename.txt";
		// パラメータマップ
		Map<String, String> parameter = new HashMap<String,String>();
		parameter.put("Parameter_key", "Parameter_value");
		// URL
		String url = "http://google.com/doodles/finder/2013/All%20doodles?Parameter_key=Parameter_value";
		// ファイルID
		long fileid = 1234567890L;
		// 作成日時
		Date createDate = new Date();
		// 更新日時
		Date updateDate = new Date();
		
		// 引数に正常値を渡した場合、正常に登録できること。
		try {
			// 登録処理を実行
			pages.insert(protcol, host, path, filename, parameter, url, fileid, createDate, updateDate);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// 引数に空のパスリスト、パラメータマップを渡した場合、正常に登録できること。
		try {
			// 登録処理を実行
			pages.insert(protcol, host, new ArrayList<String>(), "", new HashMap<String, String>(), url, fileid, createDate, updateDate);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// ========================================正常系========================================
		// 引数に正常値を渡した場合、正常に取得できること。
		try {
			// 登録処理を実行
			UrlsRecord record = pages.select(protcol, host, path, filename, parameter);
			assertEquals(record.getProtocol(), "http");
			assertEquals(record.getHost(), "google.com");
			assertEquals(record.getH_path(), path.hashCode());
			assertEquals(record.getH_filename(), filename.hashCode());
			assertEquals(record.getH_parameter(), parameter.hashCode());
			assertEquals(record.getUrl(), url);
			assertEquals(record.getFileId(), fileid);
			assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getCreateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(createDate));
			assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getUpdateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(updateDate));
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// 引数に空のパスリスト、パラメータマップを渡した場合、正常に取得できること。
		try {
			// 登録処理を実行
			pages.select(protcol, host, null, null, null);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		manager.finishTrunsaction();
	}
}
