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

public abstract class MoveScenario {
	
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
	
	private enum Phase {
		ClassPhase,
		ArgumentPhase,
		ArgumentParamPhase,
		ClosedPhase;
	}
	
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
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\" \"(スペース)の位置が不正です。");
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
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\"(\"の位置が不正です。");
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\"(\"の位置が不正です。");
					} else if (nowPhase == Phase.ClosedPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\"(\"の位置が不正です。");
					}
					break;
				case '\'':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\'の位置が不正です。");
					} else if (nowPhase == Phase.ArgumentPhase) {
						nowPhase = Phase.ArgumentParamPhase;
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						nowPhase = Phase.ArgumentPhase;
						argumentList.add(str.toString());
						str = new StringBuilder();
					} else if (nowPhase == Phase.ClosedPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\'\"の位置が不正です。");
					}
					break;
				case ',':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\",\"の位置が不正です。");
					}  else if (nowPhase == Phase.ArgumentPhase) {
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\",\"の位置が不正です。");
					} else if (nowPhase == Phase.ClosedPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\",\"の位置が不正です。");
					}
					break;
				case ')':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\")\"の位置が不正です。");
					}  else if (nowPhase == Phase.ArgumentPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\")\"の位置が不正です。");
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						nowPhase = Phase.ClosedPhase;
					} else if (nowPhase == Phase.ClosedPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\")\"の位置が不正です。");
					}
					
					break;
				case ';':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\";\"の位置が不正です。");
					}  else if (nowPhase == Phase.ArgumentPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\";\"の位置が不正です。");
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\";\"の位置が不正です。");
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
			throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "引数が完結していません。");
		} else if (nowPhase == Phase.ArgumentParamPhase) {
			throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "引数が完結していません。");
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
			throw new MoveActionFatalException(null);
		} catch (IllegalArgumentException e) {
			throw new MoveActionFatalException(null);
		} catch (InvocationTargetException e) {
			throw new MoveActionFatalException(null);
		} catch (NoSuchMethodException e) {
			throw new MoveActionFatalException(null);
		} catch (SecurityException e) {
			throw new MoveActionFatalException(null);
		}
		return moveAction;
	}
	
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
