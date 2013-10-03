package jp.co.dk.crawler.dao.record;

import java.util.Date;

import jp.co.dk.datastoremanager.DataConvertable;
import jp.co.dk.datastoremanager.Record;
import jp.co.dk.datastoremanager.database.DataBaseRecord;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/**
 * UrlsRecordは、URLSテーブルの単一のレコードを表すクラス。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class UrlsRecord implements DataConvertable{
	
	/** プロトコル */
	protected String protocol;
	
	/** ホスト名 */
	protected String host;
	
	/** パス-ハッシュ値 */
	protected int h_path;
	
	/** パラメータ-ハッシュ値 */
	protected int h_parameter;
	
	/** URL文字列 */
	protected String url;
	
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
	 * URL文字列を取得する。
	 * @return URL文字列
	 */
	public String getUrl() {
		return url;
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
	
	@Override
	public DataConvertable convert(DataBaseRecord record)	throws DataStoreManagerException {
		UrlsRecord urlsRecord     = new UrlsRecord();
		urlsRecord.protocol       = record.getString("PROTOCOL");
		urlsRecord.host           = record.getString("HOSTNAME");
		urlsRecord.h_path         = record.getInt("H_PATH");
		urlsRecord.h_parameter    = record.getInt("H_PARAM");
		urlsRecord.url            = record.getString("URL");
		urlsRecord.createDate     = record.getTimestamp("CREATE_DATE");
		urlsRecord.updateDate     = record.getTimestamp("UPDATE_DATE");
		return urlsRecord;
	}

	@Override
	public DataConvertable convert(Record record)	throws DataStoreManagerException {
		UrlsRecord urlsRecord     = new UrlsRecord();
		urlsRecord.protocol       = record.getString(1);
		urlsRecord.host           = record.getString(2);
		urlsRecord.h_path         = record.getInt(3);
		urlsRecord.h_parameter    = record.getInt(4);
		urlsRecord.url            = record.getString(5);
		urlsRecord.createDate     = record.getDate(6);
		urlsRecord.updateDate     = record.getDate(7);
		return urlsRecord;
	}
	
}
