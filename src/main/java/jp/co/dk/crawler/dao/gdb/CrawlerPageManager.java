package jp.co.dk.crawler.dao.gdb;

import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.PageEventHandler;
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
class CrawlerPageManager extends PageManager {
	
	/** グラフデータベースサービス */
	protected GraphDatabaseService graphDB;
	
	/**
	 * コンストラクタ<p/>
	 * 指定のページを元に本ページ管理マネージャーを生成します。
	 * 
	 * @param graphDB グラフデータベースサービス
	 * @param url URL文字列
	 * @param pageRedirectHandler  ページリダイレクト制御オブジェクト
	 * @param pageEventHandlerList ページイベントハンドラ一覧
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	CrawlerPageManager(GraphDatabaseService graphDB, String url, PageRedirectHandler pageRedirectHandler, List<PageEventHandler> pageEventHandlerList) throws PageIllegalArgumentException, PageAccessException {
		super(url, pageRedirectHandler, pageEventHandlerList);
		this.graphDB = graphDB;
		jp.co.dk.crawler.dao.gdb.Page page = (jp.co.dk.crawler.dao.gdb.Page)super.getPage();
		page.graphDB = graphDB;
	}
	
	/**
	 * コンストラクタ<p/>
	 * 指定のページ、ページ遷移上限数を元に本ページ管理マネージャーを生成します。
	 * 
	 * @param graphDB グラフデータベースサービス
	 * @param url URL文字列
	 * @param pageRedirectHandler ページリダイレクト制御オブジェクト
	 * @param pageEventHandlerList ページイベントハンドラ一覧
	 * @param maxNestLevel ページ遷移上限数
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	CrawlerPageManager(GraphDatabaseService graphDB, String url, PageRedirectHandler pageRedirectHandler, List<PageEventHandler> pageEventHandlerList, int maxNestLevel) throws PageIllegalArgumentException, PageAccessException {
		super(url, pageRedirectHandler, pageEventHandlerList, maxNestLevel);
		this.graphDB = graphDB;
		jp.co.dk.crawler.dao.gdb.Page page = (jp.co.dk.crawler.dao.gdb.Page)super.getPage();
		page.graphDB = graphDB;
	}
	
	/**
	 * コンストラクタ<p/>
	 * 指定のページ、現在のページ遷移数、ページ遷移上限数を元に本ページ管理マネージャーを生成します。
	 * 
	 * @param graphDB              グラフデータベースサービス
	 * @param parentPage           遷移元ページのページマネージャ
	 * @param page                 遷移先のページオブジェクト
	 * @param pageRedirectHandler  ページリダイレクト制御オブジェクト
	 * @param pageEventHandlerList ページイベントハンドラ一覧
	 * @param nestLevel            現在のページ遷移数
	 * @param maxNestLevel         ページ遷移上限数
	 */
	CrawlerPageManager(GraphDatabaseService graphDB, PageManager parentPage, Page page,  PageRedirectHandler pageRedirectHandler, List<PageEventHandler> pageEventHandlerList, int nestLevel, int maxNestLevel){
		super(parentPage, page, pageRedirectHandler, pageEventHandlerList, nestLevel, maxNestLevel);
		this.graphDB = graphDB;
	}
	
	@Override
	public Page createPage(String url) throws PageIllegalArgumentException, PageAccessException {
		return new jp.co.dk.crawler.dao.gdb.Page(url, this.graphDB);
	}
	
	@Override
	protected PageManager createPageManager(PageManager pageManager, Page page, PageRedirectHandler pageRedirectHandler, List<PageEventHandler> pageEventHandlerList, int nextLevel, int maxNestLevel) {
		return new CrawlerPageManager(this.graphDB, pageManager, page, pageRedirectHandler, pageEventHandlerList, nextLevel, maxNestLevel);
	}
}


