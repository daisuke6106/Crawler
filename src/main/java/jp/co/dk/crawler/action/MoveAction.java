package jp.co.dk.crawler.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.logger.Logger;
import jp.co.dk.logger.LoggerFactory;

/**
 * MoveActionは、ページ移動時に追加処理を行う際に実装する抽象クラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class MoveAction {
	
	public static final String MOVE_ACTION_PACKAGE = "jp.co.dk.crawler.action.module";
	
	/** ロガーインスタンス */
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/** アクションクラス実行時引数 */
	protected String[] args;
	
	/**
	 * <p>コンストラクタ</p>
	 * 遷移時実行処理の引数を元にMoveActionのインスタンスを生成します。
	 * @param args アクション実行時引数
	 * @throws MoveActionFatalException 引数が不正な場合
	 */
	public MoveAction(String args[]) throws MoveActionFatalException {
		this.args = args;
	}
	
	/**
	 * ページ移動前に行う処理
	 * 
	 * @param movable 移動先が記載された要素
	 * @param browzer 移動前のブラウザオブジェクト
	 * @throws MoveActionException 再起可能例外が発生した場合
	 * @throws MoveActionFatalException 致命的例外が発生した場合
	 */
	public void beforeAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {}
	
	/**
	 * ページ移動後に行う処理
	 * 
	 * @param movable 移動先が記載された要素
	 * @param browzer 移動後のブラウザオブジェクト
	 * @throws MoveActionException 再起可能例外が発生した場合
	 * @throws MoveActionFatalException 致命的例外が発生した場合
	 */
	public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {}
	
	/**
	 * ページ移動時にエラーが発生した場合に行う処理
	 * 
	 * @param movable 移動先が記載された要素
	 * @param browzer 移動後のブラウザオブジェクト
	 * @throws MoveActionException 再起可能例外が発生した場合
	 * @throws MoveActionFatalException 致命的例外が発生した場合
	 */
	public void errorAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {}
	
	/** インデックス */
	protected int index = 0;

	/** URLハッシュフォーマット */
	private static Pattern urlHashPattern = Pattern.compile("%urlhash");
	
	/** タイトルフォーマット */
	private static Pattern titlePattern = Pattern.compile("%title");
	
	/** 日付フォーマット */
	private static Pattern datePattern = Pattern.compile("%date\\{(.*)\\}");
	
	/** インデックスフォーマット */
	private static Pattern indexPattern = Pattern.compile("%index\\{(.*)\\}");
	
	
	protected String replaceFormat(MovableElement movable, Browzer browzer, String formatBase) {
		String format = new String(formatBase);

		Matcher urlHashMatcher = urlHashPattern.matcher(format);
		if (urlHashMatcher.find()) format = urlHashMatcher.replaceAll(Integer.toString(browzer.getPage().getURL().hashCode()));
		
		Matcher titleMatcher = titlePattern.matcher(format);
		if (titleMatcher.find()) {
			try {
				jp.co.dk.document.File file = browzer.getPage().getDocument();
				if (file instanceof jp.co.dk.document.html.HtmlDocument) {
					jp.co.dk.document.html.HtmlDocument htmlDocument = (jp.co.dk.document.html.HtmlDocument)file;
					String title = htmlDocument.getTitle();
					format = titleMatcher.replaceAll(title);
				} else {
					format = titleMatcher.replaceAll("");
				}
			} catch (PageAccessException | DocumentException e) {
				format = titleMatcher.replaceAll("");
			}
		}
		
		Matcher dateMatcher = datePattern.matcher(format);
		if (dateMatcher.find()) {
			SimpleDateFormat sdf = new SimpleDateFormat(dateMatcher.group(1));
			String date = sdf.format(new Date());
			format = dateMatcher.replaceAll(date);
		}
		
		Matcher indexMatcher = indexPattern.matcher(format);
		if (indexMatcher.find()) format = indexMatcher.replaceAll(String.format("%0" + indexMatcher.group(1) + "d", new Integer(++index)));
		
		// ファイル名のエスケープ
		format = format.replaceAll("\\\\", "");
		format = format.replaceAll("/"   , "");
		format = format.replaceAll(":"   , "");
		format = format.replaceAll("\\*" , "");
		format = format.replaceAll("\\?" , "");
		format = format.replaceAll("\""  , "");
		format = format.replaceAll("<"   , "");
		format = format.replaceAll(">"   , "");
		format = format.replaceAll("|"   , "");
		return format;
	}
	
}
