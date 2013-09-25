package jp.co.dk.crawler.dao;

import jp.co.dk.crawler.dao.record.PagesRecord;
import jp.co.dk.datastoremanager.DataAccessObject;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/**
 * Pagesは、PAGESテーブルに対しての制御を行うDAOインスタンスが実装するインターフェース。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public interface Pages extends DataAccessObject{
	
	/**
	 * PAGESテーブルを作成する。<p/>
	 * テーブル作成に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void createTable() throws DataStoreManagerException;
	
	/**
	 * PAGESテーブルから特定の１レコードを取得する。
	 * 
	 * @param protocol プロトコル名
	 * @param hostname ホスト名
	 * @param h_path   パスのハッシュ値
	 * @param param    パラメータのハッシュ値
	 * @return 取得したレコードオブジェクト
	 */
	public PagesRecord select(String protocol, String hostname, int h_path, int param)throws DataStoreManagerException;
	
	/**
	 * PAGESテーブルから指定の１レコードを登録する。
	 * 
	 * @param record レコードオブジェクト
	 * @return 登録したレコードオブジェクト
	 */
	public void insert(PagesRecord record)throws DataStoreManagerException;
	
	/**
	 * PAGESテーブルを削除する。<p/>
	 * テーブル削除に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void dropTable() throws DataStoreManagerException;
}
