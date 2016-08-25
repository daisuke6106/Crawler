package jp.co.dk.crawler.db;

import java.util.Date;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.http.header.RequestHeader;
import jp.co.dk.browzer.http.header.ResponseHeader;
import jp.co.dk.crawler.CrawlerPage;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.document.ByteDump;

/**
 * AbstractPageはクローラにて使用される単一のページを表す抽象クラスです。<p/>
 * 本クラスにて接続先のページ情報のデータストアへの保存、データストアからの読み込みを行います。<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class AbstractPage extends CrawlerPage {
	
	/**
	 * <p>コンストラクタ</p>
	 * 指定のURL、データストアマネージャのインスタンスを元に、ページオブジェクトのインスタンスを生成します。
	 * 
	 * @param url URL文字列
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	public AbstractPage(String url) throws PageIllegalArgumentException, PageAccessException {
		super(url);
	}
	
	/**
	 * <p>コンストラクタ</p>
	 * 指定のURL、リクエストヘッダ、レスポンスヘッダ、データ、イベントハンドラを基にページオブジェクトのインスタンスを生成します。
	 * 本コンストラクタはすでに保存されているページ情報からページオブジェクトを復元する際に使用します。
	 * 
	 * @param url URL文字列
	 * @param requestHeader リクエストヘッダ
	 * @param responseHeader レスポンスヘッダ
	 * @param data データオブジェクト
	 * @throws PageIllegalArgumentException データが不正、もしくは不足していた場合
	 */
	protected AbstractPage(String url, RequestHeader requestHeader, ResponseHeader responseHeader, Date accessDate, ByteDump data) throws PageIllegalArgumentException {
		super(url, requestHeader, responseHeader, accessDate, data);
	}
	/**
	 * このページ情報をデータストアに保存する。<p/>
	 * 
	 * @return 保存結果（true=保存された、false=すでにデータが存在するため、保存されなかった）
	 * @throws CrawlerSaveException データストアの登録時に必須項目が設定されていなかった場合
	 */
	public abstract boolean save() throws CrawlerSaveException;
	
	/**
	 * このページが既にデータストアに保存されているか判定します。<p/>
	 * 既に保存されている場合、trueを返却し、保存されているない場合、falseを返却します。<br/>
	 * 
	 * @return 判定結果（true=保存済みである、false=未だに保存されていないページである）
	 * @throws CrawlerException データストアへ対する操作にて例外が発生した場合
	 */
	public abstract boolean isSaved() throws CrawlerSaveException;
	
	/**
	 * このページをデータストアに保存されているものと比較して最新状態であるかどうかを判定します。<p/>
	 * 最新である場合はtrue、最新でない、もしくはページが保存すらされていない場合は、falseを返却します。<br/>
	 * <br/>
	 * 判定は以下の手順に沿って行われます。<br/>
	 * １．ページが保存すらされていない場合は、falseを返却<br/>
	 * ２．このページと保存されてるページで更新日付が異なる場合はfalse。<br/>
	 * ３．このページと保存されているページで更新日付が同じ、かつ、保存されているデータが完全に一致売る場合は、true<br/>
	 * 　　（更新日付がともにnullも一致と判定、データの差異比較の場合も、同様）
	 * 
	 * @return 判定結果（true=最新である、false=最新でない、またはページが保存されていない）
	 * @throws CrawlerSaveException  データストアへ対する操作にて例外が発生した場合
	 */
	public abstract boolean isLatest() throws CrawlerSaveException ;
	
	@Override
	protected abstract AbstractUrl createUrl(String url) throws PageIllegalArgumentException;
		
}