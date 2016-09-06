package jp.co.dk.crawler.controler;

import static jp.co.dk.crawler.message.CrawlerMessage.FAILE_TO_SCENARIO_GENERATION;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.Crawler;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.scenario.MoveScenario;
import jp.co.dk.crawler.scenario.action.MoveAction;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

/**
 * AbstractCrawlerScenarioControlerは、遷移制御にシナリオを使用するコントローラが実装するべき基底クラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class AbstractCrawlerScenarioControler extends AbtractCrawlerControler {

	/** 遷移先、遷移先でのイベント定義 */
	protected Pattern scenarioPattern = Pattern.compile("^(.+?)@(.+?)$");
	
	/** 走査シナリオ */
	protected String[] scenarioStrList;
	
	/** インターバル（単位：秒） */
	protected long interval = 1;
	
	@Override
	protected String getCommandName() {
		return "crawler";
	}

	@Override
	@SuppressWarnings("all")
	protected void getOptions(Options options) {
		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withArgName("URL").withDescription("URL").withLongOpt("url").create("u"));
		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withArgName("URL").withDescription("走査シナリオ").withLongOpt("scenario").create("s"));
		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withArgName("minuts").withDescription("インターバル（単位：秒）").withLongOpt("interval").create("i"));
		
	}

	@Override
	public void execute() {
		// 開始URL
		String url = cmd.getOptionValue("u");
		// クローラを生成する。
		try {
			this.crawler = this.createBrowzer(url);
		} catch (CrawlerInitException | PageIllegalArgumentException | PageAccessException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		// 走査シナリオ
		this.scenarioStrList = cmd.getOptionValues("s");

		// シナリオオブジェクトを生成
		List<MoveScenario> scenarioList = new ArrayList<>();
		try {
			for (String scenarioStr : scenarioStrList) scenarioList.add(this.createScenarios(scenarioStr));
		} catch (MoveActionFatalException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
		// インターバル
		String intervalStr = cmd.getOptionValue("i");
		if (intervalStr != null && !intervalStr.equals("")) this.interval = Long.parseLong(intervalStr);
		
		for (MoveScenario moveScenario : scenarioList) {
			// クローリング開始
			try {
				// クローリングを開始する。
				moveScenario.start(this.crawler, this.interval);
			} catch (MoveActionException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				System.exit(1);
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
				System.exit(255);
			}
		}
		// 正常終了
		System.exit(0);
	}
	
	/**
	 * <p>単一のシナリオ生成を実施する。</p>
	 * シナリオを定義する文字列を基に単一のシナリオクラスを生成する。
	 * 
	 * @param command シナリオ文字列
	 * @return シナリオオブジェクト
	 * @throws MoveActionFatalException シナリオの生成に失敗した場合
	 */
	public MoveScenario createScenarios(String command) throws MoveActionFatalException {
		String[] commandList = command.split("->");
		MoveScenario beforeScenario = null;
		for (int i=commandList.length-1; i>=0; i--) {
			MoveScenario scenario = this.createScenario(commandList[i]);
			if (beforeScenario != null) scenario.setChildScenario(beforeScenario);
			beforeScenario = scenario;
		}
		return beforeScenario;
	}

	/**
	 * シナリオインスタンスを生成し、返却します。
	 * 
	 * @param scenarioStr シナリオを表す文字列
	 * @return シナリオインスタンス
	 * @throws MoveActionFatalException シナリオの生成に失敗した場合
	 */
	protected MoveScenario createScenario(String scenarioStr) throws MoveActionFatalException {
		Matcher scenarioMatcher = this.scenarioPattern.matcher(scenarioStr);
		if (scenarioMatcher.find()) {
			String scenario = scenarioMatcher.group(1);
			List<Object> scenarioInstanceList = new ClassGenerater(scenario).createObjectList();
			if (scenarioInstanceList.size() != 1) throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"シナリオが複数設定されています。", scenarioStr});
			Object scenarioClass = scenarioInstanceList.get(0);
			if (!(scenarioClass instanceof MoveScenario)) throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"シナリオクラスではありません。", scenarioStr});
			MoveScenario moveScenario = (MoveScenario)scenarioClass;
			
			String actionStr = scenarioMatcher.group(2);
			if (actionStr == null || actionStr.equals("")) throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"アクションが定義されていません。", scenarioStr});
			List<Object> actionInstanceList = new ClassGenerater(actionStr).createObjectList();
			List<MoveAction> moveActionList = new ArrayList<>();
			for (Object actionClass : actionInstanceList) {
				if (!(actionClass instanceof MoveAction)) throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"アクションクラスではありません。", actionClass.getClass().toString()});
				moveActionList.add((MoveAction)actionClass);
			}
			moveScenario.setMoveActionList(moveActionList);
			return moveScenario;
			
		} else {
			List<Object> instanceList = new ClassGenerater(scenarioStr).createObjectList();
			if (instanceList.size() != 1) throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"シナリオが複数設定されています。", scenarioStr});
			Object scenarioClass = instanceList.get(0);
			if (!(scenarioClass instanceof MoveScenario)) throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"シナリオクラスではありません。", scenarioStr});
			return (MoveScenario)scenarioClass;
		}
	}
	
	/**
	 * 指定のＵＲＬを基にクローラクラスのインスタンスを生成する。
	 * 
	 * @param url 接続先URL
	 * @throws CrawlerInitException クローラの初期化処理に失敗した場合
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	protected abstract Crawler createBrowzer(String url) throws CrawlerInitException, PageIllegalArgumentException, PageAccessException;

}
