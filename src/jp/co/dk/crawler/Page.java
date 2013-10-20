package jp.co.dk.crawler;

import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.browzer.http.header.Header;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.Documents;
import jp.co.dk.crawler.dao.Links;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.Urls;
import jp.co.dk.crawler.dao.record.DocumentsRecord;
import jp.co.dk.crawler.dao.record.PagesRecord;
import jp.co.dk.crawler.dao.record.UrlsRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.ByteDump;

/**
 * PAGEはクローラにて使用される単一おnページを表すクラスです。<p/>
 * 本クラスにて接続先のページ情報のデータストアへの保存、データストアからの読み込みを行います。<br/>
 * 
 */
public class Page extends jp.co.dk.browzer.Page{
	
	/** データストアマネージャ */
	protected DataStoreManager dataStoreManager;
	
	/**
	 * コンストラクタ<p/>
	 * 指定のURL、データストアマネージャのインスタンスを元に、ページオブジェクトのインスタンスを生成します。
	 * 
	 * @param url
	 * @param dataStoreManager
	 * @throws BrowzingException
	 */
	public Page(String url, DataStoreManager dataStoreManager) throws BrowzingException {
		super(url);
		this.dataStoreManager = dataStoreManager;
	}
	
	public Page (String url, Header header, ByteDump data) throws BrowzingException {
		super(url, header, data);
	}
	
	public void save() throws CrawlerException, DataStoreManagerException {
		Urls      urls      = (Urls)      dataStoreManager.getDataAccessObject(CrawlerDaoConstants.URLS);
		Pages     pages     = (Pages)     dataStoreManager.getDataAccessObject(CrawlerDaoConstants.PAGES);
		Documents documents = (Documents) dataStoreManager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
		urls.insert(protocol, host, path, parameter, url, fileid, createDate, updateDate);
		
		
	}
	
	/**
	 * このページを保存している履歴の個数を取得します。
	 * 
	 * @return 履歴を保持している個数
	 * @throws DataStoreManagerException データストアへ対する操作にて例外が発生した場合
	 */
//	public int getCount() throws DataStoreManagerException {
//		Pages pages = (Pages)this.dataStoreManager.getDataAccessObject(CrawlerDaoConstants.PAGES);
//		String protcol = super.protocol;
//		String host    = super.host;
//		List<String> pathList = super.pathList;
//		Map<String, String> parameter = super.parameter;
//		return pages.count(protcol, host, pathList, parameter);
//	}
	
	/**
	 * このページが既にデータストアに保存されているか判定します。<p/>
	 * 既に保存されている場合、trueを返却し、保存されているない場合、falseを返却します。
	 * 
	 * 確認対象は、PAGESテーブルにすでにレコードがあるかどうかで判定を行います。
	 * 
	 * @return 判定結果（true=保存済みである、false=未だに保存されていないページである）
	 * @throws DataStoreManagerException データストアへ対する操作にて例外が発生した場合
	 */
//	public boolean isSaved() throws DataStoreManagerException {
//		if(getCount() != 0) return true;
//		return false;
//	}
	
	/**
	 * このページをデータストアに保存されているものと比較して最新状態であるかどうかを判定します。<p/>
	 * 最新である場合はtrue、最新でない、もしくはページが保存すらされていない場合は、falseを返却します。
	 * @return 判定結果（true=最新である、false=最新でない、またはページが保存されていない）
	 * @throws DataStoreManagerException データストアへ対する操作にて例外が発生した場合
	 */
//	public boolean isLatest() throws DataStoreManagerException {
//		if (!isSaved()) return false;
//		super.header.getResponseRecord();
//	}
	
//	protected UrlsRecord getUrlsRecord() throws DataStoreManagerException {
//		Urls urls = (Urls)this.dataStoreManager.getDataAccessObject(CrawlerDaoConstants.URLS);
//		String protcol = super.protocol;
//		String host    = super.host;
//		List<String> pathList = super.pathList;
//		Map<String, String> parameter = super.parameter;
//		return urls.select(protcol, host, pathList, parameter);
//	}
//	
//	protected List<PagesRecord> getPagesRecord() throws DataStoreManagerException {
//		Pages pages = (Pages)this.dataStoreManager.getDataAccessObject(CrawlerDaoConstants.PAGES);
//		String protcol = super.protocol;
//		String host    = super.host;
//		List<String> pathList = super.pathList;
//		Map<String, String> parameter = super.parameter;
//		return pages.select(protcol, host, pathList, parameter);
//	}
//	
//	protected DocumentsRecord getDocumentsRecord() throws DataStoreManagerException {
//		PagesRecord pagesRecord = getPagesRecord();
//		if (pagesRecord == null) return null;
//		long fileId = pagesRecord.getFileId();
//		long timeId = pagesRecord.getTimeId();
//		Documents documents = (Documents)this.dataStoreManager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
//		return documents.select(fileId, timeId);
//	}
}
