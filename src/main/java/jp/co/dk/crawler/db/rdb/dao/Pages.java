package jp.co.dk.crawler.db.rdb.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.db.rdb.dao.record.PagesRecord;
import jp.co.dk.crawler.exception.CrawlerException;
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
	 * PAGESテーブルから指定の条件に合致するレコードを取得する。
	 * 
	 * @param protocol  プロトコル名
	 * @param hostname  ホスト名
	 * @param path      パスのハッシュ値
	 * @param parameter パラメータのハッシュ値
	 * @return 取得したレコードオブジェクトの一覧
	 */
	public List<PagesRecord> select(String protcol, String host, List<String> path, Map<String, String> parameter) throws DataStoreManagerException ;
	
	/**
	 * PAGESテーブルから指定の条件に合致するレコードの数を取得する。
	 * 
	 * @param protocol  プロトコル名
	 * @param hostname  ホスト名
	 * @param path      パスのハッシュ値
	 * @param parameter パラメータのハッシュ値
	 * @return カウント結果
	 */
	public int count(String protcol, String host, List<String> path, Map<String, String> parameter) throws DataStoreManagerException ;
	
	/**
	 * PAGESテーブルから指定の条件に合致するレコードを取得する。
	 * 
	 * @param protocol  プロトコル名
	 * @param hostname  ホスト名
	 * @param path      パスのハッシュ値
	 * @param parameter パラメータのハッシュ値
	 * @param fileId    ファイルID
	 * @param timeId    タイムID
	 * @return 取得したレコードオブジェクト
	 */
	public PagesRecord select(String protcol, String host, List<String> path, Map<String, String> parameter, String fileId, long timeId) throws DataStoreManagerException ;
	
	/**
	 * PAGESテーブルから指定の条件に合致するレコードを取得する。
	 * DOCUMENTSテーブルから指定のファイルIDにひもづくレコードで最も最新の１レコードを取得する。
	 * 
	 * @param protocol  プロトコル名
	 * @param hostname  ホスト名
	 * @param path      パスのハッシュ値
	 * @param parameter パラメータのハッシュ値
	 * @return 取得したレコードオブジェクト
	 */
	public PagesRecord selectLastest(String protcol, String host, List<String> path, Map<String, String> parameter) throws DataStoreManagerException ;
	
	/**
	 * PAGESテーブルから指定の１レコードを登録する。<p/>
	 * 必須パラメータが設定されていない場合、例外を送出します。<br/>
	 * <br/>
	 * pathと、parameterに限り設定されていない場合（nullの場合）、空のリスト、マップインスタンスで置き換えます。<br/>
	 * 
	 * @param protcol        プロトコル文字列（必須）
	 * @param host           ホスト名（必須）
	 * @param path           パスリスト（設定されていない場合、空のリストで置き換え）
	 * @param parameter      パラメータマップ（設定されていない場合、空のマップで置き換え）
	 * @param requestHeader  リクエストヘッダ
	 * @param responceHeader レスポンスヘッダ
	 * @param httpStatusCode HTTPステータスコード
	 * @param httpVersion    HTTPバージョン
	 * @param fileid         ファイルID
	 * @param timeid         タイムID
	 * @param createDate     登録日付
	 * @param updateDate     更新日付
	 * @throws DataStoreManagerException 登録に失敗した場合
	 * @throws CrawlerException          必須パラメータが設定されていない場合
	 */
	public void insert(String protcol, String host, List<String> path, Map<String, String> parameter, Map<String, String> requestHeader, Map<String, List<String>> responceHeader, String httpStatusCode, String httpVersion, String fileid, long timeid, Date createDate, Date updateDate) throws DataStoreManagerException, CrawlerException ;
	
	/**
	 * PAGESテーブルを削除する。<p/>
	 * テーブル削除に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void dropTable() throws DataStoreManagerException;
}
