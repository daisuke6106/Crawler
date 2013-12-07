package jp.co.dk.crawler.dao.mysql;

import java.util.Date;

import jp.co.dk.crawler.TestCrawlerFoundation;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.RedirectErrors;
import jp.co.dk.crawler.dao.record.RedirectErrorsRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.message.CrawlerMessage;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.message.DataStoreManagerMessage;

import org.junit.Test;

public class TestRedirectErrorsMysqlImpl extends TestCrawlerFoundation{

	@Test
	public void insert() throws DataStoreManagerException {
		
		// ========================================準備========================================
		DataStoreManager manager = getAccessableDataStoreManager();
		manager.startTrunsaction();
		RedirectErrors errors = (RedirectErrors)manager.getDataAccessObject(CrawlerDaoConstants.REDIRECT_ERRORS);
		
		try {
			//ファイルID
			long fileid    = 1234567890L;
			//タイムID
			long timeid    = new Date().getTime();
			// ファイル名
			String message  = "例外メッセージ本文";
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
				errors.insert(fileid, timeid, message, stackTraceElements, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			}
			
			// ========================================異常系========================================
			// すでにレコードが存在する場合、異常が発生すること。
			try {
				// 登録処理を実行
				errors.insert(fileid, timeid, message, stackTraceElements, createDate, updateDate);
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
		RedirectErrors errors = (RedirectErrors)manager.getDataAccessObject(CrawlerDaoConstants.REDIRECT_ERRORS);
		
		try {
			//ファイルID
			long fileid    = 1234567890L;
			//タイムID
			long timeid    = new Date().getTime();
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
				errors.insert(fileid, timeid, message, stackTraceElements, createDate, updateDate);
				// PK以外NULLを設定した場合、正常に登録されること
				errors.insert(fileid+1, timeid+1, null, null, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			}
			
			// ========================================正常系========================================
			// 引数に正常値を渡した場合、正常に取得できること。
			try {
				RedirectErrorsRecord record = errors.select(fileid, timeid);
				assertEquals(record.getFileId(), fileid);
				assertEquals(record.getTimeId(), timeid);
				assertEquals(record.getMessage(), message);
				assertEquals(record.getStackTraceElements(), stackTraceElements);
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getCreateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(createDate));
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getUpdateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(updateDate));
				
				RedirectErrorsRecord record1 = errors.select(fileid+1, timeid+1);
				assertEquals(record1.getFileId(), fileid+1);
				assertEquals(record1.getTimeId(), timeid+1);
				assertNull(record1.getMessage());
				assertNull(record1.getStackTraceElements());
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getCreateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(createDate));
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getUpdateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(updateDate));
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
		RedirectErrors errors = (RedirectErrors)manager.getDataAccessObject(CrawlerDaoConstants.REDIRECT_ERRORS);
		
		try {
			//ファイルID
			long fileid    = 1234567890L;
			//タイムID
			long timeid    = new Date().getTime();
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
			
			// 登録していない場合０件が返却されること
			assertEquals(errors.count(fileid, timeid), 0);
			assertEquals(errors.count(fileid+1, timeid+1), 0);
			
			// 引数に正常値を渡した場合、正常に登録できること。
			try {
				// 登録処理を実行
				errors.insert(fileid, timeid, message, stackTraceElements, createDate, updateDate);
				// PK以外NULLを設定した場合、正常に登録されること
				errors.insert(fileid+1, timeid+1, null, null, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			}
			
			// ========================================正常系========================================
			// 引数に正常値を渡した場合、正常に取得できること。
			try {
				assertEquals(errors.count(fileid, timeid), 1);
				assertEquals(errors.count(fileid+1, timeid+1), 1);
			} catch (DataStoreManagerException e) {
				fail(e);
			}
		} finally {
			manager.finishTrunsaction();
		}
	}
	
}
