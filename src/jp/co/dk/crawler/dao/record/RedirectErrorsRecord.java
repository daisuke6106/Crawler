package jp.co.dk.crawler.dao.record;

import java.util.Date;

import jp.co.dk.datastoremanager.DataConvertable;
import jp.co.dk.datastoremanager.Record;
import jp.co.dk.datastoremanager.database.DataBaseRecord;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/**
 * RedirectErrorsRecordは、REDIRECT_ERRORSテーブルの単一のレコードを表すクラス。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class RedirectErrorsRecord implements DataConvertable{
	
	/** ファイルID */
	protected String fileId;
	
	/** タイムID */
	protected long timeId;
	
	/** メッセージ本文 */
	protected String message;
	
	/** 例外データ本体 */
	protected StackTraceElement[] stackTraceElements;
	
	/** 作成日時 */
	protected Date createDate;
	
	/** 更新日時 */
	protected Date updateDate;
	
	/**
	 * ファイルIDを取得する。
	 * @return ファイルID
	 */
	public String getFileId() {
		return fileId;
	}
	
	/**
	 * タイムIDを取得する。
	 * @return タイムID
	 */
	public long getTimeId() {
		return timeId;
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
		RedirectErrorsRecord pagesRecord       = new RedirectErrorsRecord();
		pagesRecord.fileId             = record.getString("FILEID");
		pagesRecord.timeId             = record.getLong("TIMEID");
		pagesRecord.message            = record.getString("MESSAGE");
		pagesRecord.stackTraceElements = (StackTraceElement[])record.getObject("STACKTRACEELEMENTS");
		pagesRecord.createDate         = record.getTimestamp("CREATE_DATE");
		pagesRecord.updateDate         = record.getTimestamp("UPDATE_DATE");
		return pagesRecord;
	}

	@Override
	public DataConvertable convert(Record record)	throws DataStoreManagerException {
		RedirectErrorsRecord pagesRecord       = new RedirectErrorsRecord();
		pagesRecord.fileId             = record.getString(1);
		pagesRecord.timeId             = record.getLong(2);
		pagesRecord.message            = record.getString(3);
		pagesRecord.stackTraceElements = (StackTraceElement[])record.getObject(4);
		pagesRecord.createDate         = record.getTimestamp(5);
		pagesRecord.updateDate         = record.getTimestamp(6);
		return pagesRecord;
	}
}
