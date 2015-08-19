package jp.co.dk.crawler.gdb.neo4j;

import static org.junit.Assert.*;

import java.util.List;

import jp.co.dk.crawler.gdb.neo4j.cypher.Cypher;
import jp.co.dk.crawler.gdb.neo4j.exception.CrawlerNeo4JCypherException;
import jp.co.dk.crawler.gdb.neo4j.exception.CrawlerNeo4JException;
import jp.co.dk.crawler.gdb.neo4j.property.CrawlerNeo4JParameter;

import org.junit.Test;

public class Neo4JDataStoreTest {

	@Test
	public void test() throws CrawlerNeo4JCypherException {
		
		try {
			CrawlerNeo4JParameter parameter = new CrawlerNeo4JParameter("http://localhost:7474/db/data");
			Neo4JDataStore neo4JDataStore = new Neo4JDataStore(parameter);
			neo4JDataStore.startTransaction();
			
			Node node = neo4JDataStore.createNode();
			node.setProperty("aaa", "bbb");
			
			List<Node> selectNodeList = neo4JDataStore.selectNodes(new Cypher("MATCH(n) RETURN n"));
			for(Node selectNode : selectNodeList) {
				System.out.println(selectNode.getProperty("aaa"));
			}
			
			neo4JDataStore.commit();
			neo4JDataStore.finishTransaction();
			
		} catch (CrawlerNeo4JException e) {
			e.printStackTrace();
		}
		
	}

}
