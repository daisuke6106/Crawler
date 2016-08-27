package jp.co.dk.crawler.scenario.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageSaveException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.document.exception.DocumentException;

public class FileSaveMoveAction extends MoveAction {
	
	/** 保存先ディレクトリ */
	protected File dir;
	
	/**  ファイル名 */
	protected String fileName;
	
	/** タイトルフォーマット */
	private static Pattern titlePattern = Pattern.compile("%title");
	
	/** 日付フォーマット */
	private static Pattern datePattern = Pattern.compile("%date\\{(.*)\\}");
	
	public FileSaveMoveAction(String[] args) throws MoveActionFatalException {
		super(args);
		if (args.length != 2) throw new RuntimeException("保存先ディレクトリとファイルフォーマットが指定されていません。");
		this.dir      = new File(this.args[0]);
		this.fileName = this.args[1];
	}

	@Override
	public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		String fileName = new String(this.fileName);
		
		Matcher titleMatcher = titlePattern.matcher(fileName);
		if (titleMatcher.find()) {
			try {
				jp.co.dk.document.File file = browzer.getPage().getDocument();
				if (file instanceof jp.co.dk.document.html.HtmlDocument) {
					jp.co.dk.document.html.HtmlDocument htmlDocument = (jp.co.dk.document.html.HtmlDocument)file;
					String title = htmlDocument.getTitle();
					fileName = titleMatcher.replaceAll(title);
				} else {
					fileName = titleMatcher.replaceAll("");
				}
			} catch (PageAccessException | DocumentException e) {
				fileName = titleMatcher.replaceAll("");
			}
		}
		
		Matcher dateMatcher = datePattern.matcher(fileName);
		if (dateMatcher.find()) {
			SimpleDateFormat sdf = new SimpleDateFormat(dateMatcher.group(1));
			String date = sdf.format(new Date());
			fileName = dateMatcher.replaceAll(date);
		}
		
		try {
			browzer.save(this.dir, fileName);
		} catch (PageAccessException | PageSaveException | DocumentException e) {
			new RuntimeException("保存に失敗しました。", e);
		}
	}
}
