package jp.co.dk.crawler.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.dao.record.DocumentsRecord;
import jp.co.dk.crawler.exception.CrawlerException;
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
	 * @param protcol    プロトコル文字列（必須）
	 * @param host       ホスト名（必須）
	 * @param path       パスリスト（設定されていない場合、空のリストで置き換え）
	 * @param parameter  パラメータマップ（設定されていない場合、空のマップで置き換え）
	 * @throws DataStoreManagerException 登録に失敗した場合
	 */
	public DocumentsRecord select(long fileId) throws DataStoreManagerException;
	
	/**
	 * DOCUMENTSテーブルから指定の１レコードを登録する。<p/>
	 * 
	 * @param fileId     ファイルID
	 * @param filename   ファイル名
	 * @param extention  拡張子
	 * @param data       データ本体
	 * @param createDate 登録日付
	 * @param updateDate 更新日付
	 * @throws DataStoreManagerException 登録に失敗した場合
	 */
	public void insert(long fileId, String filename, String extention, byte[] data, Date createDate, Date updateDate) throws DataStoreManagerException;
	
	/**
	 * DOCUMENTSテーブルを削除する。<p/>
	 * テーブル削除に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void dropTable() throws DataStoreManagerException;
}
