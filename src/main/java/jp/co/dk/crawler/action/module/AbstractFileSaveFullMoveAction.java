package jp.co.dk.crawler.action.module;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.action.MoveAction;
import jp.co.dk.crawler.message.CrawlerMessage;
import static jp.co.dk.crawler.message.CrawlerMessage.*;

/**
 * AbstractFileSaveMoveActionは、指定のページを指定されたディレクトリに保存する際に使用する基底アクションクラス。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class AbstractFileSaveFullMoveAction extends MoveAction {
	
	/** 保存先ディレクトリ */
	private File dirbase;
	
	/**  ディレクトリ名 */
	private String dir;
	
	/**
	 * <p>コンストラクタ</p>
	 * 遷移時アクションの実行時に使用する引数を基にインスタンスを生成します。<br/>
	 * 引数[0]=保存先ディレクトリ<br/>
	 * 引数[1]=ファイル名<br/>
	 *  
	 * @param args アクション時の引数
	 * @throws MoveActionFatalException 引数が不正な場合
	 */
	public AbstractFileSaveFullMoveAction(String[] args) throws MoveActionFatalException {
		super(args);
	}
	
	protected void setDirBase(String dirbase) throws MoveActionFatalException {
		this.dirbase = new File(dirbase);
		if (!this.dirbase.exists()) throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"ディレクトリが存在しません。", dirbase});
		if (!this.dirbase.isDirectory()) throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"ディレクトリではありません。", dirbase});
	}
	
	protected void setDir(String dir) throws MoveActionFatalException {
		this.dir = dir;
	}
	
	@Override
	public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		// 保存を実行する。
		this.save(this.dirbase, this.replaceFormat(movable, browzer, dir), movable, browzer);
		
	}
	
	/**
	 * <p>保存実行</p>
	 * 指定されたディレクトリに指定のファイル名にて保存を実行する。
	 * 
	 * @param dirbase 保存先ディレクトリ
	 * @param dir 保存先のディレクトリ（フォーマット変換済み）
	 * @param movable 移動先が記載された要素
	 * @param browzer 移動後のブラウザオブジェクト
	 * @throws MoveActionException 保存に失敗した場合
	 * @throws MoveActionFatalException 保存時に致命的例外が発生した場合
	 */
	protected abstract void save(File dirbase, String dir, MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException ;
	
	/**
	 * 
	 * @param dir 保存先ディレクトリ
	 * @param filename ファイル名
	 * @param data 出力内容
	 * @throws MoveActionException
	 * @throws MoveActionFatalException
	 */
	protected void save(File dir, String filename, byte[] data) throws MoveActionException, MoveActionFatalException {
		java.io.File path = new java.io.File(new StringBuilder(dir.getAbsolutePath()).append('/').append(filename).toString());
		if (path.exists()) throw new MoveActionException(CrawlerMessage.ERROR_FILE_APLREADY_EXISTS_IN_THE_SPECIFIED_PATH, path.getAbsolutePath());
		try (OutputStream outputStream = new FileOutputStream(path)) {
			outputStream.write(data);
		} catch (IOException e) {
			throw new MoveActionException(CrawlerMessage.ERROR_FAILED_TO_SAVE_FILE, path.getPath(), e);
		}
	}
}
