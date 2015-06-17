package jp.co.dk.crawler.rdb.dao;

import java.util.Date;

import jp.co.dk.crawler.rdb.dao.record.DocumentsRecord;
import jp.co.dk.datastoremanager.DataAccessObject;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/**
 * Documentsは、DOCUMENTSテーブルに対しての制御を行うDAOインスタンスが実装するインターフェース。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public interface Documents extends DataAccessObject {
	
	/**
	 * DOCUMENTSテーブルを作成する。<p/>
	 * テーブル作成に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void createTable() throws DataStoreManagerException;
	
	/**
	 * DOCUMENTSテーブルから特定の１レコードを取得する。
	 * 
	 * @param fileId ファイルID
	 * @param timeId タイムID
	 * @throws DataStoreManagerException 取得に失敗した場合
	 */
	public DocumentsRecord select(String fileId, long timeId) throws DataStoreManagerException;
	
	/**
	 * DOCUMENTSテーブルから特定のレコードの件数を取得する。
	 * 
	 * @param fileId ファイルID
	 * @param timeId タイムID
	 * @return 件数
	 * @throws DataStoreManagerException 取得に失敗した場合
	 */
	public int count(String fileId, long timeId) throws DataStoreManagerException;
	
	/**
	 * DOCUMENTSテーブルから指定のファイルIDにひもづくレコードで最も最新の１レコードを取得する。
	 * 
	 * @param fileId ファイルID
	 * @throws DataStoreManagerException 取得に失敗した場合
	 */
	public DocumentsRecord selectLastest(String fileId) throws DataStoreManagerException;
	
	/**
	 * DOCUMENTSテーブルから指定の１レコードを登録する。<p/>
	 * 
	 * @param fileId         ファイルID
	 * @param timeId         タイムID
	 * @param filename       ファイル名
	 * @param extention      拡張子
	 * @param lastUpdateDate 最終更新日時
	 * @param data           データ本体
	 * @param createDate     登録日付
	 * @param updateDate     更新日付
	 * @throws DataStoreManagerException 登録に失敗した場合
	 */
	public void insert(String fileId, long timeId, String filename, String extention, Date lastUpdateDate, byte[] data, Date createDate, Date updateDate) throws DataStoreManagerException;
	
	/**
	 * DOCUMENTSテーブルを削除する。<p/>
	 * テーブル削除に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void dropTable() throws DataStoreManagerException;
}
