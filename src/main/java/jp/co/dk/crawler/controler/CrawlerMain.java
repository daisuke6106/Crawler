package jp.co.dk.crawler.controler;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.Crawler;
import jp.co.dk.crawler.exception.CrawlerInitException;

public class CrawlerMain extends AbstractCrawlerScenarioControler {
	
	public static void main(String[] args) {
		new CrawlerMain().execute(args);
	}

	@Override
	public Crawler createBrowzer(String url) throws CrawlerInitException, PageIllegalArgumentException, PageAccessException {
		return new Crawler(url);
	}

	@Override
	protected String getCommandName() {
		return "crawler";
	}

	@Override
	protected String getDescription() {
		StringBuilder description = new StringBuilder();
		description.append(this.getCommandName());
		description.append("は、開始URLのページからシナリオに指定された通りにページを巡回し、シナリオに紐付くアクションを行うコマンドです。").append(System.lineSeparator());
		description.append("オプションのURL(--url)に開始地点となるページのURL、オプションのシナリオ(--scenario)に巡回する際のシナリオとページを訪れた際に行うアクションを指定します。").append(System.lineSeparator());
		description.append("シナリオは「シナリオ@アクション」の形式で記述し、複数のシナリオはアロー(->)でつなげます。").append(System.lineSeparator());
		return description.toString();
	}

}

