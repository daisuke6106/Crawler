package jp.co.dk.crawler.action.module;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageSaveException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.action.MoveActionName;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.HtmlElement;
import jp.co.dk.document.message.DocumentMessage;
import jp.co.dk.logger.Loggable;
import static jp.co.dk.crawler.message.CrawlerMessage.*;

/**
 * FileSaveHtmlMoveActionは、指定のページを指定されたディレクトリに指定の名称で保存します。
 * 
 * @version 1.0
 * @author D.Kanno
 */
@MoveActionName(
	name           = "file_save_html",  
	manualTitle    = "ページデータ保存",
	manualText     = "ページデータを指定ディレクトリに指定ファイル名で保存します。",
	manualArgument = {
		"ディレクトリへのパス", 
		"ファイル名（%title=HTMLのタイトル、%date{yyyyMMddHHmmss}=日付、{X}内には日付フォーマット、%index{X}=連番、{X}内には桁数）",
		"保存対象の要素指定文字列" 
	},
	manualExample  = {"file_save('/tmp/,'%date{yyyyMMdd_HHmmss}_%index{10},'body #info'')と指定した場合、"
			+ "/tmp/ディレクトリに「20161201_235959_0000000010」というファイル名で"
			+ "HTML内の「body」タグ内にあるIDが「info」内の内容を保存します。"}
)
public class FileSaveHtmlMoveAction extends AbstractFileSaveMoveAction {
	
	/** 要素指定文字列 */
	protected String tagStr;
	
	/**
	 * <p>コンストラクタ</p>
	 * 遷移時アクションの実行時に使用する引数を基にインスタンスを生成します。<br/>
	 * 引数[0]=保存先ディレクトリ<br/>
	 * 引数[1]=ファイル名<br/>
	 *  
	 * @param args アクション時の引数
	 * @throws MoveActionFatalException 引数が不正な場合
	 */
	public FileSaveHtmlMoveAction(String[] args) throws MoveActionFatalException {
		super(args);
		if (args.length != 3) throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"保存先ディレクトリとファイルフォーマット、保存対象の要素指定文字列が指定されていません。", args.toString()});
		this.setDir(args[0]);
		this.setFileName(args[1]);
		this.tagStr = args[2];
	}

	@Override
	protected void save(File dir, String fileName, MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		try {
			jp.co.dk.document.File file = browzer.getPage().getDocument();
			if (file instanceof HtmlDocument) {
				HtmlDocument htmlDocument = (HtmlDocument)file;
				List<HtmlElement> hitElements = htmlDocument.getNode(this.tagStr);
				for (int i=0; i < hitElements.size(); i++) {
					HtmlElement htmlElement = (HtmlElement)hitElements.get(i);
					String outputContent = htmlElement.getContent();
					fileName = fileName + "." + i;
					java.io.File path = new java.io.File(new StringBuilder(dir.getAbsolutePath()).append('/').append(fileName).toString());
					if (path.exists()) throw new MoveActionException(DocumentMessage.ERROR_FILE_APLREADY_EXISTS_IN_THE_SPECIFIED_PATH, path.getAbsolutePath());
					
					try (OutputStream outputStream = new FileOutputStream(path)) {
						outputStream.write(outputContent.getBytes());
					} catch (IOException e) {
						throw new MoveActionException(DocumentMessage.ERROR_FAILED_TO_SAVE_FILE, path.getPath(), e);
					}
				}
				
			} else {
				browzer.save(dir, fileName);
			}
			// ログに出力する。
			final String printFileName = fileName;
			this.logger.info(new Loggable(){
				@Override
				public String printLog(String lineSeparator) {
					return "保存が完了しました。PATH=[" + dir.toString() + "], FILENAME=[" +  printFileName + "]";
				}}
			);
		} catch (PageAccessException | DocumentException | PageSaveException e) {
			throw new MoveActionException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"保存に失敗しました。", fileName}, e);
		}
		
	}
}
