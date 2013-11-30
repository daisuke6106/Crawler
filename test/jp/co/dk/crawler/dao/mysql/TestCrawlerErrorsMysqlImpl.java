package jp.co.dk.crawler.dao.mysql;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.TestCrawlerFoundation;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.CrawlerErrors;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.Urls;
import jp.co.dk.crawler.dao.record.CrawlerErrorsRecord;
import jp.co.dk.crawler.dao.record.UrlsRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.message.CrawlerMessage;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.message.DataStoreManagerMessage;

import org.junit.Test;

public class TestCrawlerErrorsMysqlImpl extends TestCrawlerFoundation{

	@Test
	public void insert() throws DataStoreManagerException {
		
		DataStoreManager manager = getAccessableDataStoreManager();
		manager.startTrunsaction();
		CrawlerErrors crawlerErrors = (CrawlerErrors)manager.getDataAccessObject(CrawlerDaoConstants.CRAWLER_ERRORS);
		
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
			// パラメータマップ
			Map<String, String> parameter = new HashMap<String,String>();
			parameter.put("Parameter_key", "Parameter_value");
			
			// ファイル名
			String message  = "exception message";
			// コンテンツデータ
			Throwable exception = new CrawlerException(CrawlerMessage.DETASTORETYPE_IS_NOT_SUPPORT);
			StackTraceElement[] stackTraceElements = exception.getStackTrace();
			// 作成日時
			Date createDate = new Date();
			// 更新日時
			Date updateDate = new Date();
			
			// ========================================正常系========================================
			
			// 引数に正常値を渡した場合、正常に登録できること。
			try {
				// 登録処理を実行
				crawlerErrors.insert(protcol, host, path, parameter, message, stackTraceElements, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			}
			
			// ========================================異常系========================================
			// すでにレコードが存在する場合、異常が発生すること。
			try {
				// 登録処理を実行
				crawlerErrors.insert(protcol, host, path, parameter, message, stackTraceElements, createDate, updateDate);
				fail();
			} catch (DataStoreManagerException e) {
				assertEquals(e.getMessageObj(), DataStoreManagerMessage.FAILE_TO_EXECUTE_SQL);
			}
			
		} finally {
			manager.finishTrunsaction();
		}
	}
	
	@Test
	public void select() throws DataStoreManagerException {
		
		DataStoreManager manager = getAccessableDataStoreManager();
		manager.startTrunsaction();
		CrawlerErrors crawlerErrors = (CrawlerErrors)manager.getDataAccessObject(CrawlerDaoConstants.CRAWLER_ERRORS);
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
			
			// パラメータマップ
			Map<String, String> parameter = new HashMap<String,String>();
			parameter.put("Parameter_key", "Parameter_value");
			
			// ファイル名
			String message  = "exception message";
			// コンテンツデータ
			Throwable exception = new CrawlerException(CrawlerMessage.DETASTORETYPE_IS_NOT_SUPPORT);
			StackTraceElement[] stackTraceElements = exception.getStackTrace();
			// 作成日時
			Date createDate = new Date();
			// 更新日時
			Date updateDate = new Date();
			
			// 引数に正常値を渡した場合、正常に登録できること。
			try {
				// 登録処理を実行
				crawlerErrors.insert(protcol, host, path,  parameter, message, stackTraceElements, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			}
			
			// 引数に空のパスリスト、パラメータマップを渡した場合、正常に登録できること。
			try {
				// 登録処理を実行
				crawlerErrors.insert(protcol, host, new ArrayList<String>(), new HashMap<String, String>(), message, stackTraceElements, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			}
			
			// ========================================正常系========================================
			// 引数に正常値を渡した場合、正常に取得できること。
			try {
				// 登録処理を実行
				CrawlerErrorsRecord record = crawlerErrors.select(protcol, host, path,  parameter);
				assertEquals(record.getProtocol(), "http");
				assertEquals(record.getHost(), "google.com");
				assertEquals(record.getH_path(), path.hashCode());
				assertEquals(record.getH_parameter(), parameter.hashCode());
				assertEquals(record.getMessage(), message);
				assertEquals(record.getStackTraceElements(), stackTraceElements);
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getCreateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(createDate));
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getUpdateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(updateDate));
			} catch (DataStoreManagerException e) {
				fail(e);
			}
			
			// 引数に空のパスリスト、パラメータマップを渡した場合、正常に取得できること。
			try {
				// 登録処理を実行
				crawlerErrors.select(protcol, host, null, null);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			}
		} finally {
			manager.finishTrunsaction();
		}
	}
}
