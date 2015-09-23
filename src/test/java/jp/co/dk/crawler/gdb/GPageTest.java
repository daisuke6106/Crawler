package jp.co.dk.crawler.gdb;

import java.io.IOException;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.rdb.CrawlerFoundationTest;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;


@RunWith(Enclosed.class)
public class GPageTest extends CrawlerFoundationTest{
	
	public static class コンストラクタ extends CrawlerFoundationTest{
		@Test
		public void 正常にインスタンスが生成できること() throws IOException {
			try {
				Neo4JDataStoreManager dsm = getNeo4JAccessableDataStoreManager();
				GPage page = new GPage("http://www.google.com", dsm);
			} catch (PageIllegalArgumentException e) {
				fail(e);
			} catch (PageAccessException e) {
				fail(e);
			} catch (Neo4JDataStoreManagerException e) {
				fail(e);
			}
		}
	}
	
	public static class 正常にインスタンスが生成できた場合 extends CrawlerFoundationTest{
		
		protected static GPage sut;
		
		protected static Neo4JDataStoreManager dsm;
		
		@BeforeClass
		public static void init() throws DocumentException, PageIllegalArgumentException, PageAccessException, Neo4JDataStoreManagerException {
			dsm = getNeo4JAccessableDataStoreManager();
			sut = new GPage("http://gigazine.net/news/20150910-docomo-iphone-6s-pre-order/", dsm);
		}
		
		@Test
		public void save() {
			try {
				dsm.startTrunsaction();
				sut.save();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (Neo4JDataStoreManagerException e) {
				fail(e);
			} catch (Throwable e) {
				fail(e);
			} finally {
				try {
					dsm.finishTrunsaction();
				} catch (Neo4JDataStoreManagerException e) {
					fail(e);
				}
			}
		}
	}
}