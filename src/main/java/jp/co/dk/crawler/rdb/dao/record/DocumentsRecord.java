package jp.co.dk.crawler.rdb.dao.record;

import java.util.Date;

import jp.co.dk.datastoremanager.DataConvertable;
import jp.co.dk.datastoremanager.Record;
import jp.co.dk.datastoremanager.database.DataBaseRecord;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/**
 * DocumentsRecordは、DOCUMENTSテーブルの単一のレコードを表すクラス。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class DocumentsRecord implements DataConvertable{
	
	/** ファイルID */
	protected String fileId;
	
	/** タイムID */
	protected long timeId;
	
	/** ファイル名 */
	protected String filename;
	
	/** 拡張子 */
	protected String extention;
	
	/** 最終更新日付 */
	protected Date lastUpdateDate;
	
	/** コンテンツ */
	protected byte[] data;
	
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
	 * ファイル名を取得する。
	 * @return ファイル名
	 */
	public String getFilename() {
		return filename;
	}
	
	/**
	 * 拡張子を取得する。
	 * @return 拡張子
	 */
	public String getExtention() {
		return extention;
	}
	
	/**
	 * このファイルの最終更新日時を取得します。
	 * @return 最終更新日時
	 */
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	
	/**
	 * このファイルのデータを取得します。
	 * @return このファイルのデータ
	 */
	public byte[] getData() {
		return data;
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
		DocumentsRecord pagesRecord    = new DocumentsRecord();
		pagesRecord.fileId         = record.getString("FILEID");
		pagesRecord.timeId         = record.getLong("TIMEID");
		pagesRecord.filename       = record.getString("FILENAME");
		pagesRecord.extention      = record.getString("EXTENTION");
		pagesRecord.lastUpdateDate = record.getTimestamp("LASTUPDATE_DATE");
		pagesRecord.data           = record.getBytes("DATA");
		pagesRecord.createDate     = record.getTimestamp("CREATE_DATE");
		pagesRecord.updateDate     = record.getTimestamp("UPDATE_DATE");
		return pagesRecord;
	}

	@Override
	public DataConvertable convert(Record record)	throws DataStoreManagerException {
		DocumentsRecord pagesRecord    = new DocumentsRecord();
		pagesRecord.fileId     = record.getString(1);
		pagesRecord.timeId     = record.getLong(2);
		pagesRecord.filename   = record.getString(3);
		pagesRecord.extention  = record.getString(4);
		pagesRecord.lastUpdateDate = record.getTimestamp(5);
		pagesRecord.data       = record.getBytes(6);
		pagesRecord.createDate = record.getTimestamp(7);
		pagesRecord.updateDate = record.getTimestamp(8);
		return pagesRecord;
	}
}
