package jp.co.dk.crawler.gdb.neo4j.exception;

import jp.co.dk.crawler.gdb.neo4j.message.CrawlerNeo4JMessage;
import jp.co.dk.message.exception.AbstractMessageException;

public class CrawlerNeo4JException  extends AbstractMessageException {

	/**
	 * シリアルバージョンID
	 */
	private static final long serialVersionUID = 6187528686419084458L;
	
	/**
	 * コンストラクタ<p>
	 * 
	 * 指定のメッセージで例外を生成します。
	 * 
	 * @param msg メッセージ定数インスタンス
	 * @since 1.0
	 */
	public CrawlerNeo4JException(CrawlerNeo4JMessage msg){
		super(msg);
	}
}
