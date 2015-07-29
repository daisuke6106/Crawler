package jp.co.dk.crawler.gdb;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.rdb.CrawlerFoundationTest;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.exception.DocumentFatalException;
import jp.co.dk.document.message.DocumentMessage;
import jp.co.dk.test.template.TestCaseTemplate;
import mockit.Expectations;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import static jp.co.dk.document.message.DocumentMessage.*;

@RunWith(Enclosed.class)
public class GCrawlerTest extends CrawlerFoundationTest{
	
	public static class コンストラクタ extends CrawlerFoundationTest{
		@Test
		public void 正常にインスタンスが生成できること() throws IOException {
			try {
				DataStoreManager dsm = getNeo4JAccessableDataStoreManager();
				GCrawler crawler = new GCrawler("http://www.google.com", dsm);
			} catch (PageIllegalArgumentException e) {
				fail(e);
			} catch (PageAccessException e) {
				fail(e);
			}
		}
	}
	
	public static class 正常にインスタンスが生成できた場合 extends CrawlerFoundationTest{
		
		protected GCrawler sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new GCrawler("http://gigazine.net/", dsm);
			} catch (PageIllegalArgumentException e) {
				fail(e);
			} catch (PageAccessException e) {
				fail(e);
			}
		}
		
		@Test
		public void save() {
			try {
				this.dsm.startTrunsaction();
				this.sut.save();
				this.dsm.commit();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (DataStoreManagerException e) {
				fail(e);
			}finally {
				try {
					this.dsm.finishTrunsaction();
				} catch (DataStoreManagerException e) {
					fail(e);
				}
			}
		}
	}
}