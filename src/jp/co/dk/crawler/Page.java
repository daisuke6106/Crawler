package jp.co.dk.crawler;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.Documents;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.Urls;
import jp.co.dk.crawler.dao.record.DocumentsRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.ByteDump;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

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
	
	/**
	 * このページ情報をデータストアに保存する。<p/>
	 * 保存処理は以下の順序で実施されます。<p/>
	 * １．このページが既にデータストアに保存されているか判定します。既に保存されていた場合はfalseを返却して処理を終了します。<br/>
	 * ２．このページの情報を「URLS」、「PAGES」、「DOCUMENTS」テーブルに保存し、trueを返却して処理を終了します。
	 * 
	 * @return 保存結果（true=保存された、false=すでにデータが存在するため、保存されなかった）
	 * @throws CrawlerException データストアの登録時に必須項目が設定されていなかった場合
	 * @throws BrowzingException 更新日付に不正な値が設定されていた場合
	 */
	public boolean save() throws CrawlerException  {
		if (this.isLatest()) return false;
		try {
			this.dataStoreManager.startTrunsaction();
			Urls      urls      = (Urls)      dataStoreManager.getDataAccessObject(CrawlerDaoConstants.URLS);
			Pages     pages     = (Pages)     dataStoreManager.getDataAccessObject(CrawlerDaoConstants.PAGES);
			Documents documents = (Documents) dataStoreManager.getDataAccessObject(CrawlerDaoConstants.DOCUMENTS);
			String                   url            = this.getURL();
			String                   protocol       = this.getProtocol();
			String                   host           = this.getHost();
			List<String>             pathList       = this.getPathList();
			String                   filename       = this.getFileName();
			String                   extension      = this.getExtension();
			Map<String,String>       parameter      = this.getParameter();
			byte[]                   data           = this.getData().getBytes();
			long                     fileid         = this.getFileId();
			long                     timeid         = this.getTimeId();
			Date                     createDate     = this.getCreateDate();
			Date                     updateDate     = this.getUpdateDate();
			Map<String,String>       requestHeader  = this.getRequestHeader().getHeaderMap();
			Map<String,List<String>> responseHeader = this.getResponseHeader().getHeaderMap();
			Date lastModified = this.getResponseHeader().getLastModified();
			if (urls.select(protocol, host, pathList, filename, parameter) == null) {
				urls.insert(protocol, host, pathList, filename, parameter, url, fileid, createDate, updateDate);
			}
			pages.insert(protocol, host, pathList, filename, parameter, requestHeader, responseHeader, fileid, timeid, createDate, updateDate);
			documents.insert(fileid, timeid, filename, extension, lastModified, data, createDate, updateDate);
		} catch (DataStoreManagerException | BrowzingException e) {
			throw new CrawlerException(FAILE_TO_GET_PAGE, this.getURL(), e);
		} finally {
			try {
				this.dataStoreManager.finishTrunsaction();
			} catch (DataStoreManagerException e) {
				throw new CrawlerException(FAILE_TO_GET_PAGE, this.getURL(), e);
			}
		}
		return true;
	}
	
	/**
	 * このページを保存している履歴の個数を取得します。
	 * 
	 * @return 履歴を保持している個数
	 * @throws CrawlerException データストアへ対する操作にて例外が発生した場合
	 */
	public int getCount() throws CrawlerException {
		try {
			this.dataStoreManager.startTrunsaction();
			Pages pages = (Pages)this.dataStoreManager.getDataAccessObject(CrawlerDaoConstants.PAGES);
			String              protcol   = this.getProtocol();
			String              host      = this.getHost();
			List<String>        pathList  = this.getPathList();
			String              filename  = this.getFileName();
			Map<String, String> parameter = this.getParameter();
			int count = pages.count(protcol, host, pathList, filename, parameter);
			return count;
		} catch (DataStoreManagerException e) {
			throw new CrawlerException(FAILE_TO_GET_PAGE, this.getURL(), e);
		} finally {
			try {
				this.dataStoreManager.finishTrunsaction();
			} catch (DataStoreManagerException e) {
				throw new CrawlerException(FAILE_TO_GET_PAGE, this.getURL(), e);
			}
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
	public boolean isSaved() throws CrawlerException {
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
	 * @throws CrawlerException  データストアへ対する操作にて例外が発生した場合
	 */
	public boolean isLatest() throws CrawlerException {
		if (!isSaved()) return false;
		try {
			this.dataStoreManager.startTrunsaction();
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
			throw new CrawlerException(FAILE_TO_GET_PAGE, this.getURL(), e);
		} finally {
			try {
				this.dataStoreManager.finishTrunsaction();
			} catch (DataStoreManagerException e) {
				throw new CrawlerException(FAILE_TO_GET_PAGE, this.getURL(), e);
			}
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
	
	@Override
	public Map<String, String> getParameter() {
		return new ParameterMap(super.parameter);
	}
	
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
		String protocol              = this.getProtocol();
		String host                  = this.getHost();
		List<String> pathList        = this.getPathList();
		String filename              = this.getFileName();
		Map<String,String> parameter = this.getParameter();
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

class ParameterMap extends HashMap<String, String> {
	
	/** シリアルバージョンID */
	private static final long serialVersionUID = 6071724790375396636L;
	
	ParameterMap() {
		super();
	}
	
	ParameterMap(Map<String, String> parameter) {
		super(parameter);
	}
	
	@Override
	public int hashCode() {
		int originalHashCode = super.hashCode();
		if (originalHashCode == 0) return 1;
		return originalHashCode;
	}
}