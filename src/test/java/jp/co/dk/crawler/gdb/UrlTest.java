package jp.co.dk.crawler.gdb;

import static org.junit.Assert.*;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.gdb.Url;
import jp.co.dk.crawler.rdb.CrawlerFoundationTest;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class UrlTest extends CrawlerFoundationTest{

	@Test
	public void save() {
		GraphDatabaseService graphDB = new GraphDatabaseFactory().newEmbeddedDatabase("/tmp/neo4j_sample");
		Transaction tx = graphDB.beginTx();
		try {
			Url url1 = new Url("http://test.com", graphDB);
			url1.save();
			Url url2 = new Url("http://test.com/path1", graphDB);
			url2.save();
			Url url3 = new Url("http://test.com/path2", graphDB);
			url3.save();
			Url url4 = new Url("http://test.com/path1/path1-1", graphDB);
			url4.save();
			Url url5 = new Url("http://test.com/path1/path1-1/test.html", graphDB);
			url5.save();
			Url url6 = new Url("http://test.com/path1/path1-1/test.html?key1=value1", graphDB);
			url6.save();
			Url url7 = new Url("http://test.com/path1/path1-1/test.html?key1=value1&key2=value2", graphDB);
			url7.save();
			tx.success();
		} catch (PageIllegalArgumentException e) {
			fail(e);
		} finally {
			tx.finish();
			graphDB.shutdown();
		}
	}

}
