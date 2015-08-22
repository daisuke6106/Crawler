package jp.co.dk.crawler.gdb.neo4j.message;

import java.io.Serializable;

import jp.co.dk.message.AbstractMessage;

/**
 * DataStoreManagerMessageは、データストアの操作にて使用するメッセージを定義する定数クラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class CrawlerNeo4JMessage extends AbstractMessage implements Serializable{
	
	/** シリアルバージョンID */
	private static final long serialVersionUID = -9157146750413541129L;
	
	/** Neo4J接続パラメータが設定されていません。 */
	public static final CrawlerNeo4JMessage NEO4JPARAMETER_IS_NOT_SET = new CrawlerNeo4JMessage("E000");
	
	/** Neo4J接続先サーバが設定されていません。 */
	public static final CrawlerNeo4JMessage NEO4JSERVER_IS_NOT_SET = new CrawlerNeo4JMessage("E001");
	
	/** Neo4Jユーザ名が設定されていません。 */
	public static final CrawlerNeo4JMessage NEO4JUSERNAME_IS_NOT_SET = new CrawlerNeo4JMessage("E002");
	
	/** Neo4Jパスワードが設定されていません。 */
	public static final CrawlerNeo4JMessage NEO4JPASSWORD_IS_NOT_SET = new CrawlerNeo4JMessage("E003");
	
	/** Cypherが設定されていません。 */
	public static final CrawlerNeo4JMessage CYPHER_IS_NOT_SET = new CrawlerNeo4JMessage("E004");

	/** トランザクションが開始されていません。 */
	public static final CrawlerNeo4JMessage TRANSACTION_IS_NOT_START = new CrawlerNeo4JMessage("E005");
	
	/** パラメータが不正です。KEY=[{0}],PARAMETER=[{1}] */
	public static final CrawlerNeo4JMessage PARAMETER_IS_FRAUD = new CrawlerNeo4JMessage("E006");
	
	protected CrawlerNeo4JMessage(String messageId) {
		super(messageId);
	}

}
