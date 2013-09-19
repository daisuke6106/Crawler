package jp.co.dk.crawler;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;

/**
 * Crawlerは、ネットワーク上に存在するHTML、XML、ファイルを巡回し、指定された出力先へ保存を行う処理を制御するクラス。<p/>
 * 保存先には、MySql等のRDBのデータストアに保存される。<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class Crawler {
	
	/** データストアマネージャー */
	protected DataStoreManager dsm;
	
	/**
	 * コンストラクタ<p/>
	 * デフォルトのデータストアプロパティファイル（properties/DataStoreManager.properties）を元に、クローラクラスのインスタンスを生成する。<br/>
	 * 指定したデータストアプロパティの設定値が不足している等、プロパティインスタンスに不整合があった場合、例外を送出する。
	 * 
	 * @throws DataStoreManagerException データストアプロパティの取得に失敗した場合
	 */
	public Crawler() throws DataStoreManagerException {
		this(new DataStoreManagerProperty());
	}
	
	/**
	 * コンストラクタ<p/>
	 * 指定のデータストアプロパティを元に、クローラクラスのインスタンスを生成する。<br/>
	 * 指定したデータストアプロパティの設定値が不足している等、プロパティインスタンスに不整合があった場合、例外を送出する。
	 * 
	 * @param dataStoreManagerProperty データストア設定プロパティ
	 * @throws DataStoreManagerException データストアプロパティの取得に失敗した場合
	 */
	public Crawler(DataStoreManagerProperty dataStoreManagerProperty) throws DataStoreManagerException { 
		this.dsm = new DataStoreManager(dataStoreManagerProperty);
	}
	
	public void save(String url) throws BrowzingException {
		Page page = new Page(url);
	}
}
