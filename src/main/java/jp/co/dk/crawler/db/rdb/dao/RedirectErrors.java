package jp.co.dk.crawler.db.rdb.dao;

import java.util.Date;

import jp.co.dk.crawler.db.rdb.dao.record.RedirectErrorsRecord;
import jp.co.dk.datastoremanager.DataAccessObject;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/**
 * RedirectErrorsは、REDIRECT_ERRORテーブルに対しての制御を行うDAOインスタンスが実装するインターフェース。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public interface RedirectErrors extends DataAccessObject {
	
	/**
	 * REDIRECT_ERRORテーブルを作成する。<p/>
	 * テーブル作成に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void createTable() throws DataStoreManagerException;
	
	/**
	 * REDIRECT_ERRORテーブルから特定の１レコードを取得する。
	 * 
	 * @param fileId ファイルID
	 * @param timeId タイムID
	 * @throws DataStoreManagerException 取得に失敗した場合
	 */
	public RedirectErrorsRecord select(String fileId, long timeId) throws DataStoreManagerException;
	
	/**
	 * REDIRECT_ERRORテーブルから特定の１レコードを取得する。
	 * 
	 * @param fileId ファイルID
	 * @param timeId タイムID
	 * @return 件数
	 * @throws DataStoreManagerException 取得に失敗した場合
	 */
	public int count(String fileId, long timeId) throws DataStoreManagerException;
	
	/**
	 * REDIRECT_ERRORテーブルから指定の１レコードを登録する。<p/>
	 * 
	 * @param fileId             ファイルID
	 * @param timeId             タイムID
	 * @param message            例外のメッセージ本文
	 * @param stackTraceElements 例外データ本体
	 * @param createDate         登録日付
	 * @param updateDate         更新日付
	 * @throws DataStoreManagerException 登録に失敗した場合
	 */
	public void insert(String fileId, long timeId, String message, StackTraceElement[] stackTraceElements, Date createDate, Date updateDate) throws DataStoreManagerException;
	
	/**
	 * REDIRECT_ERRORテーブルを削除する。<p/>
	 * テーブル削除に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void dropTable() throws DataStoreManagerException;
}
