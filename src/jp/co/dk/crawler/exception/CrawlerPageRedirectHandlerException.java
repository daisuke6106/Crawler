package jp.co.dk.crawler.exception;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.exception.BrowzingException;

/**
 * CrawlerPageRedirectHandlerExceptionは、ページリダイレクトハンドラにてHTTPステータスコードを判定した際に発生する例外クラスです。<p/>
 * HTTPステータスコードが404等を返却、そのページに遷移出来なかった場合などに発生します。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class CrawlerPageRedirectHandlerException extends BrowzingException {

	/** シリアルバージョンID */
	private static final long serialVersionUID = -6589147054844498464L;
	
	/** エラー発生ページオブジェクト */
	private jp.co.dk.crawler.Page page;
	
	/**
	 * コンストラクタ<p/>
	 * 遷移先のページオブジェクトと、例外オブジェクトを元に、例外クラスを生成します。
	 * 
	 * @param exception 例外オブジェクト
	 * @param page 例外が発生したページオブジェクト
	 */
	public CrawlerPageRedirectHandlerException(BrowzingException exception, jp.co.dk.crawler.Page page) {
		super(exception.getMessageObj(), exception.getEmbeddedStrList());
		this.page = page;
	}
	
	/**
	 * この例外が発生したページオブジェクトを返却します。
	 * @return ペジオブジェクト
	 */
	public Page getPage() {
		return page;
	}
	
}