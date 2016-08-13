package jp.co.dk.crawler.controler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageMovableLimitException;
import jp.co.dk.browzer.exception.PageRedirectException;
import jp.co.dk.browzer.scenario.MoveScenario;
import jp.co.dk.browzer.scenario.action.MoveAction;
import jp.co.dk.crawler.AbstractCrawler;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.scenario.RegExpAllMoveScenario;
import jp.co.dk.crawler.scenario.RegExpMoveScenario;
import jp.co.dk.document.exception.DocumentException;

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
		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withArgName("URL").withDescription("走査シナリオ").withLongOpt("scenario").create("s"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(true).withArgName("minuts").withDescription("インターバル（単位：秒）").withLongOpt("interval").create("i"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withDescription("すべて").withLongOpt("all").create("a"));
	}

	@Override
	public void execute() {
		// 走査シナリオ
		this.scenarioStrList = cmd.getOptionValues("s");
		
		// インターバル
		String intervalStr = cmd.getOptionValue("i");
		if (intervalStr != null && !intervalStr.equals("")) this.interval = Long.parseLong(intervalStr);
		
		// シナリオオブジェクトを生成
		List<MoveScenario> scenarioList = new ArrayList<>();
		for (String scenarioStr : scenarioStrList) scenarioList.add(this.createScenarios(scenarioStr));
		
		for (MoveScenario moveScenario : scenarioList) {
			// クローラを生成する。
			try {
				this.crawler = this.createBrowzer(((RegExpMoveScenario)moveScenario).getUrlPattern());
			} catch (CrawlerInitException | PageIllegalArgumentException | PageAccessException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
			
			// クローリング開始
			try {
				this.crawler.start(moveScenario.getChildScenario(), this.interval);
			} catch (MoveActionException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			} catch (PageIllegalArgumentException | PageRedirectException | PageMovableLimitException | PageAccessException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			} catch (DocumentException e) {
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
	
	private Pattern commandPattern = Pattern.compile("^(.+)@(.+)$");
	
	private Pattern actionPattern  = Pattern.compile("^(.+)\\((.*)\\)$");
	
	public RegExpMoveScenario createScenarios(String command) {
		String[] commandList = command.split("->");
		RegExpMoveScenario beforeScenario = null;
		for (int i=commandList.length-1; i>=0; i--) {
			RegExpMoveScenario scenario = createScenario(commandList[i]);
			if (beforeScenario != null) {
				scenario.setChildScenario(beforeScenario);
			}
			beforeScenario = scenario;
		}
		return beforeScenario;
	}

	protected RegExpMoveScenario createScenario(String command) throws MoveActionFatalException {
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
					if ("none".equals(commandStr)) {
						moveActionList.add(createNoneMoveAction(argiments));
					} else if ("print".equals(commandStr)) {
						moveActionList.add(createPrintMoveAction(argiments));
					} else if ("save".equals(commandStr)) {
						moveActionList.add(createSaveMoveAction(argiments));
					} else {
						moveActionList.add(createMoveActionByClassName(commandStr, argiments));
					}
				} else {
					if ("none".equals(action)) {
						moveActionList.add(createNoneMoveAction(new String[]{}));
					} else if ("print".equals(action)) {
						moveActionList.add(createPrintMoveAction(new String[]{}));
					} else if ("save".equals(action)) {
						moveActionList.add(createSaveMoveAction(new String[]{}));
					} else {
						moveActionList.add(createMoveActionByClassName(action, new String[]{}));
					}
				}
				
			}
			
			return new RegExpAllMoveScenario(urlPatternStr, urlPattern, moveActionList);
		} else {
			throw new MoveActionFatalException(null);
		}
	}
	
	public abstract AbstractCrawler createBrowzer(String url) throws CrawlerInitException, PageIllegalArgumentException, PageAccessException;

	protected abstract MoveAction createNoneMoveAction(String[] arguments);

	protected abstract MoveAction createPrintMoveAction(String[] arguments);
	
	protected abstract MoveAction createSaveMoveAction(String[] arguments);

	@SuppressWarnings("all")
	protected MoveAction createMoveActionByClassName(String className, String[] arguments) {
		MoveAction moveAction;
		try {
			Class<MoveAction> actionClass = (Class<MoveAction>) Class.forName(className);
			moveAction = actionClass.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			throw new MoveActionFatalException(null);
		}
		return moveAction;
	}
	
}
