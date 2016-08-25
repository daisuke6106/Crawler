package jp.co.dk.crawler.db.rdb.dao.record;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.rdb.DataBaseRecord;
import jp.co.dk.datastoremanager.rdb.DataConvertable;
import jp.co.dk.datastoremanager.rdb.Record;

/**
 * PagesRecordは、PAGESテーブルの単一のレコードを表すクラス。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class PagesRecord implements DataConvertable{

	/** プロトコル */
	protected String protocol;
	
	/** ホスト名 */
	protected String host;
	
	/** パス-ハッシュ値 */
	protected int h_path;
	
	/** パラメータ-ハッシュ値 */
	protected int h_parameter;
	
	/** ファイルID */
	protected String fileid;
	
	/** タイムID */
	protected long timeid;
	
	/** パス */
	protected List<String> path;
	
	/** パスカウント */
	protected int pathCount;
	
	/** パラメータ */
	protected Map<String,String> parameter;
	
	/** パラメータカウント */
	protected int parameterCount;
	
	/** リクエストヘッダー */
	protected Map<String, String> requestHeader;
	
	/** レスポンスヘッダー */
	protected Map<String, List<String>> responceHeader;
	
	/** HTTPステータスコード */
	protected String httpStatusCode;
	
	/** HTTPバージョン */
	protected String httpVersion;
	
	/** 作成日時 */
	protected Date createDate;
	
	/** 更新日時 */
	protected Date updateDate;
	
	/**
	 * プロトコル名を取得する。
	 * @return プロトコル名
	 */
	public String getProtocol() {
		return protocol;
	}
	
	/**
	 * ホスト名を取得する。
	 * @return ホスト名
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * パスリストのハッシュ値を取得する。
	 * @return パスリストのハッシュ値
	 */
	public int getH_path() {
		return h_path;
	}
	
	/**
	 * パラメータのハッシュ値を取得する。
	 * @return パラメータのハッシュ値
	 */
	public int getH_parameter() {
		return h_parameter;
	}
	
	/**
	 * ファイルIDを取得する。
	 * @return ファイルID
	 */
	public String getFileid() {
		return fileid;
	}
	
	/**
	 * タイムIDを取得する。
	 * @return タイムID
	 */
	public long getTimeid() {
		return timeid;
	}
	
	/**
	 * パスリストを取得する。
	 * @return パスリスト
	 * @throws CrawlerException バイト配列からパスリストへ変換に失敗した場合
	 */
	public List<String> getPath() throws CrawlerException {
		return this.path;
	}
	
	/**
	 * パスカウントを取得する。
	 * @return パスカウント
	 */
	public int getPathCount() {
		return pathCount;
	}
	
	/**
	 * パラメータマップを取得する。
	 * @return パラメータマップ
	 * @throws CrawlerException バイト配列からパラメータマップへ変換に失敗した場合
	 */
	public Map<String, String> getParameter() throws CrawlerException {
		return this.parameter;
	}
	
	/**
	 * パスカウントを取得する。
	 * @return パスカウント
	 */
	public int getParameterCount() {
		return parameterCount;
	}
	
	/**
	 * リクエストヘッダーマップを取得する。
	 * @return リクエストヘッダーマップ
	 * @throws CrawlerException バイト配列からリクエストヘッダーマップへ変換に失敗した場合
	 */
	public Map<String, String> getRequestHeader() throws CrawlerException {
		return this.requestHeader;
	}
	
	/**
	 * レスポンスヘッダーマップを取得する。
	 * @return レスポンスヘッダーマップ
	 * @throws CrawlerException バイト配列からレスポンスヘッダーマップへ変換に失敗した場合
	 */
	public Map<String, List<String>> getResponceHeader() throws CrawlerException {
		return this.responceHeader;
	}
	
	/**
	 * ファイルIDを取得する。
	 * @return ファイルID
	 */
	public String getFileId() {
		return fileid;
	}
	
	/**
	 * HTTPステータスコードを取得する。
	 * @return HTTPステータスコード
	 */
	public String getHttpStatusCode() {
		return httpStatusCode;
	}
	
	/**
	 * HTTPバージョンを取得する。
	 * @return HTTPバージョン
	 */
	public String getHttpVersion() {
		return httpVersion;
	}
	
	/**
	 * タイムIDを取得する。
	 * @return タイムID
	 */
	public long getTimeId() {
		return timeid;
	}
	
	/**
	 * 登録日時を取得する。
	 * @return 登録日時
	 */
	public Date getCreateDate() {
		return createDate;
	}
	
	/**
	 * 更新日時を取得する。
	 * @return 更新日時
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DataConvertable convert(DataBaseRecord record)	throws DataStoreManagerException {
		PagesRecord pagesRecord    = new PagesRecord();
		pagesRecord.protocol       = record.getString("PROTOCOL");
		pagesRecord.host           = record.getString("HOSTNAME");
		pagesRecord.h_path         = record.getInt("H_PATH");
		pagesRecord.h_parameter    = record.getInt("H_PARAM");
		pagesRecord.fileid         = record.getString("FILEID");
		pagesRecord.timeid         = record.getLong("TIMEID");
		pagesRecord.path           = (List<String>) record.getObject("PATH");
		pagesRecord.pathCount      = record.getInt("PATH_COUNT");
		pagesRecord.parameter      = (Map<String,String>) record.getObject("PARAMETER");
		pagesRecord.parameterCount = record.getInt("PARAMETER_COUNT");
		pagesRecord.requestHeader  = (Map<String,String>) record.getObject("REQUEST_HEADER");
		pagesRecord.responceHeader = (Map<String,List<String>>) record.getObject("RESPONCE_HEADER");
		pagesRecord.httpStatusCode = record.getString("HTTP_STATUS_CODE");
		pagesRecord.httpVersion    = record.getString("HTTP_VERSION");
		pagesRecord.createDate     = record.getTimestamp("CREATE_DATE");
		pagesRecord.updateDate     = record.getTimestamp("UPDATE_DATE");
		return pagesRecord;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public DataConvertable convert(Record record)	throws DataStoreManagerException {
		PagesRecord pagesRecord    = new PagesRecord();
		pagesRecord.protocol       = record.getString(1);
		pagesRecord.host           = record.getString(2);
		pagesRecord.h_path         = record.getInt(3);
		pagesRecord.h_parameter    = record.getInt(4);
		pagesRecord.path           = (List<String>) record.getObject(5);
		pagesRecord.pathCount      = record.getInt(6);
		pagesRecord.parameter      = (Map<String,String>) record.getObject(7);
		pagesRecord.parameterCount = record.getInt(8);
		pagesRecord.requestHeader  = (Map<String,String>) record.getObject(9);
		pagesRecord.responceHeader = (Map<String,List<String>>) record.getObject(10);
		pagesRecord.httpStatusCode = record.getString(11);
		pagesRecord.httpVersion    = record.getString(12);
		pagesRecord.fileid         = record.getString(13);
		pagesRecord.timeid         = record.getLong(14);
		pagesRecord.createDate     = record.getDate(15);
		pagesRecord.updateDate     = record.getDate(16);
		return pagesRecord;
	}
}
