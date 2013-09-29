package jp.co.dk.crawler.dao.mysql;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.TestCrawlerFoundation;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.record.PagesRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.message.CrawlerMessage;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.message.DataStoreManagerMessage;

import org.junit.Test;

public class TestPagesMysqlImpl extends TestCrawlerFoundation{

	@Test
	public void createTable_dropTable() {
		try {
			Pages pages = new PagesMysqlImpl(super.getAccessableDataBaseAccessParameter());
			pages.createTable();
			pages.dropTable();
		} catch (DataStoreManagerException e) {
			e.printStackTrace();
			fail(e);
		}
	}
	
	@Test
	public void insert() throws DataStoreManagerException {
		
		// ========================================準備========================================
		// テーブル作成
		Pages pages = new PagesMysqlImpl(super.getAccessableDataBaseAccessParameter());
		pages.createTable();
		
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
		// パラメータマップ
		Map<String, String> parameter = new HashMap<String,String>();
		parameter.put("Parameter_key", "Parameter_value");
		// リクエストヘッダ
		Map<String, String> requestHeader = new HashMap<String,String>();
		requestHeader.put("RequestHeader_key", "RequestHeader_value");
		// レスポンスヘッダ
		Map<String, String> responceHeader = new HashMap<String,String>();
		responceHeader.put("ResponceHeader_key", "ResponceHeader_value");
		// コンテンツデータ
		byte[] contents = {1,2,3};
		// 作成日時
		Date createDate = new Date();
		// 更新日時
		Date updateDate = new Date();
		
		// ========================================正常系========================================
		
		// 引数に正常値を渡した場合、正常に登録できること。
		try {
			// 登録処理を実行
			pages.insert(protcol, host, path, parameter, requestHeader, responceHeader, contents, createDate, updateDate);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// 引数に空のパスリスト、パラメータマップを渡した場合、正常に登録できること。
		try {
			// 登録処理を実行
			pages.insert(protcol, host, new ArrayList<String>(), new HashMap<String, String>(), requestHeader, responceHeader, contents, createDate, updateDate);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// ========================================異常系========================================
		// すでにレコードが存在する場合、異常が発生すること。
		try {
			// 登録処理を実行
			pages.insert(protcol, host, path, parameter, requestHeader, responceHeader, contents, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.FAILE_TO_EXECUTE_SQL);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// プロトコル名がnullの場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert(null, host, path, parameter, requestHeader, responceHeader, contents, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		// ホスト名がnullの場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert(protcol, null, path, parameter, requestHeader, responceHeader, contents, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		// プロトコル名が空の場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert("", host, path, parameter, requestHeader, responceHeader, contents, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		// ホスト名が空の場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert(protcol, "", path, parameter, requestHeader, responceHeader, contents, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		// ========================================後処理========================================
		pages.dropTable();
	}
	
	@Test
	public void select() throws DataStoreManagerException {
		
		// ========================================準備========================================
		// テーブル作成
		Pages pages = new PagesMysqlImpl(super.getAccessableDataBaseAccessParameter());
		pages.createTable();
		
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
		// パラメータマップ
		Map<String, String> parameter = new HashMap<String,String>();
		parameter.put("Parameter_key", "Parameter_value");
		// リクエストヘッダ
		Map<String, String> requestHeader = new HashMap<String,String>();
		requestHeader.put("RequestHeader_key", "RequestHeader_value");
		// レスポンスヘッダ
		Map<String, String> responceHeader = new HashMap<String,String>();
		responceHeader.put("ResponceHeader_key", "ResponceHeader_value");
		// コンテンツデータ
		byte[] contents = {1,2,3};
		// 作成日時
		Date createDate = new Date();
		// 更新日時
		Date updateDate = new Date();
		
		// 引数に正常値を渡した場合、正常に登録できること。
		try {
			// 登録処理を実行
			pages.insert(protcol, host, path, parameter, requestHeader, responceHeader, contents, createDate, updateDate);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// 引数に空のパスリスト、パラメータマップを渡した場合、正常に登録できること。
		try {
			// 登録処理を実行
			pages.insert(protcol, host, new ArrayList<String>(), new HashMap<String, String>(), requestHeader, responceHeader, contents, createDate, updateDate);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// ========================================正常系========================================
		// 引数に正常値を渡した場合、正常に取得できること。
		try {
			// 登録処理を実行
			PagesRecord record = pages.select(protcol, host, path, parameter);
			assertEquals(record.getProtocol(), "http");
			assertEquals(record.getHost(), "google.com");
			assertEquals(record.getH_path(), path.hashCode());
			assertEquals(record.getH_parameter(), parameter.hashCode());
			assertEquals(record.getPath(), path);
			assertEquals(record.getParameter(), parameter);
			assertEquals(record.getRequestHeader(), requestHeader);
			assertEquals(record.getResponceHeader(), responceHeader);
			assertEquals(record.getContents().length, contents.length);
			for (int i=0; i<contents.length; i++) {
				assertEquals(record.getContents()[i], contents[i]);
			}
			assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getCreateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(createDate));
			assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getUpdateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(updateDate));
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// 引数に空のパスリスト、パラメータマップを渡した場合、正常に取得できること。
		try {
			// 登録処理を実行
			PagesRecord record = pages.select(protcol, host, null, null);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// ========================================後処理========================================
		pages.dropTable();
	}
}
