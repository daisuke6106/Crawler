package jp.co.dk.crawler.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.dao.record.PagesRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataAccessObject;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/**
 * Linksは、LINKSテーブルに対しての制御を行うDAOインスタンスが実装するインターフェース。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public interface Links extends DataAccessObject {
	
	/**
	 * LINKSテーブルを作成する。<p/>
	 * テーブル作成に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void createTable() throws DataStoreManagerException;
	
	/**
	 * LINKSテーブルから特定の１レコードを取得する。
	 * 
	 * @param protocol  プロトコル名
	 * @param hostname  ホスト名
	 * @param path      パスのハッシュ値
	 * @param parameter パラメータのハッシュ値
	 * @return 取得したレコードオブジェクト
	 */
	public PagesRecord select(String protcol, String host, List<String> path, Map<String, String> parameter)throws DataStoreManagerException;
	
	/**
	 * INKSテーブルから指定の１レコードを登録する。<p/>
	 * 必須パラメータが設定されていない場合、例外を送出します。<br/>
	 * <br/>
	 * pathと、parameterに限り設定されていない場合（nullの場合）、空のリスト、マップインスタンスで置き換えます。<br/>
	 * 
	 * @param from_protcol   プロトコル文字列（必須）
	 * @param from_host      ホスト名（必須）
	 * @param from_path      パスリスト（設定されていない場合、空のリストで置き換え）
	 * @param from_parameter パラメータマップ（設定されていない場合、空のマップで置き換え）
	 * @param to_protcol     プロトコル文字列（必須）
	 * @param to_host        ホスト名（必須）
	 * @param to_path        パスリスト（設定されていない場合、空のリストで置き換え）
	 * @param to_parameter   パラメータマップ（設定されていない場合、空のマップで置き換え）
	 * @throws DataStoreManagerException 登録に失敗した場合
	 * @throws CrawlerException 必須パラメータが設定されていない場合
	 */
	public void insert(String from_protcol, String from_host, List<String> from_path, Map<String, String> from_parameter, String to_protcol, String to_host, List<String> to_path, Map<String, String> to_parameter)throws DataStoreManagerException, CrawlerException ;
	
	/**
	 * LINKSテーブルを削除する。<p/>
	 * テーブル削除に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void dropTable() throws DataStoreManagerException;
}
