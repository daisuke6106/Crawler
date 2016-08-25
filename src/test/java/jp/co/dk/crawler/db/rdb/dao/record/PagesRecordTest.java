package jp.co.dk.crawler.db.rdb.dao.record;

import jp.co.dk.crawler.db.rdb.CrawlerFoundationTest;

// 実装を変えたため、本試験は不要

public class PagesRecordTest extends CrawlerFoundationTest{

//	@Test
//	public void constractor() {
//		// ==============================正常系==============================
//		// すべての値を設定した場合、正常にインスタンス生成ができること。
//		// 設定した値はすべてフィールドに保存されること。
//		try {
//			// プロトコル
//			String protcol   = "http";
//			// ホスト名
//			String host      = "google.com";
//			// パスリスト
//			List<String> path = new ArrayList<String>();
//			path.add("doodles");
//			path.add("finder");
//			path.add("2013");
//			path.add("All%20doodles");
//			// パラメータマップ
//			Map<String, String> parameter = new HashMap<String,String>();
//			parameter.put("Parameter_key", "Parameter_value");
//			// リクエストヘッダ
//			Map<String, String> requestHeader = new HashMap<String,String>();
//			requestHeader.put("RequestHeader_key", "RequestHeader_value");
//			// レスポンスヘッダ
//			Map<String, String> responceHeader = new HashMap<String,String>();
//			responceHeader.put("ResponceHeader_key", "ResponceHeader_value");
//			// コンテンツデータ
//			byte[] contents = {1,2,3};
//			// 作成日時
//			Date createDate = new Date();
//			// 更新日時
//			Date updateDate = new Date();
//			
//			// インスタンス生成
//			PagesRecord record = new PagesRecord(protcol, host, path, parameter, requestHeader, responceHeader, contents, createDate, updateDate);
//			
//			// すべての値が一致すること。
//			assertEquals(record.protocol, protcol);
//			assertEquals(record.host, host);
//			assertEquals(record.path, path);
//			assertEquals(record.parameter, parameter);
//			assertEquals(record.requestHeader, requestHeader);
//			assertEquals(record.responceHeader, responceHeader);
//			assertEquals(record.contents, contents);
//			assertEquals(record.createDate, createDate);
//			assertEquals(record.updateDate, updateDate);
//		} catch (CrawlerException e) {
//			fail(e);
//		}
//		
//		// ==============================異常系==============================
//		// パラメータが不足している場合、例外が発生すること。
//		try {
//			// プロトコル
//			String protcol   = null;
//			// ホスト名
//			String host      = "google.com";
//			// パスリスト
//			List<String> path = new ArrayList<String>();
//			path.add("doodles");
//			path.add("finder");
//			path.add("2013");
//			path.add("All%20doodles");
//			// パラメータマップ
//			Map<String, String> parameter = new HashMap<String,String>();
//			parameter.put("Parameter_key", "Parameter_value");
//			// リクエストヘッダ
//			Map<String, String> requestHeader = new HashMap<String,String>();
//			requestHeader.put("RequestHeader_key", "RequestHeader_value");
//			// レスポンスヘッダ
//			Map<String, String> responceHeader = new HashMap<String,String>();
//			responceHeader.put("ResponceHeader_key", "ResponceHeader_value");
//			// コンテンツデータ
//			byte[] contents = {1,2,3};
//			// 作成日時
//			Date createDate = new Date();
//			// 更新日時
//			Date updateDate = new Date();
//			
//			// インスタンス生成
//			new PagesRecord(protcol, host, path, parameter, requestHeader, responceHeader, contents, createDate, updateDate);
//			fail();
//		} catch (CrawlerException e) {
//			assertEquals(e.getMessageObj(), PARAMETER_IS_NOT_SET);
//		}
//		
//		// パラメータが不足している場合、例外が発生すること。
//		try {
//			// プロトコル
//			String protcol   = "";
//			// ホスト名
//			String host      = "google.com";
//			// パスリスト
//			List<String> path = new ArrayList<String>();
//			path.add("doodles");
//			path.add("finder");
//			path.add("2013");
//			path.add("All%20doodles");
//			// パラメータマップ
//			Map<String, String> parameter = new HashMap<String,String>();
//			parameter.put("Parameter_key", "Parameter_value");
//			// リクエストヘッダ
//			Map<String, String> requestHeader = new HashMap<String,String>();
//			requestHeader.put("RequestHeader_key", "RequestHeader_value");
//			// レスポンスヘッダ
//			Map<String, String> responceHeader = new HashMap<String,String>();
//			responceHeader.put("ResponceHeader_key", "ResponceHeader_value");
//			// コンテンツデータ
//			byte[] contents = {1,2,3};
//			// 作成日時
//			Date createDate = new Date();
//			// 更新日時
//			Date updateDate = new Date();
//			
//			// インスタンス生成
//			new PagesRecord(protcol, host, path, parameter, requestHeader, responceHeader, contents, createDate, updateDate);
//			fail();
//		} catch (CrawlerException e) {
//			assertEquals(e.getMessageObj(), PARAMETER_IS_NOT_SET);
//		}
//		
//		// パラメータが不足している場合、例外が発生すること。
//		try {
//			// プロトコル
//			String protcol   = "http";
//			// ホスト名
//			String host      = null;
//			// パスリスト
//			List<String> path = new ArrayList<String>();
//			path.add("doodles");
//			path.add("finder");
//			path.add("2013");
//			path.add("All%20doodles");
//			// パラメータマップ
//			Map<String, String> parameter = new HashMap<String,String>();
//			parameter.put("Parameter_key", "Parameter_value");
//			// リクエストヘッダ
//			Map<String, String> requestHeader = new HashMap<String,String>();
//			requestHeader.put("RequestHeader_key", "RequestHeader_value");
//			// レスポンスヘッダ
//			Map<String, String> responceHeader = new HashMap<String,String>();
//			responceHeader.put("ResponceHeader_key", "ResponceHeader_value");
//			// コンテンツデータ
//			byte[] contents = {1,2,3};
//			// 作成日時
//			Date createDate = new Date();
//			// 更新日時
//			Date updateDate = new Date();
//			
//			// インスタンス生成
//			new PagesRecord(protcol, host, path, parameter, requestHeader, responceHeader, contents, createDate, updateDate);
//			fail();
//		} catch (CrawlerException e) {
//			assertEquals(e.getMessageObj(), PARAMETER_IS_NOT_SET);
//		}
//		
//		// パラメータが不足している場合、例外が発生すること。
//		try {
//			// プロトコル
//			String protcol   = "http";
//			// ホスト名
//			String host      = "";
//			// パスリスト
//			List<String> path = new ArrayList<String>();
//			path.add("doodles");
//			path.add("finder");
//			path.add("2013");
//			path.add("All%20doodles");
//			// パラメータマップ
//			Map<String, String> parameter = new HashMap<String,String>();
//			parameter.put("Parameter_key", "Parameter_value");
//			// リクエストヘッダ
//			Map<String, String> requestHeader = new HashMap<String,String>();
//			requestHeader.put("RequestHeader_key", "RequestHeader_value");
//			// レスポンスヘッダ
//			Map<String, String> responceHeader = new HashMap<String,String>();
//			responceHeader.put("ResponceHeader_key", "ResponceHeader_value");
//			// コンテンツデータ
//			byte[] contents = {1,2,3};
//			// 作成日時
//			Date createDate = new Date();
//			// 更新日時
//			Date updateDate = new Date();
//			
//			// インスタンス生成
//			new PagesRecord(protcol, host, path, parameter, requestHeader, responceHeader, contents, createDate, updateDate);
//			fail();
//		} catch (CrawlerException e) {
//			assertEquals(e.getMessageObj(), PARAMETER_IS_NOT_SET);
//		}
//		
//		// ==============================正常系==============================
//		// すべての値を設定した場合、正常にインスタンス生成ができること。
//		// 設定した値はすべてフィールドに保存されること。
//		try {
//			// プロトコル
//			String protcol   = "http";
//			// ホスト名
//			String host      = "google.com";
//			// パスリスト
//			List<String> path = new ArrayList<String>();
//			path.add("doodles");
//			path.add("finder");
//			path.add("2013");
//			path.add("All%20doodles");
//			// パラメータマップ
//			Map<String, String> parameter = new HashMap<String,String>();
//			parameter.put("Parameter_key", "Parameter_value");
//			// リクエストヘッダ
//			Map<String, String> requestHeader = new HashMap<String,String>();
//			requestHeader.put("RequestHeader_key", "RequestHeader_value");
//			// レスポンスヘッダ
//			Map<String, String> responceHeader = new HashMap<String,String>();
//			responceHeader.put("ResponceHeader_key", "ResponceHeader_value");
//			// コンテンツデータ
//			byte[] contents = {1,2,3};
//			// 作成日時
//			Date createDate = new Date();
//			// 更新日時
//			Date updateDate = new Date();
//			
//			// インスタンス生成
//			PagesRecord record = new PagesRecord(protcol, host, path, parameter, requestHeader, responceHeader, contents, createDate, updateDate);
//			
//			// すべての値が一致すること。
//			assertEquals(record.protocol, protcol);
//			assertEquals(record.host, host);
//			assertEquals(record.path, path);
//			assertEquals(record.parameter, parameter);
//			assertEquals(record.requestHeader, requestHeader);
//			assertEquals(record.responceHeader, responceHeader);
//			assertEquals(record.contents, contents);
//			assertEquals(record.createDate, createDate);
//			assertEquals(record.updateDate, updateDate);
//		} catch (CrawlerException e) {
//			fail(e);
//		}
//	}

}
