package jp.co.dk.crawler.gdb;

import static org.junit.Assert.*;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.gdb.GUrl;
import jp.co.dk.crawler.gdb.neo4j.Neo4JDataStore;
import jp.co.dk.crawler.gdb.neo4j.exception.CrawlerNeo4JException;
import jp.co.dk.crawler.gdb.neo4j.property.CrawlerNeo4JParameter;
import jp.co.dk.crawler.rdb.CrawlerFoundationTest;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.exception.DocumentException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

@RunWith(Enclosed.class)
public class GUrlTest extends CrawlerFoundationTest{
	
	public static class 正常にインスタンスが生成できた場合＿０１ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStore dsm;
		
		@BeforeClass
		public static void init() throws CrawlerNeo4JException, PageIllegalArgumentException{
			dsm = new Neo4JDataStore(new CrawlerNeo4JParameter("http://localhost:7474/db/data"));
			sut = new GUrl("http://test.com", dsm);
		}
		
		@Test
		public void save() throws CrawlerNeo4JException {
			try {
				dsm.startTransaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (RuntimeException e) {
				fail(e);
			} finally {
				dsm.finishTransaction();
			}
		}
	}
	
	public static class 正常にインスタンスが生成できた場合＿０２ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStore dsm;
		
		@BeforeClass
		public static void init() throws CrawlerNeo4JException, PageIllegalArgumentException{
			dsm = new Neo4JDataStore(new CrawlerNeo4JParameter("http://localhost:7474/db/data"));
			sut = new GUrl("http://test.com/test1", dsm);
		}
		
		@Test
		public void save() throws CrawlerNeo4JException {
			try {
				dsm.startTransaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (RuntimeException e) {
				fail(e);
			} finally {
				dsm.finishTransaction();
			}
		}
	}

	public static class 正常にインスタンスが生成できた場合＿０３ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStore dsm;
		
		@BeforeClass
		public static void init() throws CrawlerNeo4JException, PageIllegalArgumentException{
			dsm = new Neo4JDataStore(new CrawlerNeo4JParameter("http://localhost:7474/db/data"));
			sut = new GUrl("http://test.com/test1/test1-1", dsm);
		}
		
		@Test
		public void save() throws CrawlerNeo4JException {
			try {
				dsm.startTransaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (RuntimeException e) {
				fail(e);
			} finally {
				dsm.finishTransaction();
			}
		}
	}
	
	public static class 正常にインスタンスが生成できた場合＿０４ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStore dsm;
		
		@BeforeClass
		public static void init() throws CrawlerNeo4JException, PageIllegalArgumentException{
			dsm = new Neo4JDataStore(new CrawlerNeo4JParameter("http://localhost:7474/db/data"));
			sut = new GUrl("http://test.com/test1/test1-1/test1-1-1", dsm);
		}
		
		@Test
		public void save() throws CrawlerNeo4JException {
			try {
				dsm.startTransaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (RuntimeException e) {
				fail(e);
			} finally {
				dsm.finishTransaction();
			}
		}
	}
	
	public static class 正常にインスタンスが生成できた場合＿０５ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStore dsm;
		
		@BeforeClass
		public static void init() throws CrawlerNeo4JException, PageIllegalArgumentException{
			dsm = new Neo4JDataStore(new CrawlerNeo4JParameter("http://localhost:7474/db/data"));
			sut = new GUrl("http://test.com/test1/test1-1/test1-1-2", dsm);
		}
		
		@Test
		public void save() throws CrawlerNeo4JException {
			try {
				dsm.startTransaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (RuntimeException e) {
				fail(e);
			} finally {
				dsm.finishTransaction();
			}
		}
	}
	
	public static class 正常にインスタンスが生成できた場合＿０６ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStore dsm;
		
		@BeforeClass
		public static void init() throws CrawlerNeo4JException, PageIllegalArgumentException{
			dsm = new Neo4JDataStore(new CrawlerNeo4JParameter("http://localhost:7474/db/data"));
			sut = new GUrl("http://test.com?aaa=bbb&ccc=ddd", dsm);
		}
		
		@Test
		public void save() throws CrawlerNeo4JException {
			try {
				dsm.startTransaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (RuntimeException e) {
				fail(e);
			} finally {
				dsm.finishTransaction();
			}
		}
	}
	
	public static class 正常にインスタンスが生成できた場合＿０７ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStore dsm;
		
		@BeforeClass
		public static void init() throws CrawlerNeo4JException, PageIllegalArgumentException{
			dsm = new Neo4JDataStore(new CrawlerNeo4JParameter("http://localhost:7474/db/data"));
			sut = new GUrl("http://test.com/test1?aaa=bbb&ccc=ddd", dsm);
		}
		
		@Test
		public void save() throws CrawlerNeo4JException {
			try {
				dsm.startTransaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (RuntimeException e) {
				fail(e);
			} finally {
				dsm.finishTransaction();
			}
		}
	}
	
	public static class 正常にインスタンスが生成できた場合＿０８ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStore dsm;
		
		@BeforeClass
		public static void init() throws CrawlerNeo4JException, PageIllegalArgumentException{
			dsm = new Neo4JDataStore(new CrawlerNeo4JParameter("http://localhost:7474/db/data"));
			sut = new GUrl("http://test.com/test1/test1-1?aaa=bbb&ccc=ddd", dsm);
		}
		
		@Test
		public void save() throws CrawlerNeo4JException {
			try {
				dsm.startTransaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (RuntimeException e) {
				fail(e);
			} finally {
				dsm.finishTransaction();
			}
		}
	}
	
	public static class 正常にインスタンスが生成できた場合＿０９ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStore dsm;
		
		@BeforeClass
		public static void init() throws CrawlerNeo4JException, PageIllegalArgumentException{
			dsm = new Neo4JDataStore(new CrawlerNeo4JParameter("http://localhost:7474/db/data"));
			sut = new GUrl("http://test.com/test1/test1-1/test1-1-1?aaa=bbb&ccc=ddd", dsm);
		}
		
		@Test
		public void save() throws CrawlerNeo4JException {
			try {
				dsm.startTransaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (RuntimeException e) {
				fail(e);
			} finally {
				dsm.finishTransaction();
			}
		}
	}
	
	public static class 正常にインスタンスが生成できた場合＿１０ extends CrawlerFoundationTest{
		
		protected static GUrl sut;
		
		protected static Neo4JDataStore dsm;
		
		@BeforeClass
		public static void init() throws CrawlerNeo4JException, PageIllegalArgumentException{
			dsm = new Neo4JDataStore(new CrawlerNeo4JParameter("http://localhost:7474/db/data"));
			sut = new GUrl("http://test.com/test1/test1-1/test1-1-2?aaa=bbb&ccc=ddd", dsm);
		}
		
		@Test
		public void save() throws CrawlerNeo4JException {
			try {
				dsm.startTransaction();
				sut.save();
				dsm.commit();
			} catch (CrawlerSaveException e) {
				fail(e);
			} catch (RuntimeException e) {
				fail(e);
			} finally {
				dsm.finishTransaction();
			}
		}
	}
}
