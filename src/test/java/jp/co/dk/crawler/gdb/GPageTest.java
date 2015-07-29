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
public class GPageTest extends CrawlerFoundationTest{
	
	public static class コンストラクタ extends CrawlerFoundationTest{
		@Test
		public void 正常にインスタンスが生成できること() throws IOException {
			try {
				DataStoreManager dsm = getNeo4JAccessableDataStoreManager();
				GPage page = new GPage("http://www.google.com", dsm);
			} catch (PageIllegalArgumentException e) {
				fail(e);
			} catch (PageAccessException e) {
				fail(e);
			}
		}
	}
	
	public static class 正常にインスタンスが生成できた場合 extends CrawlerFoundationTest{
		
		protected GPage sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new GPage("http://gigazine.net/news/20150729-spot-the-drowning-child/", dsm);
			} catch (PageIllegalArgumentException e) {
				fail(e);
			} catch (PageAccessException e) {
				fail(e);
			}
		}
		
		@Test
		public void oder01_isSaved() {
			try {
				this.dsm.startTrunsaction();
				assertThat(this.sut.isSaved(), is(false));
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
		
		@Test
		public void oder02_getID() {
			try {
				this.dsm.startTrunsaction();
				assertThat(this.sut.getID(), is(-1));
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
		
		@Test
		public void oder03_save() {
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
		
		@Test
		public void oder04_isSaved() {
			try {
				this.dsm.startTrunsaction();
				assertThat(this.sut.isSaved(), is(true));
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

		@Test
		public void oder05_getID() {
			try {
				this.dsm.startTrunsaction();
				assertThat(this.sut.getID(), not(-1));
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