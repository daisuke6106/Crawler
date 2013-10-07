package jp.co.dk.crawler.dao.record;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataConvertable;
import jp.co.dk.datastoremanager.Record;
import jp.co.dk.datastoremanager.database.DataBaseRecord;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

/**
 * PagesRecordは、PAGESテーブルの単一のレコードを表すクラス。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class DocumentsRecord implements DataConvertable{
	
	/** ファイルID */
	protected long fileId;
	
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
	public long getFileId() {
		return fileId;
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
		pagesRecord.fileId         = record.getLong("FILEID");
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
		pagesRecord.fileId     = record.getLong(1);
		pagesRecord.filename   = record.getString(2);
		pagesRecord.extention  = record.getString(3);
		pagesRecord.lastUpdateDate = record.getTimestamp(4);
		pagesRecord.data       = record.getBytes(5);
		pagesRecord.createDate = record.getTimestamp(6);
		pagesRecord.updateDate = record.getTimestamp(7);
		return pagesRecord;
	}
}
