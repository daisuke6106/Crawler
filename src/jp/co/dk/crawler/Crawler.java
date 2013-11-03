package jp.co.dk.crawler;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;
import jp.co.dk.property.exception.PropertyException;

/**
 * Crawlerは、ネットワーク上に存在するHTML、XML、ファイルを巡回し、指定された出力先へ保存を行う処理を制御するクラス。<p/>
 * 保存先には、MySql等のRDBのデータストアに保存される。<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class Crawler extends Browzer{
	
	/** データストアマネージャー */
	protected DataStoreManager dsm;
	
	/**
	 * コンストラクタ<p/>
	 * デフォルトのデータストアプロパティファイル（properties/DataStoreManager.properties）を元に、クローラクラスのインスタンスを生成する。<br/>
	 * 指定したデータストアプロパティの設定値が不足している等、プロパティインスタンスに不整合があった場合、例外を送出する。
	 * 
	 * @throws DataStoreManagerException データストアプロパティの取得に失敗した場合
	 * @throws PropertyException プロパティファイルの読み込みに失敗した場合
	 * @throws BrowzingException ページ情報の読み込みに失敗した場合
	 */
	public Crawler(String url) throws DataStoreManagerException, BrowzingException, PropertyException {
		this(url, new DataStoreManagerProperty());
	}
	
	/**
	 * コンストラクタ<p/>
	 * 指定のデータストアプロパティを元に、クローラクラスのインスタンスを生成する。<br/>
	 * 指定したデータストアプロパティの設定値が不足している等、プロパティインスタンスに不整合があった場合、例外を送出する。
	 * 
	 * @param dataStoreManagerProperty データストア設定プロパティ
	 * @throws DataStoreManagerException データストアプロパティの取得に失敗した場合
	 * @throws BrowzingException ページ情報の読み込みに失敗した場合
	 */
	public Crawler(String url, DataStoreManagerProperty dataStoreManagerProperty) throws DataStoreManagerException, BrowzingException { 
		super(url);
		this.dsm = new DataStoreManager(dataStoreManagerProperty);
	}
	
	/**
	 * 現在アクティブになっているページオブジェクトをデータストアへ保存する。
	 * @throws DataStoreManagerException 
	 */
	public void save() throws DataStoreManagerException {
		this.dsm.startTrunsaction();
		Page page = this.getPage();
		this.dsm.finishTrunsaction();
		
	}
	
	@Override
	protected Page ceatePage(String url) throws BrowzingException {
		return new jp.co.dk.crawler.Page(url, this.dsm);
	}
}
