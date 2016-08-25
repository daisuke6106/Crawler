package jp.co.dk.crawler.db.rdb.controler;

import jp.co.dk.crawler.db.rdb.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.db.rdb.dao.CrawlerErrors;
import jp.co.dk.crawler.db.rdb.dao.Documents;
import jp.co.dk.crawler.db.rdb.dao.Links;
import jp.co.dk.crawler.db.rdb.dao.Pages;
import jp.co.dk.crawler.db.rdb.dao.RedirectErrors;
import jp.co.dk.crawler.db.rdb.dao.Urls;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;

public class CrawlerInitialize {
	
	/**
	 * 環境作成を実行する。<p/>
	 * プロパティに定義されている接続先のデータベースに対して、以下のテーブルを作成する。<br/>
	 * ・LINKS<br/>
	 * ・URLS<br/>
	 * ・PAGES<br/>
	 * ・DOCUMENTS<br/>
	 * ・REDIRECT_ERRORS<br/>
	 * ・CRAWLER_ERRORS<br/>
	 * 
	 * @throws DataStoreManagerException テーブルの作成に失敗した場合
	 */
	public static void main(String[] args) {
		try {
			DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty();
			DataStoreManager dataStoreManager = new DataStoreManager(dataStoreManagerProperty);
			dataStoreManager.startTrunsaction();
			Links          links          = (Links)         dataStoreManager.getDataAccessObject(CrawlerDaoConstants.LINKS);
			Urls           urls           = (Urls)          dataStoreManager.getDataAccessObject(CrawlerDaoConstants.URLS);
			Pages          pages          = (Pages)         dataStoreManager.getDataAccessObject(CrawlerDaoConstants.PAGES);
			Documents      documents      = (Documents)     dataStoreManager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
			RedirectErrors redirectErrors = (RedirectErrors)dataStoreManager.getDataAccessObject(CrawlerDaoConstants.REDIRECT_ERRORS);
			CrawlerErrors  crawlerErrors  = (CrawlerErrors) dataStoreManager.getDataAccessObject(CrawlerDaoConstants.CRAWLER_ERRORS);
			links.createTable();
			urls.createTable();
			pages.createTable();
			documents.createTable();
			redirectErrors.createTable();
			crawlerErrors.createTable();
			dataStoreManager.finishTrunsaction();
		} catch (DataStoreManagerException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
}
