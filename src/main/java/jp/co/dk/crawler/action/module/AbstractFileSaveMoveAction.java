package jp.co.dk.crawler.action.module;

import java.io.File;
import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.action.MoveAction;
import static jp.co.dk.crawler.message.CrawlerMessage.*;

/**
 * AbstractFileSaveMoveActionは、指定のページを指定されたディレクトリに保存する際に使用する基底アクションクラス。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class AbstractFileSaveMoveAction extends MoveAction {
	
	/** 保存先ディレクトリ */
	private File dir;
	
	/**  ファイル名 */
	private String fileName;
	
	/**
	 * <p>コンストラクタ</p>
	 * 遷移時アクションの実行時に使用する引数を基にインスタンスを生成します。<br/>
	 * 引数[0]=保存先ディレクトリ<br/>
	 * 引数[1]=ファイル名<br/>
	 *  
	 * @param args アクション時の引数
	 * @throws MoveActionFatalException 引数が不正な場合
	 */
	public AbstractFileSaveMoveAction(String[] args) throws MoveActionFatalException {
		super(args);
	}
	
	protected void setDir(String dir) throws MoveActionFatalException {
		this.dir = new File(dir);
		if (!this.dir.exists()) throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"ディレクトリが存在しません。", this.args[0]});
		if (!this.dir.isDirectory()) throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"ディレクトリではありません。", this.args[0]});
	}
	
	protected void setFileName(String filename) throws MoveActionFatalException {
		this.fileName = filename;
	}
	
	@Override
	public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		// 保存を実行する。
		this.save(this.dir, this.replaceFormat(movable, browzer, fileName), movable, browzer);
		
	}
	
	/**
	 * <p>保存実行</p>
	 * 指定されたディレクトリに指定のファイル名にて保存を実行する。
	 * 
	 * @param dir 保存先ディレクトリ
	 * @param fileName 保存時のファイル名
	 * @param movable 移動先が記載された要素
	 * @param browzer 移動後のブラウザオブジェクト
	 * @throws MoveActionFatalException 保存時に致命的例外が発生した場合
	 */
	protected abstract void save(File dir, String fileName, MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException ;
}
