package jp.co.dk.crawler.dao.record;

import java.util.Date;

import jp.co.dk.datastoremanager.DataConvertable;
import jp.co.dk.datastoremanager.Record;
import jp.co.dk.datastoremanager.database.DataBaseRecord;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/**
 * CrawlerErrorsRecordは、REDIRECT_ERRORテーブルの単一のレコードを表すクラス。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class CrawlerErrorsRecord implements DataConvertable{
	
	/** プロトコル */
	protected String protocol;
	
	/** ホスト名 */
	protected String host;
	
	/** パス-ハッシュ値 */
	protected int h_path;
	
	/** パラメータ-ハッシュ値 */
	protected int h_parameter;
	
	/** メッセージ本文 */
	protected String message;
	
	/** 例外データ本体 */
	protected StackTraceElement[] stackTraceElements;
	
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
	 * 例外のメッセージ本文を取得する。
	 * @return 例外のメッセージ本文
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * この例外データ本体を取得します。
	 * @return 例外データ本体
	 */
	public StackTraceElement[] getStackTraceElements() {
		return stackTraceElements;
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
		CrawlerErrorsRecord pagesRecord       = new CrawlerErrorsRecord();
		pagesRecord.protocol           = record.getString("PROTOCOL");
		pagesRecord.host               = record.getString("HOSTNAME");
		pagesRecord.h_path             = record.getInt("H_PATH");
		pagesRecord.h_parameter        = record.getInt("H_PARAM");
		pagesRecord.message            = record.getString("MESSAGE");
		pagesRecord.stackTraceElements = (StackTraceElement[])record.getObject("STACKTRACEELEMENTS");
		pagesRecord.createDate         = record.getTimestamp("CREATE_DATE");
		pagesRecord.updateDate         = record.getTimestamp("UPDATE_DATE");
		return pagesRecord;
	}

	@Override
	public DataConvertable convert(Record record)	throws DataStoreManagerException {
		CrawlerErrorsRecord pagesRecord       = new CrawlerErrorsRecord();
		pagesRecord.protocol           = record.getString(1);
		pagesRecord.host               = record.getString(2);
		pagesRecord.h_path             = record.getInt(3);
		pagesRecord.h_parameter        = record.getInt(4);
		pagesRecord.message            = record.getString(5);
		pagesRecord.stackTraceElements = (StackTraceElement[])record.getObject(6);
		pagesRecord.createDate         = record.getTimestamp(7);
		pagesRecord.updateDate         = record.getTimestamp(8);
		return pagesRecord;
	}
}
