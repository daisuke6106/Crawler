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
import static jp.co.dk.crawler.message.CrawlerMessage.*;

/**
 * <p>MoveScenarioは、遷移先や、遷移後に行う処理の定義を行うシナリオクラスです。</p>
 * シナリオは親子関係を持っており、ネストさせることができます。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class MoveScenario {
	
	/** シナリオを表す文字列 */
	protected String scenarioStr;
	
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
	
	/**
	 * 構文解析時に使用するフェーズ定数です。
	 * @version 1.0
	 * @author D.Kanno
	 */
	private enum Phase {
		ClassPhase,
		ArgumentPhase,
		ArgumentParamPhase,
		ClosedPhase;
	}
	
	/**
	 * <p>コンストラクタ</p>
	 * シナリオを表す文字列を基に、シナリオを表すインスタンスを生成します。
	 * 
	 * @param scenarioStr シナリオを表す文字列
	 */
	public MoveScenario(String scenarioStr) {
		this.scenarioStr = scenarioStr;
	}
	
	/**
	 * <p>MoveAction一覧を生成し、返却します。</p>
	 * クラス名([引数,])の形式で記述された文字列を元に、MoveActin一覧を生成し、返却します。<br/>
	 * 例<br/>
	 * 単体指定の場合<br/>
	 * jp.co.example.MoveActionXXX()<br/>
	 * jp.co.example.MoveActionXXX('aaa','bbb')<br/>
	 * 複数指定の場合<br/>
	 * jp.co.example.MoveActionXXX();jp.co.example.MoveActionYYY()<br/>
	 * jp.co.example.MoveActionXXX('aaa','bbb');jp.co.example.MoveActionYYY('aaa','bbb')<br/>
	 * @param command クラス名([引数,])の形式で記述された文字列
	 * @return MoveAction一覧
	 * @throws MoveActionFatalException 構文解析に失敗した場合
	 */
	protected List<MoveAction> createMoveActionList(String command) throws MoveActionFatalException {
		Deque<Character> formatQue = new LinkedList<>();
		for (char chara : command.toCharArray()) formatQue.offer(new Character(chara));
		List<MoveAction> moveActionList = new ArrayList<>();
		
		String className = "";
		List<String> argumentList = new ArrayList<>();
		StringBuilder str = new StringBuilder();
		
		Phase nowPhase = Phase.ClassPhase;
		boolean isEscaped = false;
		
		while (formatQue.size() != 0) {
			char commandChar = formatQue.poll().charValue();
			if (isEscaped) {
				str.append(commandChar);
				isEscaped = false;
				continue;
			}
			switch (commandChar) {
				case ' ':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\" \"(スペース)の位置が不正です。", command});
					} else if (nowPhase == Phase.ArgumentPhase) {
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						str.append(commandChar);
					} else if (nowPhase == Phase.ClosedPhase) {
					}
					break;	
				case '(':
					if (nowPhase == Phase.ClassPhase) {
						nowPhase = Phase.ArgumentPhase;
						className = str.toString();
						str = new StringBuilder();
					} else if (nowPhase == Phase.ArgumentPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\"(\"の位置が不正です。", command});
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\"(\"の位置が不正です。", command});
					} else if (nowPhase == Phase.ClosedPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\"(\"の位置が不正です。", command});
					}
					break;
				case '\'':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\'の位置が不正です。", command});
					} else if (nowPhase == Phase.ArgumentPhase) {
						nowPhase = Phase.ArgumentParamPhase;
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						nowPhase = Phase.ArgumentPhase;
						argumentList.add(str.toString());
						str = new StringBuilder();
					} else if (nowPhase == Phase.ClosedPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\'\"の位置が不正です。", command});
					}
					break;
				case ',':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\",\"の位置が不正です。", command});
					}  else if (nowPhase == Phase.ArgumentPhase) {
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\",\"の位置が不正です。", command});
					} else if (nowPhase == Phase.ClosedPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\",\"の位置が不正です。", command});
					}
					break;
				case ')':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\")\"の位置が不正です。", command});
					}  else if (nowPhase == Phase.ArgumentPhase) {
						nowPhase = Phase.ClosedPhase;
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\")\"の位置が不正です。", command});
					} else if (nowPhase == Phase.ClosedPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\")\"の位置が不正です。", command});
					}
					
					break;
				case ';':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\";\"の位置が不正です。", command});
					}  else if (nowPhase == Phase.ArgumentPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\";\"の位置が不正です。", command});
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"\";\"の位置が不正です。", command});
					} else if (nowPhase == Phase.ClosedPhase) {
						nowPhase = Phase.ClassPhase;
						moveActionList.add(createMoveActionByClassName(className, argumentList.toArray(new String[0])));
					}
					break;
				case '\\':
					isEscaped = true;
					break;
				default:
					str.append(commandChar);
			}
		}
		if (nowPhase == Phase.ClassPhase) {
			moveActionList.add(createMoveActionByClassName(className, new String[]{}));
		}  else if (nowPhase == Phase.ArgumentPhase) {
			throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"引数が完結していません。", command});
		} else if (nowPhase == Phase.ArgumentParamPhase) {
			throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"引数が完結していません。", command});
		} else if (nowPhase == Phase.ClosedPhase) {
			nowPhase = Phase.ClassPhase;
			moveActionList.add(createMoveActionByClassName(className, argumentList.toArray(new String[0])));
		}
		return moveActionList;
	}
	
	/**
	 * <p>MoveActionクラス生成</p>
	 * 引数に指定されたクラス名と、そのクラスのコンストラクタに引き渡す引数を基にMoveActionクラスを生成し、返却します。
	 * 
	 * @param className クラス名
	 * @param arguments コンストラクタに引き渡す引数
	 * @return MoveActionインスタンス
	 */
	@SuppressWarnings("all")
	protected MoveAction createMoveActionByClassName(String className, String[] arguments) {
		MoveAction moveAction;
		try {
			Class<MoveAction> actionClass = (Class<MoveAction>) Class.forName(className);
			Constructor<MoveAction> moveActionConstructor = actionClass.getDeclaredConstructor(new Class[]{String[].class});
			moveAction = moveActionConstructor.newInstance(new Object[]{arguments});
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{e.getMessage(), className}, e);
		} catch (IllegalArgumentException e) {
			throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{e.getMessage(), className}, e);
		} catch (InvocationTargetException e) {
			throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{e.getMessage(), className}, e);
		} catch (NoSuchMethodException e) {
			throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{e.getMessage(), className}, e);
		} catch (SecurityException e) {
			throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{e.getMessage(), className}, e);
		}
		return moveAction;
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
		return this.scenarioStr;
	}
}
