package jp.co.dk.crawler;

import jp.co.dk.browzer.Url;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.datastoremanager.DataStoreManager;

public abstract class AbstractUrl extends Url {
	
	/** データストアマネージャ */
	protected DataStoreManager dataStoreManager;
	
	public AbstractUrl(String url, DataStoreManager dataStoreManager) throws PageIllegalArgumentException {
		super(url);
		this.dataStoreManager = dataStoreManager;
	}
	
	/**
	 * このページ情報をデータストアに保存する。<p/>
	 * 
	 * @return 保存結果（true=保存された、false=すでにデータが存在するため、保存されなかった）
	 * @throws CrawlerSaveException データストアの登録時に必須項目が設定されていなかった場合
	 */
	public abstract boolean save() throws CrawlerSaveException;
}
