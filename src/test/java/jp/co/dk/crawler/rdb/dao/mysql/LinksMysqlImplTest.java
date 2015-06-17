package jp.co.dk.crawler.rdb.dao.mysql;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.message.CrawlerMessage;
import jp.co.dk.crawler.rdb.CrawlerFoundationTest;
import jp.co.dk.crawler.rdb.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.rdb.dao.Documents;
import jp.co.dk.crawler.rdb.dao.Links;
import jp.co.dk.crawler.rdb.dao.record.LinksRecord;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.message.DataStoreManagerMessage;

import org.junit.Test;

public class LinksMysqlImplTest extends CrawlerFoundationTest{

	@Test
	public void insert() throws DataStoreManagerException {
		
		DataStoreManager manager = getAccessableDataStoreManager();
		manager.startTrunsaction();
		
		Links pages =  (Links)manager.getDataAccessObject(CrawlerDaoConstants.LINKS);
		try {
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
			// ファイル名
			String from_filename = "1failename.txt";
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
			// ファイル名
			String to_filename = "2failename.txt";
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
		
		} finally {
			// ========================================後処理========================================
			manager.finishTrunsaction();
		}
	}
	
	@Test
	public void select() throws DataStoreManagerException {
		
		DataStoreManager manager = getAccessableDataStoreManager();
		manager.startTrunsaction();
		Links pages =  (Links)manager.getDataAccessObject(CrawlerDaoConstants.LINKS);
		try {
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
			// ファイル名
			String from_filename = "1failename.txt";
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
			// ファイル名
			String to_filename = "2failename.txt";
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
				pages.insert(from_protcol, from_host, null, null, to_protcol, to_host, null, null, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			} catch (CrawlerException e) {
				fail(e);
			}
			
			// ========================================正常系========================================
			// 引数に正常値を渡した場合、正常に取得できること。
			try {
				// 登録処理を実行
				LinksRecord record = pages.select(from_protcol, from_host, from_path, from_parameter).get(0);
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
				pages.select(from_protcol, from_host, null, null);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			}
		// ========================================後処理========================================
		} finally {
			manager.finishTrunsaction();
		}
	}
	
	@Test
	public void count() throws DataStoreManagerException {
		
		DataStoreManager manager = getAccessableDataStoreManager();
		manager.startTrunsaction();
		Links pages =  (Links)manager.getDataAccessObject(CrawlerDaoConstants.LINKS);
		try {
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
			// ファイル名
			String from_filename = "1failename.txt";
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
			// ファイル名
			String to_filename = "2failename.txt";
			// パラメータマップ
			Map<String, String> to_parameter = new HashMap<String,String>();
			to_parameter.put("2Parameter_key", "2Parameter_value");
			
			// 作成日時
			Date createDate = new Date();
			// 更新日時
			Date updateDate = new Date();
			
			// 登録前は０件が取得できること。
			assertEquals(pages.count(from_protcol, from_host, from_path, from_parameter),0);
			assertEquals(pages.count(from_protcol, from_host, null, null),0);
			
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
				pages.insert(from_protcol, from_host, null, null, to_protcol, to_host, null, null, createDate, updateDate);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			} catch (CrawlerException e) {
				fail(e);
			}
			
			// ========================================正常系========================================
			// 引数に正常値を渡した場合、正常に取得できること。
			try {
				// 登録処理を実行
				assertEquals(pages.count(from_protcol, from_host, from_path, from_parameter),1);
			} catch (DataStoreManagerException e) {
				e.printStackTrace();
				fail(e);
			}
			
			// 引数に空のパスリスト、パラメータマップを渡した場合、正常に取得できること。
			try {
				// 登録処理を実行
				assertEquals(pages.count(from_protcol, from_host, null, null),1);
				
			} catch (DataStoreManagerException e) {
				fail(e);
			}
		// ========================================後処理========================================
		} finally {
			manager.finishTrunsaction();
		}
	}
}
