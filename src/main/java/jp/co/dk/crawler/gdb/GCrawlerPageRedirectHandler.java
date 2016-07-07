package jp.co.dk.crawler.gdb;

import jp.co.dk.browzer.Page;
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
	
	/** データストアマネージャー */
	protected Neo4JDataStoreManager dsm;
	
	/**
	 * コンストラクタ<p/>
	 * 指定のイベントハンドラ一覧を元にページリダイレクトハンドラを生成します。
	 * 
	 * @param dsm データストアマネージャ
	 * @throws CrawlerInitException クローラページリダイレクトハンドラの生成に失敗した場合
	 */
	GCrawlerPageRedirectHandler(Neo4JDataStoreManager dsm) throws CrawlerInitException {
		super();
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
	protected Page redirectBy_INFOMATIONAL(ResponseHeader header, Page page) throws PageRedirectException {
		try {
			return super.redirectBy_INFOMATIONAL(header, page);
		} catch (PageRedirectException e) {
			throw new CrawlerPageRedirectHandlerException(e, page);
		}
	}
	
	@Override
	protected Page redirectBy_SUCCESS(ResponseHeader header, Page page) throws PageRedirectException {
		try {
			return super.redirectBy_SUCCESS(header, page);
		} catch (PageRedirectException e) {
			throw new CrawlerPageRedirectHandlerException(e, page);
		}
	}
	
	@Override
	protected Page redirectBy_REDIRECTION(ResponseHeader header, Page page) throws PageRedirectException {
		try {
			return super.redirectBy_REDIRECTION(header, page);
		} catch (PageRedirectException e) {
			throw new CrawlerPageRedirectHandlerException(e, page);
		}
	}

	@Override
	protected Page redirectBy_CLIENT_ERROR(ResponseHeader header, Page page) throws PageRedirectException {
		try {
			return super.redirectBy_CLIENT_ERROR(header, page);
		} catch (PageRedirectException e) {
			throw new CrawlerPageRedirectHandlerException(e, page);
		}
	}
	
	@Override
	protected Page redirectBy_SERVER_ERROR(ResponseHeader header, Page page) throws PageRedirectException {
		try {
			return super.redirectBy_SERVER_ERROR(header, page);
		} catch (PageRedirectException e) {
			throw new CrawlerPageRedirectHandlerException(e, page);
		}
		
	}
	
}