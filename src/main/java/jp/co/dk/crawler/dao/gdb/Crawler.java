package jp.co.dk.crawler.dao.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.DATASTOREMANAGER_IS_NOT_SET;

import java.util.ArrayList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.PageEventHandler;
import jp.co.dk.browzer.PageManager;
import jp.co.dk.browzer.PageRedirectHandler;
import jp.co.dk.browzer.event.PrintPageEventHandler;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageMovableLimitException;
import jp.co.dk.browzer.exception.PageRedirectException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.exception.CrawlerInitException;

public class Crawler extends Browzer {
	
	/** グラフデータベースサービス */
	protected GraphDatabaseService graphDB;
	
	public Crawler(String url, GraphDatabaseService graphDB) throws PageIllegalArgumentException, PageAccessException {
		super(url);
		if (graphDB == null) throw new CrawlerInitException(DATASTOREMANAGER_IS_NOT_SET);
		this.graphDB = graphDB;
	}
	
	@Override
	public Page move(MovableElement movable) throws PageIllegalArgumentException, PageAccessException, PageRedirectException, PageMovableLimitException {
		return null;
	}
	
	@Override
	protected PageManager createPageManager(String url, PageRedirectHandler handler) throws PageIllegalArgumentException, PageAccessException {
		return new CrawlerPageManager(this.graphDB, url, handler, this.pageEventHandlerList);
	}
	
	@Override
	protected PageManager createPageManager(String url, PageRedirectHandler handler, int maxNestLevel) throws PageIllegalArgumentException, PageAccessException {
		return new CrawlerPageManager(this.graphDB, url, handler, this.pageEventHandlerList, maxNestLevel);
	}
	
	@Override
	protected PageRedirectHandler createPageRedirectHandler() {
		return new CrawlerPageRedirectHandler(this.graphDB, this.getPageEventHandler());
	}
	
	@Override
	protected List<PageEventHandler> getPageEventHandler() {
		List<PageEventHandler> list = new ArrayList<PageEventHandler>();
		list.add(new PrintPageEventHandler());
		return list;
	}
}
