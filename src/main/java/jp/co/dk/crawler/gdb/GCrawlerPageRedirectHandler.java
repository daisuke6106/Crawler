package jp.co.dk.crawler.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

import java.util.List;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.PageEventHandler;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageRedirectException;
import jp.co.dk.browzer.http.header.ResponseHeader;
import jp.co.dk.crawler.AbstractPageRedirectHandler;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerPageRedirectHandlerException;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;

/**
 * CrawlerPageRedirectHandlerは、ページ接続完了後、その結果に対してのイベントを定義するクラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
class GCrawlerPageRedirectHandler extends AbstractPageRedirectHandler {
	
	protected Neo4JDataStoreManager dsm;
	
	/**
	 * コンストラクタ<p/>
	 * 指定のイベントハンドラ一覧を元にページリダイレクトハンドラを生成します。
	 * 
	 * @param dsm データストアマネージャ
	 * @param eventHandler イベントハンドラ一覧
	 * @throws CrawlerInitException クローラページリダイレクトハンドラの生成に失敗した場合
	 */
	GCrawlerPageRedirectHandler(List<PageEventHandler> eventHandler) throws CrawlerInitException {
		super(eventHandler);
	}
	
	/**
	 * コンストラクタ<p/>
	 * 指定のイベントハンドラ一覧を元にページリダイレクトハンドラを生成します。
	 * 
	 * @param dsm データストアマネージャ
	 * @param eventHandler イベントハンドラ一覧
	 * @throws CrawlerInitException クローラページリダイレクトハンドラの生成に失敗した場合
	 */
	GCrawlerPageRedirectHandler(Neo4JDataStoreManager dsm, List<PageEventHandler> eventHandler) throws CrawlerInitException {
		super(eventHandler);
		this.dsm = dsm;
	}
	
	void setDataStoreManager(Neo4JDataStoreManager dsm) {
		this.dsm = dsm;
	}
	
	@Override
	protected Page ceatePage(String url)  throws PageIllegalArgumentException, PageAccessException  {
		return new GPage(url, this.dsm);
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