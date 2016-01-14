package jp.co.dk.crawler.message;

import java.io.Serializable;

import jp.co.dk.message.AbstractMessage;

/**
 * DataStoreManagerMessageは、データストアの操作にて使用するメッセージを定義する定数クラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class CrawlerMessage extends AbstractMessage implements Serializable{
	
	/** シリアルバージョンID */
	private static final long serialVersionUID = -5583067693198405513L;

	/** 指定のデータストアは対応していません。データストア種別=[{0}] */
	public static final CrawlerMessage DETASTORETYPE_IS_NOT_SUPPORT = new CrawlerMessage("E001");
	
	/** レコードインスタンスの生成に失敗しました。必須パラメータが設定されていません。PARAM=[{0}] */
	public static final CrawlerMessage PARAMETER_IS_NOT_SET = new CrawlerMessage("E002");
	
	/** バイト配列からオブジェクトのインスタンスへの変換に失敗しました。PARAM=[{0}] */
	public static final CrawlerMessage FAILE_TO_CONVERT_TO_INSTANCE_OF_AN_OBJECT_FROM_A_BYTE_ARRAY = new CrawlerMessage("E003");
	
	/** ページ情報の保存に失敗しました。URL=[{0}] */
	public static final CrawlerMessage FAILE_TO_SAVE_PAGE = new CrawlerMessage("E004");
	
	/** ページ情報の取得に失敗しました。URL=[{0}] */
	public static final CrawlerMessage FAILE_TO_GET_PAGE = new CrawlerMessage("E005");
	
	/** データストアマネージャが設定されていません。 */
	public static final CrawlerMessage DATASTOREMANAGER_IS_NOT_SET = new CrawlerMessage("E006");
	
	/** ロガーが設定されていません。 */
	public static final CrawlerMessage LOGGER_IS_NOT_SET = new CrawlerMessage("E007");
	
	/** データストアマネージャの生成に失敗しました。 */
	public static final CrawlerMessage DATASTOREMANAGER_CAN_NOT_CREATE = new CrawlerMessage("E008");
	
	/** ページ情報の読込に失敗しました。URL=[{0}] */
	public static final CrawlerMessage FAILE_TO_READ_PAGE = new CrawlerMessage("E009");
	
	/** URL情報の読込に失敗しました。URL=[{0}] */
	public static final CrawlerMessage FAILE_TO_READ_URL = new CrawlerMessage("E010");
	
	protected CrawlerMessage(String messageId) {
		super(messageId);
	}

}
