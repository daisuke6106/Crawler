package jp.co.dk.crawler.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.*;
import jp.co.dk.browzer.PageRedirectHandler;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.AbstractCrawler;
import jp.co.dk.crawler.AbstractPageManager;
import jp.co.dk.crawler.AbstractPageRedirectHandler;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.message.CrawlerMessage;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStore;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.cypher.Cypher;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerCypherException;

/**
 * Crawlerは、ネットワーク上に存在するHTML、XML、ファイルを巡回し、指定された出力先へ保存を行う処理を制御するクラス。<p/>
 * 保存先には、MySql等のRDBのデータストアに保存される。<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class GCrawler extends AbstractCrawler {
	
	/** データストアマネージャー */
	protected Neo4JDataStoreManager dsm;
	
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
	public GCrawler(String url, Neo4JDataStoreManager dataStoreManager) throws CrawlerInitException, PageIllegalArgumentException, PageAccessException { 
		super(url);
		if (dataStoreManager == null) throw new CrawlerInitException(CrawlerMessage.DATASTOREMANAGER_IS_NOT_SET);
		this.dsm = dataStoreManager;
		((GCrawlerPageManager)this.pageManager).setDataStoreManager(dataStoreManager);
		((GCrawlerPageRedirectHandler)this.pageRedirectHandler).setDataStoreManager(dataStoreManager);
		
	}
	
	public void saveAllUrl() throws PageAccessException, PageIllegalArgumentException, DocumentException, CrawlerSaveException, Neo4JDataStoreManagerCypherException {
		GPage activePage = (GPage)this.getPage();
		activePage.saveAllUrl();
	}
	
	@Override
	public boolean save() throws CrawlerSaveException {
		GPage activePage = (GPage)this.getPage();
		activePage.save();
		
		GPage beforePage = (GPage)this.pageManager.getParentPage();
		if (beforePage != null) {
			try {
				String beforePage_LatestAccessDate = beforePage.getLatestAccessDate();
				String activePage_LatestAccessDate = activePage.getLatestAccessDate();
				
				Neo4JDataStore dataStore = this.dsm.getDataAccessObject("PAGE");
				Cypher pageData = new Cypher("MATCH(beforepage:PAGE) WHERE beforepage.accessdate=?").setParameter(beforePage_LatestAccessDate);
				pageData.append("MATCH(activepage:PAGE) WHERE activepage.accessdate=?").setParameter(activePage_LatestAccessDate);
				pageData.append("CREATE(beforepage)-[:LINK]->(activepage)");
				dataStore.selectNode(pageData);
			} catch (Neo4JDataStoreManagerCypherException e) {
				throw new CrawlerSaveException(DATASTOREMANAGER_CAN_NOT_CREATE);
			}
		}
		return true;
	}
	
	@Override
	protected AbstractPageManager createPageManager(String url, PageRedirectHandler handler) throws PageIllegalArgumentException, PageAccessException {
		return new GCrawlerPageManager(this.dsm, url, handler);
	}
	
	@Override
	protected AbstractPageManager createPageManager(String url, PageRedirectHandler handler, int maxNestLevel) throws PageIllegalArgumentException, PageAccessException {
		return new GCrawlerPageManager(this.dsm, url, handler, maxNestLevel);
	}

	@Override
	protected AbstractPageRedirectHandler createPageRedirectHandler() {
		return new GCrawlerPageRedirectHandler(this.dsm);
	}
	
	/**
	 * データストアマネージャを設定します。
	 * @param dsm データストアマネージャ
	 */
	public Neo4JDataStoreManager getDataStoreManager() {
		return this.dsm;
	}
}