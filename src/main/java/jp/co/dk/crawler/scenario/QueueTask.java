package jp.co.dk.crawler.scenario;

import java.util.List;

import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.AbstractCrawler;
import jp.co.dk.crawler.scenario.action.MoveAction;
import jp.co.dk.logger.Loggable;
import jp.co.dk.logger.Logger;
import jp.co.dk.logger.LoggerFactory;

public class QueueTask {
	
	protected static Logger logger = LoggerFactory.getLogger(QueueTask.class);
	
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
	 * ページ移動前に行う処理
	 * 
	 * @param movable 移動先が記載された要素
	 * @param crawler 移動前のブラウザオブジェクト
	 * @throws MoveActionException 再起可能例外が発生した場合
	 * @throws MoveActionFatalException 致命的例外が発生した場合
	 */
	public MoveControl beforeAction(AbstractCrawler crawler) throws MoveActionException, MoveActionFatalException {
		QueueTask.logger.info(new Loggable(){
			@Override
			public String printLog(String lineSeparator) {
				return "ページ=[" +  crawler.getPage().getURL() + "]からURL=[" + movableElement.getUrl() + "]に移動します。";
			}});
		for(MoveAction moveAction : moveActionList) moveAction.beforeAction(this.movableElement, crawler);
		if (!crawler.isVisited(this.movableElement)) {
			return MoveControl.Transition;
		} else {
			return MoveControl.NotTransition;
		}
		
	}
	
	/**
	 * ページ移動後に行う処理
	 * 
	 * @param movable 移動先が記載された要素
	 * @param crawler 移動後のブラウザオブジェクト
	 * @throws MoveActionException 再起可能例外が発生した場合
	 * @throws MoveActionFatalException 致命的例外が発生した場合
	 */
	public void afterAction(AbstractCrawler crawler) throws MoveActionException, MoveActionFatalException {
		QueueTask.logger.info(new Loggable(){
			@Override
			public String printLog(String lineSeparator) {
				return "URL=[" + movableElement.getUrl() + "]に正常に移動できました。";
			}});
		for(MoveAction moveAction : moveActionList) moveAction.afterAction(this.movableElement, crawler);
	}
	
	/**
	 * ページ移動時にエラーが発生した場合に行う処理
	 * 
	 * @param movable 移動先が記載された要素
	 * @param crawler 移動後のブラウザオブジェクト
	 * @throws MoveActionException 再起可能例外が発生した場合
	 * @throws MoveActionFatalException 致命的例外が発生した場合
	 */
	public void errorAction(AbstractCrawler crawler) throws MoveActionException, MoveActionFatalException {
		QueueTask.logger.info(new Loggable(){
			@Override
			public String printLog(String lineSeparator) {
				return "URL=[" + movableElement.getUrl() + "]に移動できませんでした。";
			}});
		for(MoveAction moveAction : moveActionList) moveAction.errorAction(this.movableElement, crawler);
	}
	
}
