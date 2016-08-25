package jp.co.dk.crawler.db.gdb;

import java.io.IOException;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageMovableLimitException;
import jp.co.dk.browzer.exception.PageRedirectException;
import jp.co.dk.crawler.db.gdb.GCrawler;
import jp.co.dk.crawler.db.rdb.CrawlerFoundationTest;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class GCrawlerTest extends CrawlerFoundationTest{
	
	public static class コンストラクタ extends CrawlerFoundationTest{
		@Test
		public void 正常にインスタンスが生成できること() throws IOException, Neo4JDataStoreManagerException {
			try {
				Neo4JDataStoreManager dsm = getNeo4JAccessableDataStoreManager();
				GCrawler crawler = new GCrawler("http://www.google.com", dsm);
			} catch (PageIllegalArgumentException e) {
				fail(e);
			} catch (PageAccessException e) {
				fail(e);
			}
		}
	}
	
	public static class 正常にインスタンスが生成できた場合 extends CrawlerFoundationTest{
		
		protected static GCrawler sut;
		
		protected static Neo4JDataStoreManager dsm;
		
		@BeforeClass
		public static void init() throws DocumentException, Neo4JDataStoreManagerException, CrawlerInitException, PageIllegalArgumentException, PageAccessException {
			dsm = getNeo4JAccessableDataStoreManager();
			sut = new GCrawler("http://gigazine.net/", dsm);
		}
		
		@Test
		public void save() {
			try {
				this.dsm.startTrunsaction();
				this.sut.save();
				this.dsm.commit();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (Neo4JDataStoreManagerException e) {
				fail(e);
			}finally {
				try {
					this.dsm.finishTrunsaction();
				} catch (Neo4JDataStoreManagerException e) {
					fail(e);
				}
			}
		}
		
		@Test
		public void saveAll() {
			try {
				this.dsm.startTrunsaction();
				this.sut.saveAll();
				this.dsm.commit();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (Neo4JDataStoreManagerException e) {
				fail(e);
			} catch (PageAccessException e) {
				fail(e);
			} catch (PageIllegalArgumentException e) {
				fail(e);
			} catch (PageRedirectException e) {
				fail(e);
			} catch (PageMovableLimitException e) {
				fail(e);
			} catch (DocumentException e) {
				fail(e);
			}finally {
				try {
					this.dsm.finishTrunsaction();
				} catch (Neo4JDataStoreManagerException e) {
					fail(e);
				}
			}
		}
	}
}