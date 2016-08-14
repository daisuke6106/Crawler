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

		// シナリオオブジェクトを生成
		List<MoveScenario> scenarioList = new ArrayList<>();
		for (String scenarioStr : scenarioStrList) scenarioList.add(this.createScenarios(scenarioStr));
		
		// インターバル
		String intervalStr = cmd.getOptionValue("i");
		if (intervalStr != null && !intervalStr.equals("")) this.interval = Long.parseLong(intervalStr);
		
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
	
}
