package jp.co.dk.crawler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.Url;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.document.exception.DocumentFatalException;

/**
 * AbstractPageはクローラにて使用される単一のページを表す抽象クラスです。<p/>
 * 本クラスにて接続先のページ情報のデータストアへの保存、データストアからの読み込みを行います。<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class AbstractPage extends Page {
	
	/**
	 * コンストラクタ<p/>
	 * 指定のURL、データストアマネージャのインスタンスを元に、ページオブジェクトのインスタンスを生成します。
	 * 
	 * @param url URL文字列
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	public AbstractPage(String url) throws PageIllegalArgumentException, PageAccessException {
		super(url);
	}

	/** ファイルID */
	protected String fileId;
	
	/** タイムID */
	protected long timeId = -1;
	
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
	protected abstract Url createUrl(String url) throws PageIllegalArgumentException;
	
	/**
	 * このページのURLからページを一意に特定するためのファイルIDを算出し返却します。<p/>
	 * 算出はこのページの<br/>
	 * ・プロトコル<br/>
	 * ・ホスト名<br/>
	 * ・パス<br/>
	 * ・ファイル名<br/>
	 * ・パラメータ<br/>
	 * のハッシュ値を上記の順で乗算した結果が返却されます。
	 * 
	 * @return ファイルID
	 * @throws PageAccessException ページデータの取得に失敗した場合
	 * @throws DocumentFatalException 暗号化処理にて致命的例外が発生した場合
	 */
	public String getFileId() throws DocumentFatalException, PageAccessException {
		if (this.fileId != null) return this.fileId;
		this.fileId = this.getData().getHash();
		return this.fileId;
	}
	
	/**
	 * この現在の時刻からファイルIDに付随する登録時刻であるタイムIDを算出し返却します。<p/>
	 * デフォルトではJVMが稼働しているマシンの現在日付をlong値に変換した値が返却されます。<br/>
	 * 変更する場合はオーバーライドを使用してください。
	 * 
	 * @return タイムID
	 */
	public long getTimeId() {
		if (timeId != -1) return this.timeId;
		this.timeId = new Date().getTime();
		return this.timeId;
	}
	
	/**
	 * 作成日付を生成し、返却します。<p/>
	 * データストアに登録される際の作成日付を返却します。<br/>
	 * デフォルトではJVMが稼働しているマシンの現在日付が返却されます。<br/>
	 * 変更する場合はオーバーライドを使用してください。
	 * 
	 * @return 作成日付
	 */
	public Date getCreateDate() {
		return new Date();
	}
	
	/**
	 * 更新日付を生成し、返却します。<p/>
	 * データストアに登録される際の更新日付を返却します。<br/>
	 * デフォルトではJVMが稼働しているマシンの現在日付が返却されます。<br/>
	 * 変更する場合はオーバーライドを使用してください。
	 * 
	 * @return 更新日付
	 */
	public Date getUpdateDate() {
		return new Date();
	}
	
	/**
	 * 引数に設定された日付が同じものかどうか判定します。
	 * 
	 * @param date1 日付１
	 * @param date2 日付２
	 * @return 判定結果（true=一致、false=不一致）
	 */
	protected boolean sameDate(Date date1, Date date2) {
		if (date1 == null && date2 == null) return true; 
		if (date1 == null && date2 != null) return false;
		if (date1 != null && date2 == null) return false;
		if (date1.equals(date2)) return true;
		return false;
	}
	
	/**
	 * 引数に設定されたバイト配列が同じものかどうか判定します。
	 * 
	 * @param bytes1 バイト配列１
	 * @param bytes2 バイト配列２
	 * @return 判定結果（true=一致、false=不一致）
	 */
	protected boolean sameBytes(byte[] bytes1, byte[] bytes2) {
		if (bytes1 == null && bytes2 == null) return true; 
		if (bytes1 == null && bytes2 != null) return false;
		if (bytes1 != null && bytes2 == null) return false;
		if (bytes1.length == bytes2.length) {
			for (int i=0; i<bytes1.length; i++) {
				if (bytes1[i] != bytes2[i]) return false; 
			}
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public Map<String, String> getParameter() {
		return new ParameterMap(this.url.getParameter());
	}
	
}

class ParameterMap extends HashMap<String, String> {
	
	/** シリアルバージョンID */
	private static final long serialVersionUID = 6071724790375396636L;
	
	ParameterMap() {
		super();
	}
	
	ParameterMap(Map<String, String> parameter) {
		super(parameter);
	}
	
	@Override
	public int hashCode() {
		int originalHashCode = super.hashCode();
		if (originalHashCode == 0) return 1;
		return originalHashCode;
	}
}