package jp.co.dk.crawler.gdb.controler;

import java.io.IOException;
import java.util.List;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.crawler.controler.AbtractCrawlerControler;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerReadException;
import jp.co.dk.crawler.gdb.GPage;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.exception.DocumentFatalException;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerException;
import jp.co.dk.neo4jdatastoremanager.property.Neo4JDataStoreManagerProperty;
import jp.co.dk.property.exception.PropertyException;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class CrawlerReadMain extends AbtractCrawlerControler {

	@Override
	public void execute() { 
		
		try (Neo4JDataStoreManager dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty())) {
			dsm.startTrunsaction();
			List<GPage> pageList = GPage.read(cmd.getOptionValue("url"), dsm);
			for (GPage page : pageList) {
				System.out.println("URL:" + page.getUrl());
				System.out.println("REQUEST_HEADER:" + page.getRequestHeader());
				System.out.println("RESPONSE_HEADER:" + page.getResponseHeader());
				System.out.println("DATA:" + page.getData().getHash());
				System.out.println("SIZE:" + page.getData().length());
				System.out.println(page.getDocument().toString());
			}
		} catch (CrawlerInitException | Neo4JDataStoreManagerException | PropertyException | IOException | CrawlerReadException | DocumentFatalException | PageAccessException | DocumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		System.exit(0);
	}
	

	@Override
	protected void getOptions(Options options){
		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withArgName("読込対象のURL").withDescription("読込対象のURL").create("url"));
	}
	
	public static void main(String[] args) {
		new CrawlerReadMain().execute(args);
		System.exit(0);
	}
	
}
