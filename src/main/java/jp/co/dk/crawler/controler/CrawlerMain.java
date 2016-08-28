package jp.co.dk.crawler.controler;

import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.Crawler;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.scenario.MoveScenario;
import jp.co.dk.crawler.scenario.RegExpMoveScenario;

public class CrawlerMain extends AbstractCrawlerScenarioControler {
	
	public static void main(String[] args) {
		new CrawlerMain().execute(args);
	}

	@Override
	protected MoveScenario createScenario(String scenarioStr) throws MoveActionFatalException {
		return new RegExpMoveScenario(scenarioStr);
	}
	
	@Override
	public Crawler createBrowzer(String url) throws CrawlerInitException, PageIllegalArgumentException, PageAccessException {
		return new Crawler(url);
	}

}

