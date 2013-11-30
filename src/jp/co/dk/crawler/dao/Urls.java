package jp.co.dk.crawler.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.dao.record.UrlsRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataAccessObject;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/**
 * Urlsは、URLSテーブルに対しての制御を行うDAOインスタンスが実装するインターフェース。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public interface Urls extends DataAccessObject {
	
	/**
	 * URLSテーブルを作成する。<p/>
	 * テーブル作成に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void createTable() throws DataStoreManagerException;
	
	/**
	 * URLSテーブルから特定の１レコードを取得する。
	 * 
	 * @param protcol    プロトコル文字列（必須）
	 * @param host       ホスト名（必須）
	 * @param path       パスリスト（設定されていない場合、空のリストで置き換え）
	 * @param parameter  パラメータマップ（設定されていない場合、空のマップで置き換え）
	 * @throws DataStoreManagerException 取得に失敗した場合
	 */
	public UrlsRecord select(String protcol, String host, List<String> path, Map<String, String> parameter) throws DataStoreManagerException ;
	
	/**
	 * URLSテーブルから指定の１レコードを登録する。<p/>
	 * 必須パラメータが設定されていない場合、例外を送出します。<br/>
	 * <br/>
	 * pathと、parameterに限り設定されていない場合（nullの場合）、空のリスト、マップインスタンスで置き換えます。<br/>
	 * 
	 * @param protcol    プロトコル文字列（必須）
	 * @param host       ホスト名（必須）
	 * @param path       パスリスト（設定されていない場合、空のリストで置き換え）
	 * @param parameter  パラメータマップ（設定されていない場合、空のマップで置き換え）
	 * @param url        URL文字列
	 * @param fileid     ファイルID
	 * @param createDate 登録日付
	 * @param updateDate 更新日付
	 * @throws DataStoreManagerException 登録に失敗した場合
	 * @throws CrawlerException 必須パラメータが設定されていない場合
	 */
	public void insert(String protcol, String host, List<String> path, Map<String, String> parameter, String url, long fileid, Date createDate, Date updateDate) throws DataStoreManagerException, CrawlerException ;
	
	/**
	 * URLSテーブルを削除する。<p/>
	 * テーブル削除に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void dropTable() throws DataStoreManagerException;
}
