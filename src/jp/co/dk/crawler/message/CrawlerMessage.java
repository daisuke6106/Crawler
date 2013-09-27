package jp.co.dk.crawler.message;

import jp.co.dk.message.AbstractMessage;

/**
 * DataStoreManagerMessageは、データストアの操作にて使用するメッセージを定義する定数クラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class CrawlerMessage extends AbstractMessage{
	
	/** 指定のデータストアは対応していません。データストア種別=[{0}] */
	public static final CrawlerMessage DETASTORETYPE_IS_NOT_SUPPORT = new CrawlerMessage("E001");
	
	/** レコードインスタンスの生成に失敗しました。必須パラメータが設定されていません。PARAM=[{0}] */
	public static final CrawlerMessage PARAMETER_IS_NOT_SET = new CrawlerMessage("E002");
	
	protected CrawlerMessage(String messageId) {
		super(messageId);
	}

}
