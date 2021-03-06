package jp.co.dk.crawler;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.PageRedirectHandler;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.exception.CrawlerInitException;

/**
 * CrawlerPageRedirectHandlerは、ページ接続完了後、その結果に対してのイベントを定義するクラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class CrawlerPageRedirectHandler extends PageRedirectHandler {
	
	/**
	 * コンストラクタ<p/>
	 * 指定のイベントハンドラ一覧を元にページリダイレクトハンドラを生成します。
	 * 
	 * @param dsm データストアマネージャ
	 * @throws CrawlerInitException クローラページリダイレクトハンドラの生成に失敗した場合
	 */
	protected CrawlerPageRedirectHandler() throws CrawlerInitException {
		super();
	}	
	
	@Override
	protected Page ceatePage(String url) throws PageIllegalArgumentException, PageAccessException {
		return new CrawlerPage(url);
	}
}