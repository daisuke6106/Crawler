package jp.co.dk.crawler.gdb;

import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.gdb.GUrl;
import jp.co.dk.crawler.rdb.CrawlerFoundationTest;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.Node;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerCypherException;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerException;
import jp.co.dk.neo4jdatastoremanager.property.Neo4JDataStoreManagerProperty;
import jp.co.dk.property.exception.PropertyException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class GUrlTest extends CrawlerFoundationTest{
	
	public static class 正常にインスタンスが生成できた場合＿０１ extends CrawlerFoundationTest {
		
		protected static GUrl sut;
		
		protected static Neo4JDataStoreManager dsm;
		
		@BeforeClass
		public static void init() throws Neo4JDataStoreManagerException, PropertyException, PageIllegalArgumentException{
			dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty());
			sut = new GUrl("http://test.com", dsm);
		}
		
		@Test
		public void save() {
			try {
				dsm.startTrunsaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException | Neo4JDataStoreManagerException e) {
				fail(e);
			} finally {
				try {
					dsm.finishTrunsaction();
				} catch (Neo4JDataStoreManagerException e) {
					fail(e);
				}
			}
		}
		
		@Test
		public void getUrlNode() {
			try {
				dsm.startTrunsaction();
				Node findUrlNode = sut.getUrlNode();
				assertThat(findUrlNode.getProperty("url"), is("http://test.com"));
				dsm.commit();
			} catch (Neo4JDataStoreManagerException | Neo4JDataStoreManagerCypherException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿０２ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStoreManager dsm;
		
		@BeforeClass
		public static void init() throws Neo4JDataStoreManagerException, PropertyException, PageIllegalArgumentException{
			dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty());
			sut = new GUrl("http://test.com/test1", dsm);
		}
		
		@Test
		public void save() {
			try {
				dsm.startTrunsaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException | Neo4JDataStoreManagerException e) {
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

	public static class 正常にインスタンスが生成できた場合＿０３ extends CrawlerFoundationTest{
		
protected static GUrl sut;
		
		protected static Neo4JDataStoreManager dsm;
		
		@BeforeClass
		public static void init() throws Neo4JDataStoreManagerException, PropertyException, PageIllegalArgumentException{
			dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty());
			sut = new GUrl("http://test.com/test1/test1-1", dsm);
		}
		
		@Test
		public void save() {
			try {
				dsm.startTrunsaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException | Neo4JDataStoreManagerException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿０４ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStoreManager dsm;
		
		@BeforeClass
		public static void init() throws Neo4JDataStoreManagerException, PropertyException, PageIllegalArgumentException{
			dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty());
			sut = new GUrl("http://test.com/test1/test1-1/test1-1-1", dsm);
		}
		
		@Test
		public void save() {
			try {
				dsm.startTrunsaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException | Neo4JDataStoreManagerException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿０５ extends CrawlerFoundationTest{
		
protected static GUrl sut;
		
		protected static Neo4JDataStoreManager dsm;
		
		@BeforeClass
		public static void init() throws Neo4JDataStoreManagerException, PropertyException, PageIllegalArgumentException{
			dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty());
			sut = new GUrl("http://test.com/test1/test1-1/test1-1-2", dsm);
		}
		
		@Test
		public void save() {
			try {
				dsm.startTrunsaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException | Neo4JDataStoreManagerException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿０６ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStoreManager dsm;
		
		@BeforeClass
		public static void init() throws Neo4JDataStoreManagerException, PropertyException, PageIllegalArgumentException{
			dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty());
			sut = new GUrl("http://test.com/?aaa=bbb&ccc=ddd", dsm);
		}
		
		@Test
		public void save() {
			try {
				dsm.startTrunsaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException | Neo4JDataStoreManagerException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿０７ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStoreManager dsm;
		
		@BeforeClass
		public static void init() throws Neo4JDataStoreManagerException, PropertyException, PageIllegalArgumentException{
			dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty());
			sut = new GUrl("http://test.com/test1?aaa=bbb&ccc=ddd", dsm);
		}
		
		@Test
		public void save() {
			try {
				dsm.startTrunsaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException | Neo4JDataStoreManagerException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿０８ extends CrawlerFoundationTest{
		
protected static GUrl sut;
		
		protected static Neo4JDataStoreManager dsm;
		
		@BeforeClass
		public static void init() throws Neo4JDataStoreManagerException, PropertyException, PageIllegalArgumentException{
			dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty());
			sut = new GUrl("http://test.com/test1/test1-1?aaa=bbb&ccc=ddd", dsm);
		}
		
		@Test
		public void save() {
			try {
				dsm.startTrunsaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException | Neo4JDataStoreManagerException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿０９ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStoreManager dsm;
		
		@BeforeClass
		public static void init() throws Neo4JDataStoreManagerException, PropertyException, PageIllegalArgumentException{
			dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty());
			sut = new GUrl("http://test.com/test1/test1-1/test1-1-1?aaa=bbb&ccc=ddd", dsm);
		}
		
		@Test
		public void save() {
			try {
				dsm.startTrunsaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException | Neo4JDataStoreManagerException e) {
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
	
	public static class 正常にインスタンスが生成できた場合＿１０ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStoreManager dsm;
		
		@BeforeClass
		public static void init() throws Neo4JDataStoreManagerException, PropertyException, PageIllegalArgumentException{
			dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty());
			sut = new GUrl("http://test.com/test1/test1-1/test1-1-2?aaa=bbb&ccc=ddd", dsm);
		}
		
		@Test
		public void save() {
			try {
				dsm.startTrunsaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException | Neo4JDataStoreManagerException e) {
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
