package jp.co.dk.crawler;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.PageManager;
import jp.co.dk.browzer.PageRedirectHandler;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;

/**
 * CrawlerPageManagerは、クローラー専用のページ遷移状態管理を行うページ管理クラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class AbstractPageManager extends PageManager {
	
	/**
	 * コンストラクタ<p/>
	 * 指定のページを元に本ページ管理マネージャーを生成します。
	 * 
	 * @param url URL文字列
	 * @param pageRedirectHandler  ページリダイレクト制御オブジェクト
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	protected AbstractPageManager(String url, PageRedirectHandler pageRedirectHandler) throws PageIllegalArgumentException, PageAccessException {
		super(url, pageRedirectHandler);
	}
	
	/**
	 * コンストラクタ<p/>
	 * 指定のページ、ページ遷移上限数を元に本ページ管理マネージャーを生成します。
	 * 
	 * @param url URL文字列
	 * @param pageRedirectHandler ページリダイレクト制御オブジェクト
	 * @param maxNestLevel ページ遷移上限数
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	protected AbstractPageManager(String url, PageRedirectHandler pageRedirectHandler, int maxNestLevel) throws PageIllegalArgumentException, PageAccessException {
		super(url, pageRedirectHandler, maxNestLevel);
	}
	
	/**
	 * コンストラクタ<p/>
	 * 指定のページ、現在のページ遷移数、ページ遷移上限数を元に本ページ管理マネージャーを生成します。
	 * 
	 * @param parentPage           遷移元ページのページマネージャ
	 * @param page                 遷移先のページオブジェクト
	 * @param pageRedirectHandler  ページリダイレクト制御オブジェクト
	 * @param pageEventHandlerList ページイベントハンドラ一覧
	 * @param nestLevel            現在のページ遷移数
	 * @param maxNestLevel         ページ遷移上限数
	 */
	protected AbstractPageManager(PageManager parentPage, Page page,  PageRedirectHandler pageRedirectHandler, int nestLevel, int maxNestLevel){
		super(parentPage, page, pageRedirectHandler, nestLevel, maxNestLevel);
	}
	
	@Override
	protected abstract PageManager createPageManager(PageManager pageManager, Page page, PageRedirectHandler pageRedirectHandler, int nextLevel, int maxNestLevel) ;
	
	@Override
	public abstract Page createPage(String url) throws PageIllegalArgumentException, PageAccessException;
	
}


