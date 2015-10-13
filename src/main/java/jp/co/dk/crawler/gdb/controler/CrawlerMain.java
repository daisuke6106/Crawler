package jp.co.dk.crawler.gdb.controler;

import java.util.List;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageMovableLimitException;
import jp.co.dk.browzer.exception.PageRedirectException;
import jp.co.dk.crawler.AbstractCrawler;
import jp.co.dk.crawler.controler.AbtractCrawlerControler;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.gdb.GCrawler;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerCypherException;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerException;
import jp.co.dk.neo4jdatastoremanager.property.Neo4JDataStoreManagerProperty;
import jp.co.dk.property.exception.PropertyException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

public class CrawlerMain extends AbtractCrawlerControler {

	public static void main(String[] args) {
		new CrawlerMain().execute(args);
	}
	
	

	@Override
	protected AbstractCrawler createCrawler(String url) throws CrawlerInitException {
		Neo4JDataStoreManager dataStoreManager;
		try {
			dataStoreManager = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty());
		} catch (Neo4JDataStoreManagerException | PropertyException e) {
			throw new CrawlerInitException(DATASTOREMANAGER_CAN_NOT_CREATE);
		}
		return new GCrawler(url, dataStoreManager);
	}

	@Override
	protected void getOptions() {
		this.options.addOption(OptionBuilder.isRequired(true).hasArg(true).withArgName("保存対象のURL").withDescription("保存対象のURL").create("url"));
		this.options.addOption(OptionBuilder.isRequired(false).hasArg(false).withDescription("そのURLに関するデータをすべて保存するか").create("all"));
	}
}
