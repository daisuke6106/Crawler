package jp.co.dk.crawler.dao.mysql;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.TestCrawlerFoundation;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

import org.junit.Test;

public class TestPagesMysqlImpl extends TestCrawlerFoundation{

	@Test
	public void createTable_dropTable() {
		try {
			Pages pages = new PagesMysqlImpl(super.getAccessableDataBaseAccessParameter());
			pages.createTable();
			pages.dropTable();
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void insert() {
		try {
			Pages pages = new PagesMysqlImpl(super.getAccessableDataBaseAccessParameter());
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
			
			pages.insert(protcol, host, path, parameter, requestHeader, responceHeader, contents, createDate, updateDate);
			
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		}
	}
}
