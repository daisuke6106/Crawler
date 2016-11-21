package jp.co.dk.crawler.action.module;


import java.io.File;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageSaveException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.action.MoveActionName;
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
public class FileSaveMoveAction extends AbstractFileSaveMoveAction {
	
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
		this.setDir(args[0]);
		this.setFileName(args[1]);
	}
	
	@Override
	protected void save(File dir, String fileName, MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		try {
			browzer.save(dir, fileName);
		} catch (PageAccessException | PageSaveException | DocumentException e) {
			throw new MoveActionException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"保存に失敗しました。", fileName});
		}

		// ログに出力する。
		final String printFileName = fileName;
		this.logger.info(new Loggable(){
			@Override
			public String printLog(String lineSeparator) {
				return "保存が完了しました。PATH=[" + dir.toString() + "], FILENAME=[" +  printFileName + "]";
			}}
		);
		
	}
}
