package jp.co.dk.crawler.dao.mysql;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.CrawlerFoundationTest;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.Links;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.record.PagesRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.message.CrawlerMessage;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.message.DataStoreManagerMessage;

import org.junit.Test;

public class PagesMysqlImplTest extends CrawlerFoundationTest{

	@Test
	public void insert() throws DataStoreManagerException {
		
		DataStoreManager manager = getAccessableDataStoreManager();
		manager.startTrunsaction();
		
		Pages pages = (Pages)manager.getDataAccessObject(CrawlerDaoConstants.PAGES);
		
		try {
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
			// リクエストヘッダ
			Map<String, String> requestHeader = new HashMap<String,String>();
			requestHeader.put("RequestHeader_key", "RequestHeader_value");
			// レスポンスヘッダ
			Map<String, List<String>> responceHeader = new HashMap<String,List<String>>();
			List<String> responseHeaderValue = new ArrayList<String>();
			responseHeaderValue.add("ResponceHeader_value");
			responceHeader.put("ResponceHeader_key", responseHeaderValue);
			// HTTPステータスコード
			String httpStutasCode = "200";
			// HTTPバージョン
			String httpVersion = "1.1";
			// ファイルＩＤ
			long fileid = 1234567890L;
			// タイムID
			long timeid = new Date().getTime();
			// 作成日時
			Date createDate = new Date();
			// 更新日時
			Date updateDate = new Date();
			
			// ========================================正常系========================================
			
			// 引数に正常値を渡した場合、正常に登録できること。
			try {
				// 登録処理を実行
				pages.insert(protcol, host, path,  parameter, requestHeader, responceHeader, httpStutasCode, httpVersion, fileid, timeid, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			} catch (CrawlerException e) {
				fail(e);
			}
			
			// 引数に空のパスリスト、パラメータマップを渡した場合、正常に登録できること。
			try {
				// 登録処理を実行
				pages.insert(protcol, host, new ArrayList<String>(), new HashMap<String, String>(), requestHeader, responceHeader, httpStutasCode, httpVersion, fileid, timeid, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			} catch (CrawlerException e) {
				fail(e);
			}
			
			// ========================================異常系========================================
			// すでにレコードが存在する場合、異常が発生すること。
			try {
				// 登録処理を実行
				pages.insert(protcol, host, path,  parameter, requestHeader, responceHeader, httpStutasCode, httpVersion, fileid, timeid, createDate, updateDate);
				fail();
			} catch (DataStoreManagerException e) {
				assertEquals(e.getMessageObj(), DataStoreManagerMessage.FAILE_TO_EXECUTE_SQL);
			} catch (CrawlerException e) {
				fail(e);
			}
			
			// プロトコル名がnullの場合、例外が送出されること。
			try {
				// 登録処理を実行
				pages.insert(null, host, path,  parameter, requestHeader, responceHeader, httpStutasCode, httpVersion, fileid, timeid, createDate, updateDate);
				fail();
			} catch (DataStoreManagerException e) {
				fail(e);
			} catch (CrawlerException e) {
				assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
			}
			
			// ホスト名がnullの場合、例外が送出されること。
			try {
				// 登録処理を実行
				pages.insert(protcol, null, path,  parameter, requestHeader, responceHeader, httpStutasCode, httpVersion, fileid, timeid, createDate, updateDate);
				fail();
			} catch (DataStoreManagerException e) {
				fail(e);
			} catch (CrawlerException e) {
				assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
			}
			
			// プロトコル名が空の場合、例外が送出されること。
			try {
				// 登録処理を実行
				pages.insert("", host, path,  parameter, requestHeader, responceHeader, httpStutasCode, httpVersion, fileid, timeid, createDate, updateDate);
				fail();
			} catch (DataStoreManagerException e) {
				fail(e);
			} catch (CrawlerException e) {
				assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
			}
			
			// ホスト名が空の場合、例外が送出されること。
			try {
				// 登録処理を実行
				pages.insert(protcol, "", path,  parameter, requestHeader, responceHeader, httpStutasCode, httpVersion, fileid, timeid, createDate, updateDate);
				fail();
			} catch (DataStoreManagerException e) {
				fail(e);
			} catch (CrawlerException e) {
				assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
			}
		} finally {
			manager.finishTrunsaction();
		}
	}
	
	@Test
	public void select() throws DataStoreManagerException {
		
		DataStoreManager manager = getAccessableDataStoreManager();
		manager.startTrunsaction();
		
		Pages pages = (Pages)manager.getDataAccessObject(CrawlerDaoConstants.PAGES);
		try {
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
			// リクエストヘッダ
			Map<String, String> requestHeader = new HashMap<String,String>();
			requestHeader.put("RequestHeader_key", "RequestHeader_value");
			// レスポンスヘッダ
			Map<String, List<String>> responceHeader = new HashMap<String,List<String>>();
			List<String> responseHeaderValue = new ArrayList<String>();
			responseHeaderValue.add("ResponceHeader_value");
			responceHeader.put("ResponceHeader_key", responseHeaderValue);
			// HTTPステータスコード
			String httpStutasCode = "200";
			// HTTPバージョン
			String httpVersion = "1.1";
			// ファイルＩＤ
			long fileid = 1234567890L;
			// タイムID
			long timeid = new Date().getTime();
			// 作成日時
			Date createDate = new Date();
			// 更新日時
			Date updateDate = new Date();
			
			// 引数に正常値を渡した場合、正常に登録できること。
			try {
				// 登録処理を実行
				pages.insert(protcol, host, path,  parameter, requestHeader, responceHeader, httpStutasCode, httpVersion, fileid, timeid, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			} catch (CrawlerException e) {
				fail(e);
			}
			
			// 引数に空のパスリスト、パラメータマップを渡した場合、正常に登録できること。
			try {
				// 登録処理を実行
				pages.insert(protcol, host, new ArrayList<String>(), new HashMap<String, String>(), requestHeader, responceHeader, httpStutasCode, httpVersion, fileid, timeid, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			} catch (CrawlerException e) {
				fail(e);
			}
			
			// ========================================正常系========================================
			// 引数に正常値を渡した場合、正常に取得できること。
			try {
				// 登録処理を実行
				PagesRecord record = pages.select(protcol, host, path,  parameter, fileid, timeid);
				assertEquals(record.getProtocol(), "http");
				assertEquals(record.getHost(), "google.com");
				assertEquals(record.getH_path(), path.hashCode());
				assertEquals(record.getH_parameter(), parameter.hashCode());
				assertEquals(record.getPath(), path);
				assertEquals(record.getPathCount(), path.size());
				assertEquals(record.getParameter(), parameter);
				assertEquals(record.getParameterCount(), parameter.size());
				assertEquals(record.getRequestHeader(), requestHeader);
				assertEquals(record.getResponceHeader(), responceHeader);
				assertEquals(record.getFileId(), fileid);
				assertEquals(record.getTimeId(), timeid);
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getCreateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(createDate));
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getUpdateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(updateDate));
			} catch (DataStoreManagerException e) {
				fail(e);
			} catch (CrawlerException e) {
				fail(e);
			}
			
			// 引数に正常値を渡した場合、正常に取得できること。
			try {
				// 登録処理を実行
				List<PagesRecord> records = pages.select(protcol, host, path,  parameter);
				assertEquals(records.size(), 1);
				assertEquals(records.get(0).getProtocol(), "http");
				assertEquals(records.get(0).getHost(), "google.com");
				assertEquals(records.get(0).getH_path(), path.hashCode());
				assertEquals(records.get(0).getH_parameter(), parameter.hashCode());
				assertEquals(records.get(0).getPath(), path);
				assertEquals(records.get(0).getPathCount(), path.size());
				assertEquals(records.get(0).getParameter(), parameter);
				assertEquals(records.get(0).getParameterCount(), parameter.size());
				assertEquals(records.get(0).getRequestHeader(), requestHeader);
				assertEquals(records.get(0).getResponceHeader(), responceHeader);
				assertEquals(records.get(0).getHttpStatusCode(), httpStutasCode);
				assertEquals(records.get(0).getHttpVersion(), httpVersion);
				assertEquals(records.get(0).getFileId(), fileid);
				assertEquals(records.get(0).getTimeId(), timeid);
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(records.get(0).getCreateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(createDate));
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(records.get(0).getUpdateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(updateDate));
			} catch (DataStoreManagerException e) {
				fail(e);
			} catch (CrawlerException e) {
				fail(e);
			}
			
			// 引数に空のパスリスト、パラメータマップを渡した場合、正常に取得できること。
			try {
				// 登録処理を実行
				pages.select(protcol, host, null, null);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			}
		} finally {
			manager.finishTrunsaction();
		}
	}
	
	
	@Test
	public void count() throws DataStoreManagerException {
		
		DataStoreManager manager = getAccessableDataStoreManager();
		manager.startTrunsaction();
		
		Pages pages = (Pages)manager.getDataAccessObject(CrawlerDaoConstants.PAGES);
		
		try {
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
			// リクエストヘッダ
			Map<String, String> requestHeader = new HashMap<String,String>();
			requestHeader.put("RequestHeader_key", "RequestHeader_value");
			// レスポンスヘッダ
			Map<String, List<String>> responceHeader = new HashMap<String,List<String>>();
			List<String> responseHeaderValue = new ArrayList<String>();
			responseHeaderValue.add("ResponceHeader_value");
			responceHeader.put("ResponceHeader_key", responseHeaderValue);
			// HTTPステータスコード
			String httpStutasCode = "200";
			// HTTPバージョン
			String httpVersion = "1.1";
			// ファイルＩＤ
			long fileid = 1234567890L;
			// タイムID
			long timeid = new Date().getTime();
			// 作成日時
			Date createDate = new Date();
			// 更新日時
			Date updateDate = new Date();
			
			// 引数に正常値を渡した場合、正常に登録できること。
			try {
				// 登録処理を実行
				pages.insert(protcol, host, path,  parameter, requestHeader, responceHeader, httpStutasCode, httpVersion, fileid, timeid, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			} catch (CrawlerException e) {
				fail(e);
			}
			
			// 引数に空のパスリスト、パラメータマップを渡した場合、正常に登録できること。
			try {
				// 登録処理を実行
				pages.insert(protcol, host, new ArrayList<String>(), new HashMap<String, String>(), requestHeader, responceHeader, httpStutasCode, httpVersion, fileid, timeid, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			} catch (CrawlerException e) {
				fail(e);
			}
			
			// ========================================正常系========================================
			// 引数に正常値を渡した場合、正常に取得できること。
			try {
				// 登録処理を実行
				int count = pages.count(protcol, host, path,  parameter);
				assertEquals(count, 1);
			} catch (DataStoreManagerException e) {
				fail(e);
			}
			// 引数に空のパスリスト、パラメータマップを渡した場合、正常に取得できること。
			try {
				// 登録処理を実行
				int count = pages.count(protcol, host, null, null);
				assertEquals(count, 1);
			} catch (DataStoreManagerException e) {
				fail(e);
			}
		
		} finally {
			manager.finishTrunsaction();
		}
	}
}
