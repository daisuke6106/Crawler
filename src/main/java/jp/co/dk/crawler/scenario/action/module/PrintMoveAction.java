package jp.co.dk.crawler.scenario.action.module;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.scenario.action.MoveAction;
import jp.co.dk.crawler.scenario.action.MoveActionName;

/**
 * PrintMoveActionは、遷移時の情報を標準出力に出漁します。
 * 
 * @version 1.0
 * @author D.Kanno
 */
@MoveActionName(name="print")
public class PrintMoveAction extends MoveAction {

	/**
	 * <p>コンストラクタ</p>
	 * 遷移時アクションの実行時に使用する引数を基にインスタンスを生成します。
	 *  
	 * @param args アクション時の引数
	 * @throws MoveActionFatalException 引数が不正な場合
	 */
	public PrintMoveAction(String[] args) throws MoveActionFatalException {
		super(args);
	}

	@Override
	public void beforeAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		System.out.println("[BEFORE MOVE] NOW_URL=[" + browzer.getPage().getURL() + "] MOVE_TO=[" + movable.getUrl() + "]");
	}
	
	@Override
	public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		System.out.println("[AFTER  MOVE] NOW_URL=[" + browzer.getPage().getURL() + "]");
	}
	
	@Override
	public void errorAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		System.err.println("[   ERROR   ] NOW_URL=[" + browzer.getPage().getURL() + "]");
	}
}