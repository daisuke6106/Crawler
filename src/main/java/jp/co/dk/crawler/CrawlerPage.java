package jp.co.dk.crawler;

import java.util.Date;
import java.util.HashMap;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.http.header.RequestHeader;
import jp.co.dk.browzer.http.header.ResponseHeader;
import jp.co.dk.document.ByteDump;
import jp.co.dk.document.exception.DocumentFatalException;

/**
 * AbstractPageはクローラにて使用される単一のページを表す抽象クラスです。<p/>
 * 本クラスにて接続先のページ情報のデータストアへの保存、データストアからの読み込みを行います。<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class CrawlerPage extends jp.co.dk.browzer.Page {
	
	/**
	 * <p>コンストラクタ</p>
	 * 指定のURL、データストアマネージャのインスタンスを元に、ページオブジェクトのインスタンスを生成します。
	 * 
	 * @param url URL文字列
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	public CrawlerPage(String url) throws PageIllegalArgumentException, PageAccessException {
		super(url, new HashMap<String,String>(), false);
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
	protected CrawlerPage(String url, RequestHeader requestHeader, ResponseHeader responseHeader, Date accessDate, ByteDump data) throws PageIllegalArgumentException {
		super(url, requestHeader, responseHeader, accessDate, data);
	}

	/** ファイルID */
	protected String fileId;
	
	/** タイムID */
	protected long timeId = -1;
	
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
	
}