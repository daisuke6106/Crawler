package jp.co.dk.crawler.db.gdb;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.PageManager;
import jp.co.dk.browzer.PageRedirectHandler;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.db.AbstractPageManager;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;

/**
 * CrawlerPageManagerは、クローラー専用のページ遷移状態管理を行うページ管理クラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
class GCrawlerPageManager extends AbstractPageManager {
	
	/** データストアマネージャ */
	protected Neo4JDataStoreManager dsm;
	
	/**
	 * コンストラクタ<p/>
	 * 指定のページを元に本ページ管理マネージャーを生成します。
	 * 
	 * @param dsm データストアマネージャ
	 * @param url URL文字列
	 * @param pageRedirectHandler  ページリダイレクト制御オブジェクト
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	GCrawlerPageManager(Neo4JDataStoreManager dsm, String url, PageRedirectHandler pageRedirectHandler) throws PageIllegalArgumentException, PageAccessException {
		super(url, pageRedirectHandler);
		this.dsm = dsm;
		((jp.co.dk.crawler.db.gdb.GPage)this.getPage()).setDataStoreManager(dsm);
	}
	
	/**
	 * コンストラクタ<p/>
	 * 指定のページ、ページ遷移上限数を元に本ページ管理マネージャーを生成します。
	 * 
	 * @param dsm データストアマネージャ
	 * @param url URL文字列
	 * @param pageRedirectHandler ページリダイレクト制御オブジェクト
	 * @param maxNestLevel ページ遷移上限数
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	GCrawlerPageManager(Neo4JDataStoreManager dsm, String url, PageRedirectHandler pageRedirectHandler, int maxNestLevel) throws PageIllegalArgumentException, PageAccessException {
		super(url, pageRedirectHandler, maxNestLevel);
		this.dsm = dsm;
		((jp.co.dk.crawler.db.gdb.GPage)this.getPage()).setDataStoreManager(dsm);
	}
	
	/**
	 * コンストラクタ<p/>
	 * 指定のページ、現在のページ遷移数、ページ遷移上限数を元に本ページ管理マネージャーを生成します。
	 * 
	 * @param dsm                  データストアマネージャ
	 * @param parentPage           遷移元ページのページマネージャ
	 * @param page                 遷移先のページオブジェクト
	 * @param pageRedirectHandler  ページリダイレクト制御オブジェクト
	 * @param pageEventHandlerList ページイベントハンドラ一覧
	 * @param nestLevel            現在のページ遷移数
	 * @param maxNestLevel         ページ遷移上限数
	 */
	GCrawlerPageManager(Neo4JDataStoreManager dsm, PageManager parentPage, Page page,  PageRedirectHandler pageRedirectHandler, int nestLevel, int maxNestLevel){
		super(parentPage, page, pageRedirectHandler, nestLevel, maxNestLevel);
		this.dsm = dsm;
		((jp.co.dk.crawler.db.gdb.GPage)this.getPage()).setDataStoreManager(dsm);
	}
	
	/**
	 * データストアマネージャを設定します。
	 * @param dsm データストアマネージャ
	 */
	void setDataStoreManager(Neo4JDataStoreManager dsm) {
		this.dsm = dsm;
		((GPage)this.page).setDataStoreManager(dsm);
	}
	
	@Override
	public Page createPage(String url) throws PageIllegalArgumentException, PageAccessException {
		return new GPage(url, this.dsm);
	}
	
	@Override
	protected PageManager createPageManager(PageManager pageManager, Page page, PageRedirectHandler pageRedirectHandler, int nextLevel, int maxNestLevel) {
		return new GCrawlerPageManager(this.dsm, pageManager, page, pageRedirectHandler, nextLevel, maxNestLevel);
	}
}


