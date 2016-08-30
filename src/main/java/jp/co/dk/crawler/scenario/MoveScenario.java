package jp.co.dk.crawler.scenario;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
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

/**
 * <p>MoveScenarioは、遷移先や、遷移後に行う処理の定義を行うシナリオクラスです。</p>
 * シナリオは親子関係を持っており、ネストさせることができます。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class MoveScenario {
	
	/** シナリオ引数 */
	protected String[] argumentList;
	
	/** ロガーインスタンス */
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/** 親シナリオ */
	protected MoveScenario parentScenario;
	
	/** 子シナリオ */
	protected MoveScenario childScenario;
	
	/** 移動時に実行するアクション */
	protected List<MoveAction> moveActionList = new ArrayList<>();
	
	/** 実行待ちキュー */
	protected Queue<QueueTask> moveableQueue = new ArrayDeque<>();
	
	/**
	 * <p>コンストラクタ</p>
	 * シナリオを表す文字列を基に、シナリオを表すインスタンスを生成します。
	 * 
	 * @param argumentList シナリオ引数
	 */
	public MoveScenario(String[] argumentList) {
		this.argumentList = argumentList;
	}
	
	/**
	 * 親シナリオが存在しているか判定します。
	 * @return 判定結果(true=親シナリオが存在する、false=親シナリオが存在しない)
	 */
	public boolean hasParentScenario() {
		return parentScenario != null;
	}
	
	/**
	 * 親シナリオを取得します。
	 * @return 親シナリオ
	 */
	public MoveScenario getParentScenario() {
		return this.parentScenario;
	}
	
	/**
	 * 親シナリオを設定します。
	 * @param scenario 親シナリオ
	 */
	public void setParentScenario(MoveScenario scenario) {
		this.parentScenario = scenario;
	}

	/**
	 * 子シナリオが存在しているか判定します。
	 * @return 判定結果(true=子シナリオが存在する、false=子シナリオが存在しない)
	 */
	public boolean hasChildScenario() {
		return childScenario != null;
	}
	
	/**
	 * 子シナリオを取得します。
	 * @return 子シナリオ
	 */
	public MoveScenario getChildScenario() {
		return this.childScenario;
	}
	
	/**
	 * 子シナリオを設定します。
	 * @param scenario 子シナリオ
	 */
	public void setChildScenario(MoveScenario scenario) {
		this.childScenario = scenario;
	}

	/**
	 * 最上位シナリオを取得します。
	 * @return 最上位シナリオ
	 */
	public MoveScenario getTopScenario() {
		MoveScenario topScenario = this;
		while (topScenario.hasParentScenario()) topScenario = topScenario.getParentScenario();
		return topScenario;
	}
	
	public void setMoveActionList(List<MoveAction> moveActionList) {
		this.moveActionList = moveActionList;
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
	
	/**
	 * シナリオを開始します。
	 * @param crawler クローラインスタンス
	 * @param interval インターバル
	 * @throws MoveActionException 移動実施時に再起可能例外が発生した場合
	 * @throws MoveActionFatalException 移動実施時に致命的例外が発生した場合
	 */
	public abstract void start(Crawler crawler, long interval) throws MoveActionException, MoveActionFatalException ;
	
	/**
	 * シナリオを開始します。
	 * @param crawler クローラインスタンス
	 * @param interval インターバル
	 * @throws MoveActionException 移動実施時に再起可能例外が発生した場合
	 * @throws MoveActionFatalException 移動実施時に致命的例外が発生した場合
	 */
	protected abstract void crawl(Crawler crawler, long interval) throws MoveActionException, MoveActionFatalException ;
	
	/**
	 * <p>ページから指定のタスクを追加する。</p>
	 * 引数に指定されたページから指定の遷移要素を取得し、本シナリオ内に保持する。
	 * 
	 * @param page 抽出対象のページ情報
	 * @throws MoveActionException タスク追加時に再起可能例外が発生した場合
	 * @throws MoveActionFatalException タスク追加時に致命的例外が発生した場合
	 */
	public void addTask(CrawlerPage page) throws MoveActionException, MoveActionFatalException {
		List<jp.co.dk.browzer.html.element.A> anchorList = (List<jp.co.dk.browzer.html.element.A>)this.getMoveableElement(page);
		for (jp.co.dk.browzer.html.element.A anchor : anchorList) this.moveableQueue.add(this.createTask(anchor, this.moveActionList));
	}
	
	/**
	 * <p>遷移要素一覧を取得する。</p>
	 * 引数に指定されたページからこのシナリオに準ずる遷移要素一覧を取得する。
	 * 
	 * @param page 抽出対象のページ
	 * @return 遷移要素一覧
	 * @throws MoveActionException タスク追加時に再起可能例外が発生した場合
	 * @throws MoveActionFatalException タスク追加時に致命的例外が発生した場合
	 */
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
	public String toString() {
		return this.argumentList.toString();
	}
}
