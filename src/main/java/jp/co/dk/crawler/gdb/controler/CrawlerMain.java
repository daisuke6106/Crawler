package jp.co.dk.crawler.gdb.controler;

import java.io.IOException;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageMovableLimitException;
import jp.co.dk.browzer.exception.PageRedirectException;
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

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class CrawlerMain extends AbtractCrawlerControler {

	@Override
	public void execute() { 
		
		try (Neo4JDataStoreManager dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty())) {
			dsm.startTrunsaction();
			GCrawler crawler = new GCrawler(cmd.getOptionValue("url"), dsm);
			if (cmd.hasOption("all")) {
				crawler.saveAll();
				crawler.saveAllUrl();
			} else {
 				crawler.save();
 				crawler.saveAllUrl();
			}
		} catch (CrawlerInitException | PageIllegalArgumentException | PageAccessException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (CrawlerSaveException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (DocumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (PageRedirectException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (PageMovableLimitException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (Neo4JDataStoreManagerException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (Neo4JDataStoreManagerCypherException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (PropertyException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	

	@Override
	protected void getOptions(Options options){
		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withArgName("保存対象のURL").withDescription("保存対象のURL").create("url"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withDescription("そのURLに関するデータをすべて保存するか").create("all"));
	}
	
	public static void main(String[] args) {
		new CrawlerMain().execute(args);
	}
	
}
