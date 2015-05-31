package jp.co.dk.crawler.dao.mysql;

import java.text.ParseException;
import java.util.Date;

import jp.co.dk.crawler.CrawlerFoundationTest;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.Documents;
import jp.co.dk.crawler.dao.record.DocumentsRecord;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.message.DataStoreManagerMessage;

import org.junit.Test;

public class DocumentsMysqlImplTest extends CrawlerFoundationTest{

	@Test
	public void createTable_dropTable() {
		try {
			Documents documents = new DocumentsMysqlImpl(super.getAccessableDataBaseAccessParameter());
			documents.dropTable();
			documents.createTable();
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void insert() {
		
		DataStoreManager manager = getAccessableDataStoreManager();
		try {
			manager.startTrunsaction();
			Documents documents = (Documents)manager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
			
			//ファイルID
			String fileid    = "12345678901234567890123456789012345678901234567890" + "12345678901234";
			//タイムID
			long timeid    = new Date().getTime();
			// ファイル名
			String filename = "filename.txt";
			// 拡張子
			String extention = "txt";
			// 最終更新日時
			Date lastUpdateDate = new Date();
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
				documents.insert(fileid, timeid, filename, extention, lastUpdateDate, contents, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			}
			
			// ========================================異常系========================================
			// すでにレコードが存在する場合、異常が発生すること。
			try {
				// 登録処理を実行
				documents.insert(fileid, timeid, filename, extention, lastUpdateDate, contents, createDate, updateDate);
				fail();
			} catch (DataStoreManagerException e) {
				assertEquals(e.getMessageObj(), DataStoreManagerMessage.FAILE_TO_EXECUTE_SQL);
			}
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} finally {
			try {
				manager.finishTrunsaction();
			} catch (DataStoreManagerException e) {
				manager = null;
			}
		}
		
	}
	
	@Test
	public void select() {
		DataStoreManager manager = getAccessableDataStoreManager();
		try {
			manager.startTrunsaction();
		
			// ========================================準備========================================
			// テーブル作成
			Documents documents = (Documents)manager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
			
			//ファイルID
			String fileid    = "12345678901234567890123456789012345678901234567890" + "12345678901234";
			//タイムID
			long timeid    = new Date().getTime();
			// ファイル名
			String filename = "filename.txt";
			// 拡張子
			String extention = "txt";
			// 最終更新日時
			Date lastUpdateDate = new Date();
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
				documents.insert(fileid, timeid, filename, extention, lastUpdateDate, contents, createDate, updateDate);
				// PK以外NULLを設定した場合、正常に登録されること
				documents.insert(fileid, timeid+1, filename, extention, null, null, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			}
			
			// ========================================正常系========================================
			// 引数に正常値を渡した場合、正常に取得できること。
			try {
				DocumentsRecord record = documents.select(fileid, timeid);
				assertEquals(record.getFileId(), fileid);
				assertEquals(record.getFilename(), filename);
				assertEquals(record.getExtention(), extention);
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getLastUpdateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(lastUpdateDate));
				for (int i=0; i<contents.length; i++) {
					assertEquals(record.getData()[i], contents[i]);
				}
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getCreateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(createDate));
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getUpdateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(updateDate));
				
				
				
				DocumentsRecord record1 = documents.select(fileid, timeid+1);
				assertEquals(record1.getFileId(), fileid);
				assertEquals(record1.getFilename(), filename);
				assertEquals(record1.getExtention(), extention);
				assertNull(record1.getLastUpdateDate());
				assertNull(record1.getData());
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getCreateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(createDate));
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getUpdateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(updateDate));
			} catch (DataStoreManagerException e) {
				fail(e);
			}
		
		} catch (DataStoreManagerException e) {
			fail(e);
		} finally {
			try {
				manager.finishTrunsaction();
			} catch (DataStoreManagerException e) {
				manager = null;
			}
		}
		
	}
	
	@Test
	public void count() {
		
		DataStoreManager manager = getAccessableDataStoreManager();
		try {
			manager.startTrunsaction();
			
			// ========================================準備========================================
			// テーブル作成
			Documents documents = (Documents)manager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
			
			//ファイルID
			String fileid    = "12345678901234567890123456789012345678901234567890" + "12345678901234";
			//タイムID
			long timeid    = new Date().getTime();
			// ファイル名
			String filename = "filename.txt";
			// 拡張子
			String extention = "txt";
			// 最終更新日時
			Date lastUpdateDate = new Date();
			// コンテンツデータ
			byte[] contents = {1,2,3};
			// 作成日時
			Date createDate = new Date();
			// 更新日時
			Date updateDate = new Date();
			
			// ========================================正常系========================================
			
			// 登録されていない場合、レコードが取得できないこと。
			assertEquals(documents.count(fileid, timeid), 0);
			assertEquals(documents.count(fileid+1, timeid+1), 0);
			
			// 引数に正常値を渡した場合、正常に登録できること。
			try {
				// 登録処理を実行
				documents.insert(fileid, timeid, filename, extention, lastUpdateDate, contents, createDate, updateDate);
				// PK以外NULLを設定した場合、正常に登録されること
				documents.insert(fileid, timeid+1, filename, extention, null, null, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			}
			
			// ========================================正常系========================================
			// 引数に正常値を渡した場合、正常に取得できること。
			try {
				// 登録されていない場合、レコードが取得できないこと。
				assertEquals(documents.count(fileid, timeid), 1);
				assertEquals(documents.count(fileid, timeid+1), 1);
			} catch (DataStoreManagerException e) {
				fail(e);
			}
		
		} catch (DataStoreManagerException e) {
			fail(e);
		} finally {
			try {
				manager.finishTrunsaction();
			} catch (DataStoreManagerException e) {
				manager = null;
			}
		}
		
	}
	
	@Test
	public void selectLastest() throws DataStoreManagerException, ParseException {
		
		DataStoreManager manager = getAccessableDataStoreManager();
		try {
			manager.startTrunsaction();
		
			// ========================================準備========================================
			// テーブル作成
			Documents documents = (Documents)manager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
			
			//ファイルID
			String fileid    = "12345678901234567890123456789012345678901234567890" + "12345678901234";
			//タイムID
			long timeid1    = super.createDateByString("20131001120101").getTime();
			long timeid2    = super.createDateByString("20131001120102").getTime();
			// ファイル名
			String filename = "filename.txt";
			// 拡張子
			String extention = "txt";
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
				documents.insert(fileid, timeid1, filename, extention, new Date(timeid1), contents, createDate, updateDate);
				documents.insert(fileid, timeid2, filename, extention, new Date(timeid2), contents, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			}
			
			// ========================================正常系========================================
			// 引数に正常値を渡した場合、正常に取得できること。
			try {
				DocumentsRecord record = documents.selectLastest(fileid);
				assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getLastUpdateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(new Date(timeid2)));
			} catch (DataStoreManagerException e) {
				fail(e);
			}
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} finally {
			try {
				manager.finishTrunsaction();
			} catch (DataStoreManagerException e) {
				manager = null;
			}
		}
	
	}
}
