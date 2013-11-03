package jp.co.dk.crawler;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;
import jp.co.dk.property.exception.PropertyException;

import org.junit.Test;

public class TestCrawler extends TestCrawlerFoundation{

	@Test
	public void constractor() {
		// ========================================正常系========================================
		// 引数なしでコンストラクタを呼び出した場合、正常にインスタンスが生成されること。
		try {
			Crawler crawler = new Crawler("");
			fail();
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (BrowzingException e) {
			fail(e);
		} catch (PropertyException e) {
			fail(e);
		}
		
		// 引数ありでコンストラクタを呼び出した場合、正常にインスタンスが生成されること。
		try {
			Crawler crawler = new Crawler("", new DataStoreManagerProperty());
			assertNotNull(crawler.dsm);
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (BrowzingException e) {
			fail(e);
		} catch (PropertyException e) {
			fail(e);
		}
		
		// ========================================異常系========================================
		// データストア関連の異常系処理は、データストアライブラリにて行なっているため、行わない
	}

}
