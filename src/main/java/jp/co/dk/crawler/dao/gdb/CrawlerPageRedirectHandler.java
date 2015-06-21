package jp.co.dk.crawler.dao.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.DATASTOREMANAGER_IS_NOT_SET;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.PageEventHandler;
import jp.co.dk.browzer.PageRedirectHandler;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageRedirectException;
import jp.co.dk.browzer.http.header.ResponseHeader;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerPageRedirectHandlerException;

/**
 * CrawlerPageRedirectHandlerは、ページ接続完了後、その結果に対してのイベントを定義するクラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
class CrawlerPageRedirectHandler extends PageRedirectHandler {
	
	/** グラフデータベースサービス */
	protected GraphDatabaseService graphDB;
	
	/**
	 * コンストラクタ<p/>
	 * 指定のイベントハンドラ一覧を元にページリダイレクトハンドラを生成します。
	 * 
	 * @param graphDB グラフデータベースサービス
	 * @param eventHandler イベントハンドラ一覧
	 * @throws CrawlerInitException クローラページリダイレクトハンドラの生成に失敗した場合
	 */
	CrawlerPageRedirectHandler(GraphDatabaseService graphDB, List<PageEventHandler> eventHandler) throws CrawlerInitException {
		super(eventHandler);
		if (graphDB == null) throw new CrawlerInitException(DATASTOREMANAGER_IS_NOT_SET);
		this.graphDB = graphDB;
	}	
	
	@Override
	protected Page ceatePage(String url)  throws PageIllegalArgumentException, PageAccessException  {
		return new jp.co.dk.crawler.dao.gdb.Page(url, this.graphDB);
	}
	
	@Override
	protected Page redirectBy_SERVER_ERROR(ResponseHeader header, Page page) throws PageRedirectException {
		try {
			return super.redirectBy_SERVER_ERROR(header, page);
		} catch (PageRedirectException e) {
			throw new CrawlerPageRedirectHandlerException(e, (jp.co.dk.crawler.rdb.Page)page);
		}
		
	}
	
	@Override
	protected Page redirectBy_CLIENT_ERROR(ResponseHeader header, Page page) throws PageRedirectException {
		try {
			return super.redirectBy_CLIENT_ERROR(header, page);
		} catch (PageRedirectException e) {
			throw new CrawlerPageRedirectHandlerException(e, (jp.co.dk.crawler.rdb.Page)page);
		}
	}
}