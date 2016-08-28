package jp.co.dk.crawler.controler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.Crawler;
import jp.co.dk.crawler.db.AbstractCrawler;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.scenario.MoveScenario;
import jp.co.dk.crawler.scenario.RegExpMoveScenario;
import jp.co.dk.crawler.scenario.action.MoveAction;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public abstract class AbstractCrawlerScenarioControler extends AbtractCrawlerControler {

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
		for (String scenarioStr : scenarioStrList) scenarioList.add(this.createScenarios(scenarioStr));
		
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
				System.exit(1);
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
				System.exit(255);
			}
		}
		// 正常終了
		System.exit(0);
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

	// ====================================================================================================
	
	
	private static Pattern actionPattern  = Pattern.compile("^(.+?)\\((.*)\\)$");
	
	public MoveScenario createScenarios(String command) {
		String[] commandList = command.split("->");
		MoveScenario beforeScenario = null;
		for (int i=commandList.length-1; i>=0; i--) {
			MoveScenario scenario = createScenario(commandList[i]);
			if (beforeScenario != null) {
				scenario.setChildScenario(beforeScenario);
			}
			beforeScenario = scenario;
		}
		return beforeScenario;
	}

}
