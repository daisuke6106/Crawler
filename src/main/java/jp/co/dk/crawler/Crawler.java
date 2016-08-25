package jp.co.dk.crawler;

import java.util.HashSet;
import java.util.Set;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.PageManager;
import jp.co.dk.browzer.PageRedirectHandler;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageMovableLimitException;
import jp.co.dk.browzer.exception.PageRedirectException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.scenario.MoveControl;
import jp.co.dk.crawler.scenario.MoveResult;
import jp.co.dk.crawler.scenario.QueueTask;

public class Crawler extends Browzer {
	
	/** 訪問URL */
	protected Set<String> visitedUrl = new HashSet<>();
	
	/** 訪問済みURL（正常） */
	protected Set<String> visitSuccessUrl = new HashSet<>();
	
	/** 訪問済みURL（エラー） */
	protected Set<String> visitErrorUrl = new HashSet<>();
	
	/**
	 * コンストラクタ<p/>
	 * 指定のＵＲＬ、クローラクラスのインスタンスを生成する。
	 * 
	 * @param url 接続先URL
	 * @throws CrawlerInitException クローラの初期化処理に失敗した場合
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	public Crawler(String url) throws CrawlerInitException, PageIllegalArgumentException, PageAccessException { 
		super(url);
	}
	
	public MoveResult move(QueueTask queueTask) throws MoveActionFatalException, MoveActionException {
		MovableElement movableElement = queueTask.getMovableElement();
		if (queueTask.beforeAction(this) == MoveControl.Transition) {
			try {
				this.move(movableElement);
				queueTask.afterAction(this);
				return MoveResult.SuccessfullTransition;
			} catch (PageIllegalArgumentException | PageAccessException | PageRedirectException | PageMovableLimitException e) {
				queueTask.errorAction(this);
				return MoveResult.FailureToTransition;
			}
		} else {
			return MoveResult.UnAuthorizedTransition;
		}
	}

	@Override
	public Page move(MovableElement movable) throws PageIllegalArgumentException, PageAccessException, PageRedirectException, PageMovableLimitException {
		this.visitedUrl.add(movable.getUrl());
		try {
			Page page = super.move(movable);
			this.visitSuccessUrl.add(movable.getUrl());
			return page;
		} catch (PageIllegalArgumentException | PageAccessException | PageRedirectException | PageMovableLimitException e) {
			this.visitErrorUrl.add(movable.getUrl());
			throw e;
		}
	}
	
	/**
	 * このページが訪問済みかどうか判定します。
	 * 
	 * @param movable 遷移先要素
	 * @return 判定結果（true=訪問済み、false=未訪問）
	 */
	public boolean isVisited(MovableElement movable) {
		return this.visitedUrl.contains(movable.getUrl());
	}
	
	@Override
	protected PageManager createPageManager(String url, PageRedirectHandler handler) throws PageIllegalArgumentException, PageAccessException {
		return new CrawlerPageManager(url, handler);
	}
	
	@Override
	protected PageManager createPageManager(String url, PageRedirectHandler handler, int maxNestLevel) throws PageIllegalArgumentException, PageAccessException {
		return new CrawlerPageManager(url, handler, maxNestLevel);
	}
	
	@Override
	protected PageRedirectHandler createPageRedirectHandler() {
		return new CrawlerPageRedirectHandler();
	}
	
}
