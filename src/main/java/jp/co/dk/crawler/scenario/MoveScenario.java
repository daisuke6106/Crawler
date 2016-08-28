package jp.co.dk.crawler.scenario;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

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
	
	private static Pattern commandPattern = Pattern.compile("^(.+?)@(.*?)$");
	
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
	
	public MoveScenario(String command) {
		this.moveActionList = this.createMoveActionList(command);
	}
	
	enum Phase {
		ClassPhase,
		ArgumentPhase,
		ArgumentParamPhase,
		ClosedPhase;
	}
	
	/**
	 * <p>MoveAction一覧を生成し、返却します。</p>
	 * 
	 * @param command
	 * @return
	 * @throws MoveActionFatalException
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
						moveActionList.add(createMoveActionByClassName(className, argumentList.toArray(new String[0])));
					}
					break;
				case ',':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\",\"の位置が不正です。");
					}  else if (nowPhase == Phase.ArgumentPhase) {
						argumentList.add(str.toString());
						str = new StringBuilder();
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\",\"の位置が不正です。");
					} else if (nowPhase == Phase.ClosedPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\",\"の位置が不正です。");
					}
					break;
				case '\'':
					if (nowPhase == Phase.ClassPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\'の位置が不正です。");
					} else if (nowPhase == Phase.ArgumentPhase) {
						nowPhase = Phase.ArgumentParamPhase;
					} else if (nowPhase == Phase.ArgumentParamPhase) {
						nowPhase = Phase.ArgumentPhase;
						str = new StringBuilder();
					} else if (nowPhase == Phase.ClosedPhase) {
						throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, "\'\"の位置が不正です。");
					}
					break;
				case '\\':
					isEscaped = true;
					break;
				default:
					str.append(commandChar);
			}
			
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
	
	protected MoveScenario createScenario(String command) throws MoveActionFatalException {
		Matcher matcher = this.commandPattern.matcher(command);
		if (matcher.find()) {
			String urlPatternStr = matcher.group(1);
			if (urlPatternStr == null || urlPatternStr.equals("")) throw new MoveActionFatalException(null);
			String actionStr     = matcher.group(2);
			if (actionStr     == null || actionStr.equals("")) throw new MoveActionFatalException(null);
			Pattern urlPattern;
			try {
				urlPattern = Pattern.compile(urlPatternStr);
			} catch (PatternSyntaxException e) {
				throw new MoveActionFatalException(null);
			}
			List<MoveAction> moveActionList = new ArrayList<>();
			String[] actionList = actionStr.split(";");
			for (String action : actionList) {
				Matcher actionMatcher = this.actionPattern.matcher(action);
				if (actionMatcher.find()) {
					String   commandStr   = actionMatcher.group(1);
					String   argumentsStr = actionMatcher.group(2);
					String[] argiments    = argumentsStr.split(",");
					moveActionList.add(createMoveActionByClassName(commandStr, argiments));
				} else {
					moveActionList.add(createMoveActionByClassName(action, new String[]{}));
				}
				
			}
			return new RegExpMoveScenario(urlPatternStr, urlPattern, moveActionList);
		} else {
			try {
				Pattern urlPattern = Pattern.compile(command);
				return new RegExpMoveScenario(command, urlPattern, new ArrayList<>());
			} catch (PatternSyntaxException e) {
				throw new MoveActionFatalException(null);
			}
			
		}
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
