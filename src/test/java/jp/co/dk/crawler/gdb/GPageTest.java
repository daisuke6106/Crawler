package jp.co.dk.crawler.gdb;

import java.io.IOException;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.rdb.CrawlerFoundationTest;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.exception.DocumentException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;


@RunWith(Enclosed.class)
public class GPageTest extends CrawlerFoundationTest{
	
	public static class コンストラクタ extends CrawlerFoundationTest{
		@Test
		public void 正常にインスタンスが生成できること() throws IOException, DataStoreManagerException {
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
		
		protected static GPage sut;
		
		protected static DataStoreManager dsm;
		
		@BeforeClass
		public static void init() throws DocumentException, DataStoreManagerException, PageIllegalArgumentException, PageAccessException {
			dsm = getNeo4JAccessableDataStoreManager();
			sut = new GPage("https://ja.wikipedia.org/wiki/HyperText_Markup_Language", dsm);
		}
		
		@Test
		public void oder01_isSaved() {
			try {
				dsm.startTrunsaction();
				assertThat(sut.isSaved(), is(false));
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (DataStoreManagerException e) {
				fail(e);
			}finally {
				try {
					dsm.finishTrunsaction();
				} catch (DataStoreManagerException e) {
					fail(e);
				}
			}
		}
		
		@Test
		public void oder02_getLatestID() {
			try {
				dsm.startTrunsaction();
				assertThat(sut.getLatestID(), is(-1));
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (DataStoreManagerException e) {
				fail(e);
			}finally {
				try {
					dsm.finishTrunsaction();
				} catch (DataStoreManagerException e) {
					fail(e);
				}
			}
		}
		
		@Test
		public void oder03_save() {
			try {
				dsm.startTrunsaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (DataStoreManagerException e) {
				fail(e);
			}finally {
				try {
					dsm.finishTrunsaction();
				} catch (DataStoreManagerException e) {
					fail(e);
				}
			}
		}
		
		@Test
		public void oder04_isSaved() {
			try {
				dsm.startTrunsaction();
				assertThat(sut.isSaved(), is(true));
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (DataStoreManagerException e) {
				fail(e);
			}finally {
				try {
					dsm.finishTrunsaction();
				} catch (DataStoreManagerException e) {
					fail(e);
				}
			}
		}

		@Test
		public void oder05_getLatestID() {
			try {
				dsm.startTrunsaction();
				assertThat(sut.getLatestID(), not(-1));
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (DataStoreManagerException e) {
				fail(e);
			}finally {
				try {
					dsm.finishTrunsaction();
				} catch (DataStoreManagerException e) {
					fail(e);
				}
			}
		}
	}
}