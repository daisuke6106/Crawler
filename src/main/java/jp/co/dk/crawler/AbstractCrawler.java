package jp.co.dk.crawler;

import java.util.ArrayList;
import java.util.List;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.PageEventHandler;
import jp.co.dk.browzer.PageRedirectHandler;
import jp.co.dk.browzer.event.PrintPageEventHandler;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.eventhandler.LogPageEventHandler;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerSaveException;

public abstract class AbstractCrawler extends Browzer {
	
	/**
	 * コンストラクタ<p/>
	 * 指定のＵＲＬ、クローラクラスのインスタンスを生成する。
	 * 
	 * @param url 接続先URL
	 * @throws CrawlerInitException クローラの初期化処理に失敗した場合
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	public AbstractCrawler(String url) throws CrawlerInitException, PageIllegalArgumentException, PageAccessException { 
		super(url);
	}
	
	/**
	 * 現在アクティブになっているページを保存する。
	 * 
	 * @return 保存結果（true=保存された、false=すでにデータが存在するため、保存されなかった）
	 * @throws CrawlerException データストアの登録時に必須項目が設定されていなかった場合
	 */
	public abstract boolean save() throws CrawlerSaveException;
	
	@Override
	protected abstract AbstractPageManager createPageManager(String url, PageRedirectHandler handler) throws PageIllegalArgumentException, PageAccessException ;
	
	@Override
	protected abstract AbstractPageManager createPageManager(String url, PageRedirectHandler handler, int maxNestLevel) throws PageIllegalArgumentException, PageAccessException ;
	
	@Override
	protected abstract AbstractPageRedirectHandler createPageRedirectHandler();
	
	@Override
	protected List<PageEventHandler> getPageEventHandler() {
		List<PageEventHandler> list = new ArrayList<PageEventHandler>();
		list.add(new PrintPageEventHandler());
		list.add(new LogPageEventHandler());
		return list;
	}
}
