package jp.co.dk.crawler.scenario;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.Crawler;
import jp.co.dk.crawler.CrawlerPage;
import jp.co.dk.crawler.scenario.action.MoveAction;
import jp.co.dk.logger.Logger;
import jp.co.dk.logger.LoggerFactory;

public abstract class MoveScenario {
	
	/** ロガーインスタンス */
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/** 親シナリオ */
	protected MoveScenario parentScenario;
	
	/** 子シナリオ */
	protected MoveScenario childScenario;
	
	/** 移動時に実行するアクション */
	protected List<MoveAction> moveActionList;
	
	/** 実行待ちキュー */
	protected Queue<QueueTask> moveableQueue = new ArrayDeque<>();
	
	public MoveScenario(List<MoveAction> moveActionList) {
		this.moveActionList = moveActionList;
	}
	
	public boolean hasParentScenario() {
		return parentScenario != null;
	}
	
	public MoveScenario getParentScenario() {
		return this.parentScenario;
	}
	
	public void setParentScenario(MoveScenario scenario) {
		this.parentScenario = scenario;
	}

	
	public boolean hasChildScenario() {
		return childScenario != null;
	}
	
	public MoveScenario getChildScenario() {
		return this.childScenario;
	}
	
	public void setChildScenario(MoveScenario scenario) {
		this.childScenario = scenario;
	}

	public MoveScenario getTopScenario() {
		MoveScenario topScenario = this;
		while (topScenario.hasParentScenario()) topScenario = topScenario.getParentScenario();
		return topScenario;
	}
	
	/**
	 * 次のタスクをポップする。
	 * @return タスク
	 */
	public QueueTask popTask() {
		return this.moveableQueue.poll();
	}
	
	/**
	 * 未実行のタスクが存在するか判定します。
	 * @return 判定結果(true=キューの件数が0ではない、false=キューの件数が0)
	 */
	public boolean hasTask() {
		return (this.moveableQueue.size() != 0);
	}
	
	/*
	public void addTaskAllScenario(Page page) throws MoveActionException, MoveActionFatalException {
		MoveScenario moveScenario = this.getTopScenario();
		moveScenario.addTask(page);
		while(moveScenario.hasChildScenario()) {
			moveScenario = moveScenario.getChildScenario();
			moveScenario.addTask(page);
		}
	}
	*/
	
	public abstract void start(Crawler crawler, long interval) throws MoveActionException, MoveActionFatalException ;
	
	protected abstract void crawl(Crawler crawler, long interval) throws MoveActionException, MoveActionFatalException ;
	
	/**
	 * <p>ページから指定のタスクを追加する。</p>
	 * 引数に指定されたページから指定の遷移要素を取得し、本シナリオ内に保持する。
	 * 
	 * @param page 抽出対象のページ情報
	 * @throws MoveActionException
	 * @throws MoveActionFatalException
	 */
	public void addTask(CrawlerPage page) throws MoveActionException, MoveActionFatalException {
		List<jp.co.dk.browzer.html.element.A> anchorList = (List<jp.co.dk.browzer.html.element.A>)this.getMoveableElement(page);
		for (jp.co.dk.browzer.html.element.A anchor : anchorList) this.moveableQueue.add(this.createTask(anchor, this.moveActionList));
	}
	
	protected abstract List<jp.co.dk.browzer.html.element.A> getMoveableElement(CrawlerPage page) throws MoveActionException, MoveActionFatalException ;
	
	/**
	 * タスクを作成する。
	 * 
	 * @param movableElement 繊維先要素
	 * @param moveActionList 繊維先で実行するアクション一覧
	 * @return タスクオブジェクト
	 */
	protected QueueTask createTask(MovableElement movableElement, List<MoveAction> moveActionList) {
		return new QueueTask(movableElement, moveActionList);
	}
	
	@Override
	public abstract String toString();
}
