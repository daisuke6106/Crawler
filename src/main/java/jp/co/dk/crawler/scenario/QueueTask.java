package jp.co.dk.crawler.scenario;

import java.util.List;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.scenario.action.MoveAction;

public class QueueTask {
	
	/** 遷移要素 */
	protected MovableElement movableElement;
	
	/** 移動時に実行するアクション */
	protected List<MoveAction> moveActionList;
	
	QueueTask(MovableElement movableElement, List<MoveAction> moveActionList) {
		this.movableElement = movableElement;
		this.moveActionList = moveActionList;
	}
	
	/**
	 * 本要素が保持している遷移先要素を返却する。
	 * @return 遷移先要素
	 */
	public MovableElement getMovableElement() {
		return this.movableElement;
	}
	
	/**
	 * シナリオ実施前に行う前処理
	 * 
	 * @param browzer 本シナリオ実施前のブラウザオブジェクト
	 * @throws MoveActionException 再起可能例外が発生した場合
	 * @throws MoveActionFatalException 致命的例外が発生した場合
	 */
	public void beforeScenario(Browzer browzer)  throws MoveActionException, MoveActionFatalException {}
	
	/**
	 * ページ移動前に行う処理
	 * 
	 * @param movable 移動先が記載された要素
	 * @param browzer 移動前のブラウザオブジェクト
	 * @throws MoveActionException 再起可能例外が発生した場合
	 * @throws MoveActionFatalException 致命的例外が発生した場合
	 */
	public MoveControl beforeAction(Browzer browzer) throws MoveActionException, MoveActionFatalException {
		for(MoveAction moveAction : moveActionList) moveAction.beforeAction(this.movableElement, browzer);
		return MoveControl.Continuation;
	}
	
	/**
	 * ページ移動後に行う処理
	 * 
	 * @param movable 移動先が記載された要素
	 * @param browzer 移動後のブラウザオブジェクト
	 * @throws MoveActionException 再起可能例外が発生した場合
	 * @throws MoveActionFatalException 致命的例外が発生した場合
	 */
	public void afterAction(Browzer browzer) throws MoveActionException, MoveActionFatalException {
		for(MoveAction moveAction : moveActionList) moveAction.afterAction(this.movableElement, browzer);
	}
	
	/**
	 * ページ移動時にエラーが発生した場合に行う処理
	 * 
	 * @param movable 移動先が記載された要素
	 * @param browzer 移動後のブラウザオブジェクト
	 * @throws MoveActionException 再起可能例外が発生した場合
	 * @throws MoveActionFatalException 致命的例外が発生した場合
	 */
	public void errorAction(Browzer browzer) throws MoveActionException, MoveActionFatalException {
		for(MoveAction moveAction : moveActionList) moveAction.errorAction(this.movableElement, browzer);
	}
	
	/**
	 * シナリオ実施後に行う後処理
	 * 
	 * @param browzer 本シナリオ実施後のブラウザオブジェクト
	 * @throws MoveActionException 再起可能例外が発生した場合
	 * @throws MoveActionFatalException 致命的例外が発生した場合
	 */
	public void afterScenario(Browzer browzer)  throws MoveActionException, MoveActionFatalException {}
	
}
