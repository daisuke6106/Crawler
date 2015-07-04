package jp.co.dk.crawler.gdb;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.AbstractPage;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.rdb.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.rdb.dao.Documents;
import jp.co.dk.crawler.rdb.dao.Pages;
import jp.co.dk.crawler.rdb.dao.Urls;
import jp.co.dk.crawler.rdb.dao.record.DocumentsRecord;
import jp.co.dk.datastoremanager.DaoConstants;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.exception.DocumentFatalException;
import static jp.co.dk.crawler.message.CrawlerMessage.*;

/**
 * PAGEはクローラにて使用される単一のページを表すクラスです。<p/>
 * 本クラスにて接続先のページ情報のデータストアへの保存、データストアからの読み込みを行います。<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class Page extends AbstractPage implements DaoConstants {
	
	/** データストアマネージャ */
	protected DataStoreManager dataStoreManager;
	
	/**
	 * コンストラクタ<p/>
	 * 指定のURL、データストアマネージャのインスタンスを元に、ページオブジェクトのインスタンスを生成します。
	 * 
	 * @param url              URL文字列
	 * @param dataStoreManager データストアマネージャ
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	public Page(String url, DataStoreManager dataStoreManager) throws PageIllegalArgumentException, PageAccessException {
		super(url);
		this.dataStoreManager = dataStoreManager;
	}
	
	/**
	 * このページ情報をデータストアに保存する。<p/>
	 * 保存処理は以下の順序で実施されます。<p/>
	 * １．このページが既にデータストアに保存されているか判定します。既に保存されていた場合はfalseを返却して処理を終了します。<br/>
	 * ２．このページの情報を「URLS」、「PAGES」、「DOCUMENTS」テーブルに保存し、trueを返却して処理を終了します。
	 * 
	 * @return 保存結果（true=保存された、false=すでにデータが存在するため、保存されなかった）
	 * @throws CrawlerSaveException データストアの登録時に必須項目が設定されていなかった場合
	 */
	public boolean save() throws CrawlerSaveException {
		if (this.isLatest()) return false;
		this.addUrlRecord();
		this.addPageRecord();
		this.addDocumentRecord();
		return true;
	}
	
	/**
	 * URLテーブルに対してレコードを追加する。<p/>
	 * このページのURL情報をURLテーブルに対してレコードを追加する。<br/>
	 * すでにレコードが存在した場合、保存は行わない。その場合、falseを返却します。
	 * 
	 * @return true=保存をおこなった場合、false=すでにレコードが存在した場合
	 * @throws CrawlerSaveException データストアへの保存に失敗した場合
	 */
	public boolean addUrlRecord() throws CrawlerSaveException{
		try {
			Urls               urls       = (Urls)dataStoreManager.getDataAccessObject(CrawlerDaoConstants.URLS);
			String             url        = this.getURL();
			String             protocol   = this.getProtocol();
			String             host       = this.getHost();
			List<String>       pathList   = this.getPathList();
			Map<String,String> parameter  = this.getParameter();
			String             fileid     = this.getFileId();
			Date               createDate = this.getCreateDate();
			Date               updateDate = this.getUpdateDate();
			if (urls.select(protocol, host, pathList, parameter) != null) return false;
			urls.insert(protocol, host, pathList, parameter, url, fileid, createDate, updateDate);
			return true;
		} catch (DataStoreManagerException | CrawlerException | DocumentFatalException | PageAccessException e) {
			throw new CrawlerSaveException(FAILE_TO_GET_PAGE, this.getURL(), e);
		}
	}
	
	/**
	 * PAGEテーブルに対してレコードを追加する。<p/>
	 * このページのページ情報をPAGEテーブルに対してレコードを追加する。<br/>
	 * すでにレコードが存在した場合、保存は行わない。その場合、falseを返却します。
	 * 
	 * @return true=保存をおこなった場合、false=すでにレコードが存在した場合
	 * @throws CrawlerSaveException データストアへの保存に失敗した場合
	 */
	public boolean addPageRecord() throws CrawlerSaveException{
		try {
			Pages     pages     = (Pages)dataStoreManager.getDataAccessObject(CrawlerDaoConstants.PAGES);
			String                   protocol       = this.getProtocol();
			String                   host           = this.getHost();
			List<String>             pathList       = this.getPathList();
			Map<String,String>       parameter      = this.getParameter();
			String                   fileid         = this.getFileId();
			long                     timeid         = this.getTimeId();
			Date                     createDate     = this.getCreateDate();
			Date                     updateDate     = this.getUpdateDate();
			Map<String,String>       requestHeader  = this.getRequestHeader().getHeaderMap();
			Map<String,List<String>> responseHeader = this.getResponseHeader().getHeaderMap();
			String                   httpStatusCode = this.getResponseHeader().getResponseRecord().getHttpStatusCode().getCode();
			String                   httpVersion    = this.getResponseHeader().getResponseRecord().getHttpVersion();
			if (pages.select(protocol, host, pathList, parameter, fileid, timeid) != null) return false;
			pages.insert(protocol, host, pathList, parameter, requestHeader, responseHeader, httpStatusCode, httpVersion, fileid, timeid, createDate, updateDate);
			return true;
		} catch (DataStoreManagerException | CrawlerException | DocumentFatalException | PageAccessException  e) {
			throw new CrawlerSaveException(FAILE_TO_GET_PAGE, this.getURL(), e);
		}
	}
	
	/**
	 * DOCUMENTテーブルに対してレコードを追加する。<p/>
	 * このページのドキュメントをDOCUMENTテーブルに対してレコードを追加する。<br/>
	 * すでにレコードが存在した場合、保存は行わない。その場合、falseを返却します。
	 * 
	 * @return true=保存をおこなった場合、false=すでにレコードが存在した場合
	 * @throws CrawlerSaveException データストアへの保存に失敗した場合
	 */
	public boolean addDocumentRecord() throws CrawlerSaveException{
		try {
			Documents documents  = (Documents) dataStoreManager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
			String  filename     = this.getFileName();
			String  extension    = this.getExtension();
			byte[]  data         = this.getData().getBytes();
			String  fileid       = this.getFileId();
			long    timeid       = this.getTimeId();
			Date    createDate   = this.getCreateDate();
			Date    updateDate   = this.getUpdateDate();
			Date    lastModified = this.getResponseHeader().getLastModified();
			if (documents.select(fileid, timeid) != null) return false;
			documents.insert(fileid, timeid, filename, extension, lastModified, data, createDate, updateDate);
			return true;
		} catch (DataStoreManagerException | BrowzingException e) {
			throw new CrawlerSaveException(FAILE_TO_GET_PAGE, this.getURL(), e);
		}
	}
	
	/**
	 * このページを保存している履歴の個数を取得します。
	 * 
	 * @return 履歴を保持している個数
	 * @throws CrawlerSaveException データストアへ対する操作にて例外が発生した場合
	 */
	public int getCount() throws CrawlerSaveException {
		try {
			Pages pages = (Pages)this.dataStoreManager.getDataAccessObject(CrawlerDaoConstants.PAGES);
			String              protcol   = this.getProtocol();
			String              host      = this.getHost();
			List<String>        pathList  = this.getPathList();
			Map<String, String> parameter = this.getParameter();
			int count = pages.count(protcol, host, pathList, parameter);
			return count;
		} catch (DataStoreManagerException e) {
			throw new CrawlerSaveException(FAILE_TO_GET_PAGE, this.getURL(), e);
		}
	}
	
	/**
	 * このページが既にデータストアに保存されているか判定します。<p/>
	 * 既に保存されている場合、trueを返却し、保存されているない場合、falseを返却します。<br/>
	 * 
	 * 確認対象は、PAGESテーブルにすでにレコードがあるかどうかで判定を行います。
	 * 
	 * @return 判定結果（true=保存済みである、false=未だに保存されていないページである）
	 * @throws CrawlerException データストアへ対する操作にて例外が発生した場合
	 */
	public boolean isSaved() throws CrawlerSaveException {
		if(getCount() != 0) return true;
		return false;
	}
	
	/**
	 * このページをデータストアに保存されているものと比較して最新状態であるかどうかを判定します。<p/>
	 * 最新である場合はtrue、最新でない、もしくはページが保存すらされていない場合は、falseを返却します。<br/>
	 * <br/>
	 * 判定は以下の手順に沿って行われます。<br/>
	 * １．ページが保存すらされていない場合は、falseを返却<br/>
	 * ２．このページと保存されてるページで更新日付が異なる場合はfalse。<br/>
	 * ３．このページと保存されているページで更新日付が同じ、かつ、保存されているデータが完全に一致売る場合は、true<br/>
	 * 　　（更新日付がともにnullも一致と判定、データの差異比較の場合も、同様）
	 * 
	 * @return 判定結果（true=最新である、false=最新でない、またはページが保存されていない）
	 * @throws CrawlerSaveException  データストアへ対する操作にて例外が発生した場合
	 */
	public boolean isLatest() throws CrawlerSaveException {
		if (!isSaved()) return false;
		try {
			Documents documents = (Documents)this.dataStoreManager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
			DocumentsRecord documentsRecord = documents.selectLastest(this.getFileId());
			
			if (documentsRecord == null) return false;
			Date thisLastModified  = this.getResponseHeader().getLastModified();
			Date savedLastModified = documentsRecord.getLastUpdateDate();
			byte[] thisPageData = this.getData().getBytes();
			byte[] savedData    = documentsRecord.getData();
			if (this.sameDate(thisLastModified, savedLastModified) && this.sameBytes(thisPageData, savedData)) return true;
			return false;
		} catch (DataStoreManagerException | BrowzingException e) {
			throw new CrawlerSaveException(FAILE_TO_GET_PAGE, this.getURL(), e);
		}
	}
	
	/**
	 * 引数に設定された日付が同じものかどうか判定します。
	 * 
	 * @param date1 日付１
	 * @param date2 日付２
	 * @return 判定結果（true=一致、false=不一致）
	 */
	protected boolean sameDate(Date date1, Date date2) {
		if (date1 == null && date2 == null) return true; 
		if (date1 == null && date2 != null) return false;
		if (date1 != null && date2 == null) return false;
		if (date1.equals(date2)) return true;
		return false;
	}
	
	/**
	 * 引数に設定されたバイト配列が同じものかどうか判定します。
	 * 
	 * @param bytes1 バイト配列１
	 * @param bytes2 バイト配列２
	 * @return 判定結果（true=一致、false=不一致）
	 */
	protected boolean sameBytes(byte[] bytes1, byte[] bytes2) {
		if (bytes1 == null && bytes2 == null) return true; 
		if (bytes1 == null && bytes2 != null) return false;
		if (bytes1 != null && bytes2 == null) return false;
		if (bytes1.length == bytes2.length) {
			for (int i=0; i<bytes1.length; i++) {
				if (bytes1[i] != bytes2[i]) return false; 
			}
			return true;
		} else {
			return false;
		}
	}
}


