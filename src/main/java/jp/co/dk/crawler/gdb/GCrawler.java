package jp.co.dk.crawler.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.DATASTOREMANAGER_CAN_NOT_CREATE;
import static jp.co.dk.crawler.message.CrawlerMessage.FAILE_TO_GET_PAGE;
import jp.co.dk.browzer.PageRedirectHandler;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.AbstractCrawler;
import jp.co.dk.crawler.AbstractPage;
import jp.co.dk.crawler.AbstractPageManager;
import jp.co.dk.crawler.AbstractPageRedirectHandler;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.message.CrawlerMessage;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.gdb.Cypher;

/**
 * Crawlerは、ネットワーク上に存在するHTML、XML、ファイルを巡回し、指定された出力先へ保存を行う処理を制御するクラス。<p/>
 * 保存先には、MySql等のRDBのデータストアに保存される。<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class GCrawler extends AbstractCrawler {
	
	/** データストアマネージャー */
	protected DataStoreManager dsm;
	
	/**
	 * コンストラクタ<p/>
	 * 指定のデータストアマネージャを元に、クローラクラスのインスタンスを生成する。<br/>
	 * データストアマネージャが設定されていなかった場合、例外を送出する。
	 * 
	 * @param dataStoreManager データストアマネージャ
	 * @throws CrawlerInitException クローラの初期化処理に失敗した場合
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	public GCrawler(String url, DataStoreManager dataStoreManager) throws CrawlerInitException, PageIllegalArgumentException, PageAccessException { 
		super(url);
		if (dataStoreManager == null) throw new CrawlerInitException(CrawlerMessage.DATASTOREMANAGER_IS_NOT_SET);
		this.dsm                 = dataStoreManager;
		this.pageRedirectHandler = this.createPageRedirectHandler();
		super.pageManager        = this.createPageManager(url, this.pageRedirectHandler);
	}
	
	@Override
	public boolean save() throws CrawlerSaveException {
		GPage beforePage = (GPage)this.pageManager.getParentPage();
		GPage activePage = (GPage)this.getPage();
		activePage.save();
		if (beforePage != null) {
			int beforePageID = beforePage.getLatestID();
			int activePageID = activePage.getLatestID();
			try {
				jp.co.dk.datastoremanager.gdb.AbstractDataBaseAccessObject dataStore = (jp.co.dk.datastoremanager.gdb.AbstractDataBaseAccessObject)this.dsm.getDataAccessObject("PAGE");
				Cypher pageData = new Cypher("MATCH(beforepage:PAGE) WHERE ID(beforepage)=? MATCH(activepage:PAGE) WHERE ID(activepage)=? CREATE(beforepage)-[:LINK]->(activepage)");
				pageData.setParameter(beforePageID);
				pageData.setParameter(activePageID);
				dataStore.execute(pageData);
			} catch (ClassCastException | DataStoreManagerException e) {
				throw new CrawlerSaveException(DATASTOREMANAGER_CAN_NOT_CREATE);
			}
		}
		return true;
	}
	
	@Override
	protected AbstractPageManager createPageManager(String url, PageRedirectHandler handler) throws PageIllegalArgumentException, PageAccessException {
		return new GCrawlerPageManager(this.dsm, url, handler, this.pageEventHandlerList);
	}
	
	@Override
	protected AbstractPageManager createPageManager(String url, PageRedirectHandler handler, int maxNestLevel) throws PageIllegalArgumentException, PageAccessException {
		return new GCrawlerPageManager(this.dsm, url, handler, this.pageEventHandlerList, maxNestLevel);
	}

	@Override
	protected AbstractPageRedirectHandler createPageRedirectHandler() {
		return new GCrawlerPageRedirectHandler(this.dsm, this.getPageEventHandler());
	}
	
	public DataStoreManager getDataStoreManager() {
		return this.dsm;
	}
}