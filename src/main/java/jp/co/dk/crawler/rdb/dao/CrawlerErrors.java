package jp.co.dk.crawler.rdb.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.rdb.dao.record.CrawlerErrorsRecord;
import jp.co.dk.datastoremanager.DataAccessObject;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/**
 * CrawlerErrorsは、CRAWLER_ERRORSテーブルに対しての制御を行うDAOインスタンスが実装するインターフェース。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public interface CrawlerErrors extends DataAccessObject {
	
	/**
	 * CRAWLER_ERRORSテーブルを作成する。<p/>
	 * テーブル作成に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void createTable() throws DataStoreManagerException;
	
	/**
	 * CRAWLER_ERRORSテーブルから特定の１レコードを取得する。
	 * 
	 * @param protcol   プロトコル名
	 * @param host      ホスト名
	 * @param path      パス
	 * @param parameter パラメータ
	 * @throws DataStoreManagerException 取得に失敗した場合
	 */
	public CrawlerErrorsRecord select(String protcol, String host, List<String> path, Map<String, String> parameter) throws DataStoreManagerException;
	
	/**
	 * CRAWLER_ERRORSテーブルから特定のレコードの件数を取得する。
	 * 
	 * @param protcol   プロトコル名
	 * @param host      ホスト名
	 * @param path      パス
	 * @param parameter パラメータ
	 * @return 件数
	 * @throws DataStoreManagerException 取得に失敗した場合
	 */
	public int count(String protcol, String host, List<String> path, Map<String, String> parameter) throws DataStoreManagerException;
	
	
	/**
	 * CRAWLER_ERRORSテーブルから指定の１レコードを登録する。<p/>
	 * 
	 * @param protcol   　　　　　プロトコル名
	 * @param host      　　　　　ホスト名
	 * @param path      　　　　　パス
	 * @param parameter 　　　　　パラメータ
	 * @param message            例外のメッセージ本文
	 * @param stackTraceElements 例外データ本体
	 * @param createDate         登録日付
	 * @param updateDate         更新日付
	 * @throws DataStoreManagerException 登録に失敗した場合
	 */
	public void insert(String protcol, String host, List<String> path, Map<String, String> parameter, String message, StackTraceElement[] stackTraceElements, Date createDate, Date updateDate) throws DataStoreManagerException;
	
	/**
	 * CRAWLER_ERRORSテーブルを削除する。<p/>
	 * テーブル削除に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void dropTable() throws DataStoreManagerException;
}
