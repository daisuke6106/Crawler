package jp.co.dk.crawler;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.Documents;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.Urls;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.ByteDump;

/**
 * PAGEはクローラにて使用される単一のページを表すクラスです。<p/>
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
	 * @param url              URL文字列
	 * @param dataStoreManager データストアマネージャ
	 * @throws BrowzingException ページ情報の読み込みに失敗した場合
	 */
	public Page(String url, DataStoreManager dataStoreManager) throws BrowzingException {
		super(url);
		this.dataStoreManager = dataStoreManager;
	}
	
	/**
	 * コンストラクタ<p/>
	 * すでに保存済みのデータからページのインスタンスを生成します。<br/>
	 * URLのプロトコルが未知、リクエストヘッダ、レスポンスヘッダが不正である場合などは、例外が発生します。
	 * 
	 * @param url            URL文字列
	 * @param requestHeader  リクエストヘッダ
	 * @param responseHeader レスポンスヘッダ
	 * @param data           ページデータ
	 * @throws BrowzingException インスタンスの生成に失敗した場合
	 */
	public Page (String url, Map<String, String> requestHeader, Map<String, List<String>> responseHeader, ByteDump data) throws BrowzingException {
		super(url, requestHeader, responseHeader, data);
	}
	
	public void save() throws CrawlerException, DataStoreManagerException {
		this.dataStoreManager.startTrunsaction();
		Urls      urls      = (Urls)      dataStoreManager.getDataAccessObject(CrawlerDaoConstants.URLS);
		Pages     pages     = (Pages)     dataStoreManager.getDataAccessObject(CrawlerDaoConstants.PAGES);
		Documents documents = (Documents) dataStoreManager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
		String url                    = super.getURL();
		String protocol               = super.getProtocol();
		String host                   = super.getHost();
		List<String> pathList         = super.getPathList(super.getURLObject());
		String filename               = super.getFileName();
		String extension              = super.getExtension();
		Map<String,String> parameter  = super.getParameter();
		byte[] data                   = super.byteDump.getBytes();
		long fileid                   = getFileId();
		long timeid                   = getTimeId();
		Date createDate               = getCreateDate();
		Date updateDate               = getUpdateDate();
		Map<String,String>       requestHeader  = super.requestHeader.getHeaderMap();
		Map<String,List<String>> responseHeader = super.responseHeader.getHeaderMap();
		urls.insert(protocol, host, pathList, filename, parameter, url, fileid, createDate, updateDate);
		pages.insert(protocol, host, pathList, filename, parameter, requestHeader, responseHeader, fileid, timeid, createDate, updateDate);
		documents.insert(fileid, timeid, filename, extension, createDate, data, createDate, updateDate);
		this.dataStoreManager.finishTrunsaction();
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
//		super.responseHeader.getResponseRecord();
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
	
	/**
	 * このページのURLからページを一意に特定するためのファイルIDを算出し返却します。<p/>
	 * 算出はこのページの<br/>
	 * ・プロトコル<br/>
	 * ・ホスト名<br/>
	 * ・パス<br/>
	 * ・ファイル名<br/>
	 * ・パラメータ<br/>
	 * のハッシュ値を上記の順で乗算した結果が返却されます。
	 * 
	 * @return ファイルID
	 */
	protected long getFileId() {
		String protocol              = super.getProtocol();
		String host                  = super.getHost();
		List<String> pathList        = super.getPathList(super.getURLObject());
		String filename              = super.getFileName();
		Map<String,String> parameter = super.getParameter();
		BigDecimal result = new BigDecimal(protocol.hashCode());
		result = result.multiply(new BigDecimal(host.hashCode()));
		result = result.multiply(new BigDecimal(pathList.hashCode()));
		result = result.multiply(new BigDecimal(filename.hashCode()));
		result = result.multiply(new BigDecimal(parameter.hashCode()));
		return result.longValue();
	}
	
	/**
	 * この現在の時刻からファイルIDに付随する登録時刻であるタイムIDを算出し返却します。<p/>
	 * デフォルトではJVMが稼働しているマシンの現在日付をlong値に変換した値が返却されます。<br/>
	 * 変更する場合はオーバーライドを使用してください。
	 * 
	 * @return タイムID
	 */
	protected long getTimeId() {
		return new Date().getTime();
	}
	
	/**
	 * 作成日付を生成し、返却します。<p/>
	 * データストアに登録される際の作成日付を返却します。<br/>
	 * デフォルトではJVMが稼働しているマシンの現在日付が返却されます。<br/>
	 * 変更する場合はオーバーライドを使用してください。
	 * 
	 * @return 作成日付
	 */
	protected Date getCreateDate() {
		return new Date();
	}
	
	/**
	 * 更新日付を生成し、返却します。<p/>
	 * データストアに登録される際の更新日付を返却します。<br/>
	 * デフォルトではJVMが稼働しているマシンの現在日付が返却されます。<br/>
	 * 変更する場合はオーバーライドを使用してください。
	 * 
	 * @return 更新日付
	 */
	protected Date getUpdateDate() {
		return new Date();
	}
}
