package jp.co.dk.crawler.db.gdb.controler;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.controler.AbstractCrawlerScenarioControler;
import jp.co.dk.crawler.db.AbstractCrawler;
import jp.co.dk.crawler.db.gdb.GCrawler;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerException;
import jp.co.dk.neo4jdatastoremanager.property.Neo4JDataStoreManagerProperty;
import jp.co.dk.property.exception.PropertyException;

public class GCrawlerMain extends AbstractCrawlerScenarioControler {

	protected Neo4JDataStoreManager dsm;
	
	{
		try {
			this.dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty());
		} catch (Neo4JDataStoreManagerException | PropertyException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		new GCrawlerMain().execute(args);
	}
	
	@Override
	public AbstractCrawler createBrowzer(String url) throws CrawlerInitException, PageIllegalArgumentException, PageAccessException {
		return new GCrawler(url, this.dsm);
	}

	@Override
	protected String getCommandName() {
		return "gcrawler";
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

