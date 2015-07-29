package jp.co.dk.crawler.gdb;

import static org.junit.Assert.*;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.gdb.GUrl;
import jp.co.dk.crawler.rdb.CrawlerFoundationTest;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.exception.DocumentException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class GUrlTest extends CrawlerFoundationTest{
	
	public static class 正常にインスタンスが生成できた場合＿０１ extends CrawlerFoundationTest{
		
		protected GUrl sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new GUrl("http://test.com", dsm);
			} catch (PageIllegalArgumentException e) {
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
	
	
	public static class 正常にインスタンスが生成できた場合＿０２ extends CrawlerFoundationTest{
		
		protected GUrl sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new GUrl("http://test.com/test1", dsm);
			} catch (PageIllegalArgumentException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿０３ extends CrawlerFoundationTest{
		
		protected GUrl sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new GUrl("http://test.com/test1/test1-1", dsm);
			} catch (PageIllegalArgumentException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿０４ extends CrawlerFoundationTest{
		
		protected GUrl sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new GUrl("http://test.com/test1/test1-1/test1-1-1", dsm);
			} catch (PageIllegalArgumentException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿０５ extends CrawlerFoundationTest{
		
		protected GUrl sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new GUrl("http://test.com/test1/test1-1/test1-1-2", dsm);
			} catch (PageIllegalArgumentException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿０６ extends CrawlerFoundationTest{
		
		protected GUrl sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new GUrl("http://test.com?aaa=bbb&ccc=ddd", dsm);
			} catch (PageIllegalArgumentException e) {
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
	
	
	public static class 正常にインスタンスが生成できた場合＿０７ extends CrawlerFoundationTest{
		
		protected GUrl sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new GUrl("http://test.com/test1?aaa=bbb&ccc=ddd", dsm);
			} catch (PageIllegalArgumentException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿０８ extends CrawlerFoundationTest{
		
		protected GUrl sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new GUrl("http://test.com/test1/test1-1?aaa=bbb&ccc=ddd", dsm);
			} catch (PageIllegalArgumentException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿０９ extends CrawlerFoundationTest{
		
		protected GUrl sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new GUrl("http://test.com/test1/test1-1/test1-1-1?aaa=bbb&ccc=ddd", dsm);
			} catch (PageIllegalArgumentException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿１０ extends CrawlerFoundationTest{
		
		protected GUrl sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new GUrl("http://test.com/test1/test1-1/test1-1-2?aaa=bbb&ccc=ddd", dsm);
			} catch (PageIllegalArgumentException e) {
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
