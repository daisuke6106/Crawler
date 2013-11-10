package jp.co.dk.crawler.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.dao.record.LinksRecord;
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
	 * LINKSテーブルから遷移元ページが特定のページである遷移先ページのレコードを複数取得する。
	 * 
	 * @param protcol   プロトコル名
	 * @param host      ホスト名
	 * @param path      パス
	 * @param filename  ファイル名
	 * @param parameter パラメータ
	 * @return 取得したレコードオブジェクト
	 * @throws DataStoreManagerException 取得に失敗した場合
	 */
	public List<LinksRecord> select(String protcol, String host, List<String> path, String filename, Map<String, String> parameter) throws DataStoreManagerException;
	
	/**
	 * LINKSテーブルから特定の１レコードを取得する。<p/>
	 * 必須パラメータが設定されていない場合、例外を送出します。<br/>
	 * <br/>
	 * path、filenameと、parameterに限り設定されていない場合（nullの場合）、空のリスト、マップインスタンスで置き換えます。<br/>
	 * 
	 * @param from_protcol   プロトコル文字列（必須）
	 * @param from_host      ホスト名（必須）
	 * @param from_path      パスリスト（設定されていない場合、空のリストで置き換え）
	 * @param from_filename  ファイル名（設定されていない場合、空の文字列で置き換え）
	 * @param from_parameter パラメータマップ（設定されていない場合、空のマップで置き換え）
	 * @param to_protcol     プロトコル文字列（必須）
	 * @param to_host        ホスト名（必須）
	 * @param to_path        パスリスト（設定されていない場合、空のリストで置き換え）
	 * @param to_filename    ファイル名（設定されていない場合、空の文字列で置き換え）
	 * @param to_parameter   パラメータマップ（設定されていない場合、空のマップで置き換え）
	 * @return 取得したレコードオブジェクト
	 * @throws DataStoreManagerException 取得に失敗した場合
	 * @throws CrawlerException          必須パラメータが設定されていない場合
	 */
	public LinksRecord select(String from_protcol, String from_host, List<String> from_path, String from_filename, Map<String, String> from_parameter, String to_protcol, String to_host, List<String> to_path, String to_filename, Map<String, String> to_parameter, Date createDate, Date updateDate) throws DataStoreManagerException, CrawlerException;
	
	/**
	 * INKSテーブルから指定の１レコードを登録する。<p/>
	 * 必須パラメータが設定されていない場合、例外を送出します。<br/>
	 * <br/>
	 * path、filenameと、parameterに限り設定されていない場合（nullの場合）、空のリスト、マップインスタンスで置き換えます。<br/>
	 * 
	 * @param from_protcol   プロトコル文字列（必須）
	 * @param from_host      ホスト名（必須）
	 * @param from_path      パスリスト（設定されていない場合、空のリストで置き換え）
	 * @param from_filename  ファイル名（設定されていない場合、空の文字列で置き換え）
	 * @param from_parameter パラメータマップ（設定されていない場合、空のマップで置き換え）
	 * @param to_protcol     プロトコル文字列（必須）
	 * @param to_host        ホスト名（必須）
	 * @param to_path        パスリスト（設定されていない場合、空のリストで置き換え）
	 * @param to_filename    ファイル名（設定されていない場合、空の文字列で置き換え）
	 * @param to_parameter   パラメータマップ（設定されていない場合、空のマップで置き換え）
	 * @param createDate     登録日付
	 * @param updateDate     更新日付
	 * @throws DataStoreManagerException 登録に失敗した場合
	 * @throws CrawlerException 必須パラメータが設定されていない場合
	 */
	public void insert(String from_protcol, String from_host, List<String> from_path, String from_filename, Map<String, String> from_parameter, String to_protcol, String to_host, List<String> to_path, String to_filename, Map<String, String> to_parameter, Date createDate, Date updateDate) throws DataStoreManagerException, CrawlerException ;
	
	/**
	 * LINKSテーブルを削除する。<p/>
	 * テーブル削除に失敗した場合、例外が送出される。
	 * 
	 * @throws DataStoreManagerException テーブル作成に失敗した場合
	 */
	public void dropTable() throws DataStoreManagerException;
}
