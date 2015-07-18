package jp.co.dk.crawler.gdb;

import static org.junit.Assert.*;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.gdb.Url;
import jp.co.dk.crawler.rdb.CrawlerFoundationTest;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.exception.DocumentException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class UrlTest extends CrawlerFoundationTest{
	
	public static class 正常にインスタンスが生成できた場合＿０１ extends CrawlerFoundationTest{
		
		protected Url sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new Url("http://test.com", dsm);
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
		
		protected Url sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new Url("http://test.com/test1", dsm);
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
		
		protected Url sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new Url("http://test.com/test1/test1-1", dsm);
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
		
		protected Url sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new Url("http://test.com/test1/test1-1/test1-1-1", dsm);
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
		
		protected Url sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new Url("http://test.com/test1/test1-1/test1-1-2", dsm);
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
		
		protected Url sut;
		
		protected DataStoreManager dsm;
		
		@Before
		public void init() throws DocumentException {
			try {
				this.dsm = getNeo4JAccessableDataStoreManager();
				this.sut = new Url("http://test.com?aaa=bbb", dsm);
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
//	@Test
//	public void save() {
//		GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase("/tmp/neo4j_sample");
//		Transaction tx = graphDB.beginTx();
//		try {
//			Url url1 = new Url("http://test.com", graphDB);
//			url1.save();
//			Url url2 = new Url("http://test.com/path1", graphDB);
//			url2.save();
//			Url url3 = new Url("http://test.com/path2", graphDB);
//			url3.save();
//			Url url4 = new Url("http://test.com/path1/path1-1", graphDB);
//			url4.save();
//			Url url5 = new Url("http://test.com/path1/path1-1/test.html", graphDB);
//			url5.save();
//			Url url6 = new Url("http://test.com/path1/path1-1/test.html?key1=value1", graphDB);
//			url6.save();
//			Url url7 = new Url("http://test.com/path1/path1-1/test.html?key1=value1&key2=value2", graphDB);
//			url7.save();
//			tx.success();
//		} catch (PageIllegalArgumentException e) {
//			fail(e);
//		} finally {
//			tx.finish();
//			graphDB.shutdown();
//		}
//	}

}
