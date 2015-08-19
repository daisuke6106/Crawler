package jp.co.dk.crawler.gdb.neo4j;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jp.co.dk.crawler.gdb.neo4j.cypher.Cypher;
import jp.co.dk.crawler.gdb.neo4j.exception.CrawlerNeo4JCypherException;
import jp.co.dk.crawler.gdb.neo4j.exception.CrawlerNeo4JException;
import jp.co.dk.crawler.gdb.neo4j.message.CrawlerNeo4JMessage;
import jp.co.dk.crawler.gdb.neo4j.property.CrawlerNeo4JParameter;

import org.neo4j.graphdb.Node;
import org.neo4j.rest.graphdb.RestAPIFacade;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;

public class Transaction implements Closeable {
	
	/** グラフデータベースサービス */
	protected RestAPIFacade restApiFacade;
	
	/** グラフデータベースサービス */
	protected RestGraphDatabase graphDatabaseService;
	
	/** トランザクション */
	protected org.neo4j.graphdb.Transaction transaction;
	
	Transaction(CrawlerNeo4JParameter parameter) throws CrawlerNeo4JException {
		if (parameter == null) throw new CrawlerNeo4JException(CrawlerNeo4JMessage.NEO4JPARAMETER_IS_NOT_SET);
		if (parameter.isAuthSet()) {
			this.restApiFacade = new RestAPIFacade(parameter.getCrawlerNeo4jServer(), parameter.getCrawlerNeo4jUser() ,parameter.getCrawlerNeo4jPass());
		} else {
			this.restApiFacade = new RestAPIFacade(parameter.getCrawlerNeo4jServer());
		}
		this.graphDatabaseService = new RestGraphDatabase(this.restApiFacade);
		this.transaction          = this.graphDatabaseService.beginTx();
	}
	
	public void commit() {
		this.transaction.success();
	}
	
	public void rollback() {
		this.transaction.failure();
	}
	
	public Node createNode() {
		return this.graphDatabaseService.createNode();
	}
	
	public Node selectNode(Cypher cypher) throws CrawlerNeo4JCypherException {
		if (cypher == null) throw new CrawlerNeo4JCypherException(CrawlerNeo4JMessage.CYPHER_IS_NOT_SET);
		RestCypherQueryEngine queryEngine = new RestCypherQueryEngine(this.restApiFacade);
		return queryEngine.query(cypher.getCypher(), cypher.getParameter()).to(Node.class).single();
	}
	
	public List<Node> selectNodes(Cypher cypher) throws CrawlerNeo4JCypherException {
		if (cypher == null) throw new CrawlerNeo4JCypherException(CrawlerNeo4JMessage.CYPHER_IS_NOT_SET);
		List<Node> nodeList = new ArrayList<>();
		RestCypherQueryEngine queryEngine = new RestCypherQueryEngine(this.restApiFacade);
		Iterator<Node> resultIterator = queryEngine.query(cypher.getCypher(), cypher.getParameter()).to(Node.class).iterator();
		while(resultIterator.hasNext()) nodeList.add(resultIterator.next());
		return nodeList;
	}
	
	@Override
	public void close() {
		this.transaction.close();
		this.graphDatabaseService.shutdown();
	}
	
	@Override
	protected void finalize() { 
		this.close();
	}
}
