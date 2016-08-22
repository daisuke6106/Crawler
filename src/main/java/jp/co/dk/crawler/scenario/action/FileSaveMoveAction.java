package jp.co.dk.crawler.scenario.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

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
	
	public FileSaveMoveAction(String[] args) throws MoveActionFatalException {
		super(args);
		if (args.length != 2) throw new RuntimeException("保存先ディレクトリとファイルフォーマットが指定されていません。");
		this.dir      = new File(this.args[0]);
		this.fileName = this.args[1];
	}

	@Override
	public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		String fileName = new String(this.fileName);
		if (fileName.contains("%D{date}")) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
			fileName = fileName.replace("%{date}", sdf.format(date));
		}
		try {
			browzer.save(this.dir, fileName);
		} catch (PageAccessException | PageSaveException | DocumentException e) {
			new RuntimeException("保存に失敗しました。", e);
		}
	}
}
