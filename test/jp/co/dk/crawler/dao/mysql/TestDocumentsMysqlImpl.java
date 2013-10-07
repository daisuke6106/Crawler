package jp.co.dk.crawler.dao.mysql;

import java.util.Date;

import jp.co.dk.crawler.TestCrawlerFoundation;
import jp.co.dk.crawler.dao.Documents;
import jp.co.dk.crawler.dao.record.DocumentsRecord;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.message.DataStoreManagerMessage;

import org.junit.Test;

public class TestDocumentsMysqlImpl extends TestCrawlerFoundation{

	@Test
	public void createTable_dropTable() {
		try {
			Documents documents = new DocumentsMysqlImpl(super.getAccessableDataBaseAccessParameter());
			documents.createTable();
			documents.dropTable();
		} catch (DataStoreManagerException e) {
			e.printStackTrace();
			fail(e);
		}
	}
	
	@Test
	public void insert() throws DataStoreManagerException {
		
		// ========================================準備========================================
		// テーブル作成
		Documents documents = new DocumentsMysqlImpl(super.getAccessableDataBaseAccessParameter());
		documents.createTable();
		
		//ファイルID
		long fileid    = 1234567890L;
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
		
		// ========================================後処理========================================
		documents.dropTable();
	}
	
	@Test
	public void select() throws DataStoreManagerException {
		
		// ========================================準備========================================
		// テーブル作成
		Documents documents = new DocumentsMysqlImpl(super.getAccessableDataBaseAccessParameter());
		documents.createTable();
		
		//ファイルID
		long fileid    = 1234567890L;
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
		
		// ========================================正常系========================================
		// 引数に正常値を渡した場合、正常に取得できること。
		try {
			// 登録処理を実行
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
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		// ========================================後処理========================================
		documents.dropTable();
	}
}
