package jp.co.dk.crawler.db.gdb.controler;

import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.controler.AbstractCrawlerScenarioControler;
import jp.co.dk.crawler.db.AbstractCrawler;
import jp.co.dk.crawler.db.gdb.GCrawler;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.scenario.MoveScenario;
import jp.co.dk.crawler.scenario.RegExpMoveScenario;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerException;
import jp.co.dk.neo4jdatastoremanager.property.Neo4JDataStoreManagerProperty;
import jp.co.dk.property.exception.PropertyException;

public class GCrawlerMain extends AbstractCrawlerScenarioControler {

	protected Neo4JDataStoreManager dsm;
	
	{
		try {
			this.dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty());
		} catch (Neo4JDataStoreManagerException | PropertyException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		new GCrawlerMain().execute(args);
	}
	
	@Override
	public AbstractCrawler createBrowzer(String url) throws CrawlerInitException, PageIllegalArgumentException, PageAccessException {
		return new GCrawler(url, this.dsm);
	}

}

