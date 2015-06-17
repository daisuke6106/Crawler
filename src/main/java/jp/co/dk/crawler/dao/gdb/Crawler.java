package jp.co.dk.crawler.dao.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.DATASTOREMANAGER_IS_NOT_SET;

import org.neo4j.graphdb.GraphDatabaseService;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.exception.CrawlerInitException;

public class Crawler extends Browzer {
	
	/** グラフデータベースサービス */
	protected GraphDatabaseService graphDB;
	
	public Crawler(String url, GraphDatabaseService graphDB) throws PageIllegalArgumentException, PageAccessException {
		super(url);
		if (graphDB == null) throw new CrawlerInitException(DATASTOREMANAGER_IS_NOT_SET);
		this.graphDB = graphDB;
	}
}
