package jp.co.dk.crawler.dao.mysql;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.TestCrawlerFoundation;
import jp.co.dk.crawler.dao.Links;
import jp.co.dk.crawler.dao.record.LinksRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.message.CrawlerMessage;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.message.DataStoreManagerMessage;

import org.junit.Test;

public class TestLinksMysqlImpl extends TestCrawlerFoundation{

	@Test
	public void createTable_dropTable() {
		try {
			Links pages = new LinksMysqlImpl(super.getAccessableDataBaseAccessParameter());
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
		Links pages = new LinksMysqlImpl(super.getAccessableDataBaseAccessParameter());
		pages.createTable();
		
		// ------------------------------------FROM値------------------------------------
		// プロトコル
		String from_protcol   = "1http";
		// ホスト名
		String from_host      = "1google.com";
		// パスリスト
		List<String> from_path = new ArrayList<String>();
		from_path.add("1doodles");
		from_path.add("1finder");
		from_path.add("12013");
		from_path.add("1All%20doodles");
		// パラメータマップ
		Map<String, String> from_parameter = new HashMap<String,String>();
		from_parameter.put("1Parameter_key", "1Parameter_value");
		
		// ------------------------------------TO値------------------------------------
		// プロトコル
		String to_protcol   = "2http";
		// ホスト名
		String to_host      = "2google.com";
		// パスリスト
		List<String> to_path = new ArrayList<String>();
		to_path.add("2doodles");
		to_path.add("2finder");
		to_path.add("22013");
		to_path.add("2All%20doodles");
		// パラメータマップ
		Map<String, String> to_parameter = new HashMap<String,String>();
		to_parameter.put("2Parameter_key", "2Parameter_value");
		
		// 作成日時
		Date createDate = new Date();
		// 更新日時
		Date updateDate = new Date();
		
		// ========================================正常系========================================
		
		// 引数に正常値を渡した場合、正常に登録できること。
		try {
			// 登録処理を実行
			pages.insert(from_protcol, from_host, from_path, from_parameter, to_protcol, to_host, to_path, to_parameter, createDate, updateDate);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// 引数に空のパスリスト、パラメータマップを渡した場合、正常に登録できること。
		try {
			// 登録処理を実行
			pages.insert(from_protcol, from_host, new ArrayList<String>(), new HashMap<String,String>(), to_protcol, to_host, new ArrayList<String>(), new HashMap<String,String>(), createDate, updateDate);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// ========================================異常系========================================
		// すでにレコードが存在する場合、異常が発生すること。
		try {
			// 登録処理を実行
			pages.insert(from_protcol, from_host, from_path, from_parameter, to_protcol, to_host, to_path, to_parameter, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			assertEquals(e.getMessageObj(), DataStoreManagerMessage.FAILE_TO_EXECUTE_SQL);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// FROM_プロトコル名がnullの場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert(null, from_host, from_path, from_parameter, to_protcol, to_host, to_path, to_parameter, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		// FROM_ホスト名がnullの場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert(from_protcol, null, from_path, from_parameter, to_protcol, to_host, to_path, to_parameter, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		// FROM_プロトコル名が空の場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert("", from_host, from_path, from_parameter, to_protcol, to_host, to_path, to_parameter, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		// FROM_ホスト名が空の場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert(from_protcol, "", from_path, from_parameter, to_protcol, to_host, to_path, to_parameter, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		// TO_プロトコル名がnullの場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert(from_protcol, from_host, from_path, from_parameter, null, to_host, to_path, to_parameter, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		// TO_ホスト名がnullの場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert(from_protcol, from_host, from_path, from_parameter, to_protcol, null, to_path, to_parameter, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		// TO_プロトコル名が空の場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert(from_protcol, from_host, from_path, from_parameter, "", to_host, to_path, to_parameter, createDate, updateDate);
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			assertEquals(e.getMessageObj(), CrawlerMessage.PARAMETER_IS_NOT_SET);
		}
		
		// TO_ホスト名が空の場合、例外が送出されること。
		try {
			// 登録処理を実行
			pages.insert(from_protcol, from_host, from_path, from_parameter, to_protcol, "", to_path, to_parameter, createDate, updateDate);
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
		Links pages = new LinksMysqlImpl(super.getAccessableDataBaseAccessParameter());
		pages.createTable();
		
		// ------------------------------------FROM値------------------------------------
		// プロトコル
		String from_protcol   = "1http";
		// ホスト名
		String from_host      = "1google.com";
		// パスリスト
		List<String> from_path = new ArrayList<String>();
		from_path.add("1doodles");
		from_path.add("1finder");
		from_path.add("12013");
		from_path.add("1All%20doodles");
		// パラメータマップ
		Map<String, String> from_parameter = new HashMap<String,String>();
		from_parameter.put("1Parameter_key", "1Parameter_value");
		
		// ------------------------------------TO値------------------------------------
		// プロトコル
		String to_protcol   = "2http";
		// ホスト名
		String to_host      = "2google.com";
		// パスリスト
		List<String> to_path = new ArrayList<String>();
		to_path.add("2doodles");
		to_path.add("2finder");
		to_path.add("22013");
		to_path.add("2All%20doodles");
		// パラメータマップ
		Map<String, String> to_parameter = new HashMap<String,String>();
		to_parameter.put("2Parameter_key", "2Parameter_value");
		
		// 作成日時
		Date createDate = new Date();
		// 更新日時
		Date updateDate = new Date();
		
		
		// 引数に正常値を渡した場合、正常に登録できること。
		try {
			// 登録処理を実行
			pages.insert(from_protcol, from_host, from_path, from_parameter, to_protcol, to_host, to_path, to_parameter, createDate, updateDate);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// 引数に空のパスリスト、パラメータマップを渡した場合、正常に登録できること。
		try {
			// 登録処理を実行
			pages.insert(from_protcol, from_host, new ArrayList<String>(), new HashMap<String, String>(), to_protcol, to_host, new ArrayList<String>(), new HashMap<String, String>(), createDate, updateDate);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		}
		
		// ========================================正常系========================================
		// 引数に正常値を渡した場合、正常に取得できること。
		try {
			// 登録処理を実行
			LinksRecord record = pages.select(from_protcol, from_host, from_path, from_parameter);
			assertEquals(record.getFromProtocol(), "1http");
			assertEquals(record.getFromHost(), "1google.com");
			assertEquals(record.getFromH_path(), from_path.hashCode());
			assertEquals(record.getFromH_parameter(), from_parameter.hashCode());
			
			assertEquals(record.getToProtocol(), "2http");
			assertEquals(record.getToHost(), "2google.com");
			assertEquals(record.getToH_path(), to_path.hashCode());
			assertEquals(record.getToH_parameter(), to_parameter.hashCode());
			
			assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getCreateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(createDate));
			assertEquals(super.getStringByDate_YYYYMMDDHH24MMDD(record.getUpdateDate()), super.getStringByDate_YYYYMMDDHH24MMDD(updateDate));
		} catch (DataStoreManagerException e) {
			e.printStackTrace();
			fail(e);
		}
		
		// 引数に空のパスリスト、パラメータマップを渡した場合、正常に取得できること。
		try {
			// 登録処理を実行
			LinksRecord record = pages.select(from_protcol, from_host, null, null);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// ========================================後処理========================================
		pages.dropTable();
	}
}
