package jp.co.dk.crawler.scenario.action.module;

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
import jp.co.dk.crawler.scenario.ManualArgument;
import jp.co.dk.crawler.scenario.action.MoveAction;
import jp.co.dk.crawler.scenario.action.MoveActionName;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.logger.Loggable;
import static jp.co.dk.crawler.message.CrawlerMessage.*;

/**
 * FileSaveMoveActionは、指定のページを指定されたディレクトリに指定の名称で保存します。
 * 
 * @version 1.0
 * @author D.Kanno
 */
@MoveActionName(
	name           = "file_save",  
	manualTitle    = "ページデータ保存",
	manualText     = "ページデータを指定ディレクトリに指定ファイル名で保存します。",
	manualArgument = {
		"ディレクトリへのパス", 
		"ファイル名（%title=HTMLのタイトル、%date{yyyyMMddHHmmss}=日付、{X}内には日付フォーマット、%index{X}=連番、{X}内には桁数）"
	},
	manualExample  = {"file_save('/tmp/,'%date{yyyyMMdd_HHmmss}_%index{10}')と指定した場合、/tmp/ディレクトリに「20161201_235959_0000000010」というファイル名で保存します。"}
)
public class FileSaveMoveAction extends MoveAction {
	
	/** 保存先ディレクトリ */
	protected File dir;
	
	/**  ファイル名 */
	protected String fileName;
	
	/** タイトルフォーマット */
	private static Pattern titlePattern = Pattern.compile("%title");
	
	/** 日付フォーマット */
	private static Pattern datePattern = Pattern.compile("%date\\{(.*)\\}");
	
	/** インデックスフォーマット */
	private static Pattern indexPattern = Pattern.compile("%index\\{(.*)\\}");
	
	/** インデックス */
	protected int index = 0;
	
	/**
	 * <p>コンストラクタ</p>
	 * 遷移時アクションの実行時に使用する引数を基にインスタンスを生成します。<br/>
	 * 引数[0]=保存先ディレクトリ<br/>
	 * 引数[1]=ファイル名<br/>
	 *  
	 * @param args アクション時の引数
	 * @throws MoveActionFatalException 引数が不正な場合
	 */
	public FileSaveMoveAction(String[] args) throws MoveActionFatalException {
		super(args);
		if (args.length != 2) throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"保存先ディレクトリとファイルフォーマットが指定されていません。", args.toString()});
		this.dir      = new File(this.args[0]);
		if (!this.dir.exists()) throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"ディレクトリが存在しません。", this.args[0]});
		if (!this.dir.isDirectory()) throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"ディレクトリではありません。", this.args[0]});
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
		
		Matcher indexMatcher = indexPattern.matcher(fileName);
		if (indexMatcher.find()) fileName = indexMatcher.replaceAll(String.format("%0" + indexMatcher.group(1) + "d", new Integer(++index)));
		
		// ファイル名のエスケープ
		fileName = fileName.replaceAll("\\\\", "");
		fileName = fileName.replaceAll("/"   , "");
		fileName = fileName.replaceAll(":"   , "");
		fileName = fileName.replaceAll("\\*" , "");
		fileName = fileName.replaceAll("\\?" , "");
		fileName = fileName.replaceAll("\""  , "");
		fileName = fileName.replaceAll("<"   , "");
		fileName = fileName.replaceAll(">"   , "");
		fileName = fileName.replaceAll("|"   , "");
		
		try {
			browzer.save(this.dir, fileName);
		} catch (PageAccessException | PageSaveException | DocumentException e) {
			throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"保存に失敗しました。", fileName});
		}
		
		final String printFileName = fileName;
		this.logger.info(new Loggable(){
			@Override
			public String printLog(String lineSeparator) {
				return "保存が完了しました。PATH=[" + dir.toString() + "], FILENAME=[" +  printFileName + "]";
			}});
	}

	@Override
	public String manualTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String manualText(String lineseparater) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ManualArgument[] getManualArgument() {
		// TODO Auto-generated method stub
		return null;
	}
}
