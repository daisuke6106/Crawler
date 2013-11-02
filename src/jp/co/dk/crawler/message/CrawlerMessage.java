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
	
	/** バイト配列からオブジェクトのインスタンスへの変換に失敗しました。PARAM=[{0}] */
	public static final CrawlerMessage FAILE_TO_CONVERT_TO_INSTANCE_OF_AN_OBJECT_FROM_A_BYTE_ARRAY = new CrawlerMessage("E003");
	
	/** ページ情報の保存に失敗しました。URL=[{0}] */
	public static final CrawlerMessage FAILE_TO_SAVE_PAGE = new CrawlerMessage("E004");
	
	
	protected CrawlerMessage(String messageId) {
		super(messageId);
	}

}
