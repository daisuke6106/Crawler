package jp.co.dk.crawler;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

import org.junit.Test;

public class TestPage extends TestCrawlerFoundation{

	@Test
	public void constactor() {
		try {
			Page page = new Page("http://www.google.com", getAccessableDataStoreManager());
		} catch (BrowzingException e) {
			fail(e);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
	
	@Test
	public void getLong() {
		
		// ==========正常にファイルIDが返却されること==========
		// プロトコル＋ホスト名
		try {
			Page page = new Page("http://www.google.com", getAccessableDataStoreManager());
			
			// 比較値を生成
			String protocol              = "http";
			String host                  = "www.google.com";
			List<String> pathList        = new ArrayList<String>();
			String filename              = "default.html";
			Map<String,String> parameter = new HashMap<String,String>();
			BigDecimal result = new BigDecimal(protocol.hashCode());
			result = result.multiply(new BigDecimal(host.hashCode()));
			result = result.multiply(new BigDecimal(pathList.hashCode()));
			result = result.multiply(new BigDecimal(filename.hashCode()));
			result = result.multiply(new BigDecimal(parameter.hashCode()));
			
			assertEquals(page.getFileId(), result.longValue());
		} catch (BrowzingException e) {
			fail(e);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// プロトコル＋ホスト名＋パス
		try {
			Page page = new Page("http://www.google.com/doodles/about", getAccessableDataStoreManager());
			
			// 比較値を生成
			String protocol              = "http";
			String host                  = "www.google.com";
			List<String> pathList        = new ArrayList<String>();
			pathList.add("doodles");
			pathList.add("about");
			String filename              = "default.html";
			Map<String,String> parameter = new HashMap<String,String>();
			BigDecimal result = new BigDecimal(protocol.hashCode());
			result = result.multiply(new BigDecimal(host.hashCode()));
			result = result.multiply(new BigDecimal(pathList.hashCode()));
			result = result.multiply(new BigDecimal(filename.hashCode()));
			result = result.multiply(new BigDecimal(parameter.hashCode()));
			
			assertEquals(page.getFileId(), result.longValue());
		} catch (BrowzingException e) {
			fail(e);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// プロトコル＋ホスト名＋パス＋ファイル名
		try {
			Page page = new Page("http://www.atmarkit.co.jp/ait/articles/1310/09/news127.html", getAccessableDataStoreManager());
			
			// 比較値を生成
			String protocol              = "http";
			String host                  = "www.atmarkit.co.jp";
			List<String> pathList        = new ArrayList<String>();
			pathList.add("ait");
			pathList.add("articles");
			pathList.add("1310");
			pathList.add("09");
			String filename              = "news127.html";
			Map<String,String> parameter = new HashMap<String,String>();
			BigDecimal result = new BigDecimal(protocol.hashCode());
			result = result.multiply(new BigDecimal(host.hashCode()));
			result = result.multiply(new BigDecimal(pathList.hashCode()));
			result = result.multiply(new BigDecimal(filename.hashCode()));
			result = result.multiply(new BigDecimal(parameter.hashCode()));
			
			assertEquals(page.getFileId(), result.longValue());
		} catch (BrowzingException e) {
			fail(e);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
		
		// プロトコル＋ホスト名＋パラメータ
		try {
			Page page = new Page("https://www.google.co.jp/?gws_rd=cr&ei=XVplUvTHH86FkwXcvoCACQ#q=test", getAccessableDataStoreManager());
			
			// 比較値を生成
			String protocol              = "https";
			String host                  = "www.google.co.jp";
			List<String> pathList        = new ArrayList<String>();
			String filename              = "default.html";
			Map<String,String> parameter = new HashMap<String,String>();
			parameter.put("gws_rd", "cr");
			parameter.put("ei", "XVplUvTHH86FkwXcvoCACQ");
			BigDecimal result = new BigDecimal(protocol.hashCode());
			result = result.multiply(new BigDecimal(host.hashCode()));
			result = result.multiply(new BigDecimal(pathList.hashCode()));
			result = result.multiply(new BigDecimal(filename.hashCode()));
			result = result.multiply(new BigDecimal(parameter.hashCode()));
			
			assertEquals(page.getFileId(), result.longValue());
		} catch (BrowzingException e) {
			fail(e);
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
}
