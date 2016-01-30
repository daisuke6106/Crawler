package jp.co.dk.crawler.gdb.controler;

import java.io.IOException;

import jp.co.dk.crawler.controler.AbtractCrawlerControler;
import jp.co.dk.crawler.gdb.GUrl;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerCypherException;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerException;
import jp.co.dk.neo4jdatastoremanager.property.Neo4JDataStoreManagerProperty;

import org.apache.commons.cli.Options;

public class CrawlerPreparationMain extends AbtractCrawlerControler {

	@Override
	public void execute() { 
		
		try (Neo4JDataStoreManager dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty())) {
			dsm.startTrunsaction();
			GUrl.createIndex(dsm);
			System.exit(0);
		} catch (Neo4JDataStoreManagerException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (Neo4JDataStoreManagerCypherException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (RuntimeException e) {
			e.printStackTrace();
			System.exit(255);
		}
	}
	
	@Override
	protected String getCommandName() {
		return "crawler_save";
	}
	
	@Override
	protected void getOptions(Options options){}
	
	public static void main(String[] args) {
		new CrawlerPreparationMain().execute(args);
	}
	
}
