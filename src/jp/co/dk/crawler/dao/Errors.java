package jp.co.dk.crawler.dao;

import java.util.Date;

import jp.co.dk.crawler.dao.record.ErrorsRecord;
import jp.co.dk.datastoremanager.DataAccessObject;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/**
 * Errorsは、ERRORSテーブルに対しての制御を行うDAOインスタンスが実装するインターフェース。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public interface Errors extends DataAccessObject {
	
	/**
	 * ERRORSテーブルを作成する。<p/>
	 * テーブル作成に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void createTable() throws DataStoreManagerException;
	
	/**
	 * ERRORSテーブルから特定の１レコードを取得する。
	 * 
	 * @param fileId ファイルID
	 * @param timeId タイムID
	 * @throws DataStoreManagerException 取得に失敗した場合
	 */
	public ErrorsRecord select(long fileId, long timeId) throws DataStoreManagerException;
	
	/**
	 * ERRORSテーブルから指定の１レコードを登録する。<p/>
	 * 
	 * @param fileId             ファイルID
	 * @param timeId             タイムID
	 * @param message            例外のメッセージ本文
	 * @param stackTraceElements 例外データ本体
	 * @param createDate         登録日付
	 * @param updateDate         更新日付
	 * @throws DataStoreManagerException 登録に失敗した場合
	 */
	public void insert(long fileId, long timeId, String message, StackTraceElement[] stackTraceElements, Date createDate, Date updateDate) throws DataStoreManagerException;
	
	/**
	 * ERRORSテーブルを削除する。<p/>
	 * テーブル削除に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void dropTable() throws DataStoreManagerException;
}
