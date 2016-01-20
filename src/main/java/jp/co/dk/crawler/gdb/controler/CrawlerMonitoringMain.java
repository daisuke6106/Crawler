package jp.co.dk.crawler.gdb.controler;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageMovableLimitException;
import jp.co.dk.browzer.exception.PageRedirectException;
import jp.co.dk.crawler.controler.AbtractCrawlerControler;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerReadException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.gdb.GCrawler;
import jp.co.dk.crawler.gdb.GUrl;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerCypherException;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerException;
import jp.co.dk.neo4jdatastoremanager.property.Neo4JDataStoreManagerProperty;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class CrawlerMonitoringMain extends AbtractCrawlerControler {

	@Override
	public void execute() { 
		
		try (Neo4JDataStoreManager dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty())) {
			dsm.startTrunsaction();
			boolean    isAllOption      = cmd.hasOption("all");
			boolean    isPersistent     = cmd.hasOption("p");
			long       intervalTime     = 0;
			if (cmd.hasOption("i")) {
				String intervalStr = cmd.getOptionValue("i");
				if (intervalStr == null || intervalStr.equals("")) {
					intervalTime = 10;
				} else {
					intervalTime = Long.parseLong(cmd.getOptionValue("i"));
				}
			} else {
				intervalTime = 10;
			}
			do {
				List<GUrl> wellKnownUrlList = GUrl.wellKnownUrlList(cmd.getOptionValue("url"), dsm);
				for (GUrl url : wellKnownUrlList) {
					System.out.println("[" + new Date() + "]:" + url.getURL());
					GCrawler crawler = new GCrawler(url.getURL(), dsm);
					if (isAllOption) {
						crawler.saveAll();
						crawler.saveAllUrl();
					} else {
						crawler.save();
						crawler.saveAllUrl();
					}
					Thread.sleep(intervalTime * 1000);
				}
				dsm.commit();
			} while (isPersistent);
			System.exit(0);
		} catch (CrawlerInitException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (Neo4JDataStoreManagerException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (CrawlerReadException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (PageIllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (PageAccessException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (DocumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (CrawlerSaveException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (Neo4JDataStoreManagerCypherException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (PageRedirectException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (PageMovableLimitException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (RuntimeException e) {
			System.out.println(e.getMessage());
			System.exit(255);
		}
	}
	
	@Override
	protected String getCommandName() {
		return "crawler_save";
	}
	
	@Override
	protected void getOptions(Options options){
		options.addOption(
			OptionBuilder
				.isRequired(true)
				.hasArg(true)
				.withArgName("URL")
				.withDescription("保存対象のURL")
				.withLongOpt("url")
				.create("u")
		);
		options.addOption(
			OptionBuilder
				.isRequired(false)
				.hasArg(false)
				.withDescription("そのURLに関するデータをすべて保存するか")
				.withLongOpt("all")
				.create("a")
		);
		options.addOption(
			OptionBuilder
				.isRequired(false)
				.hasArg(false)
				.withDescription("永続的に監視するか")
				.withLongOpt("permanence")
				.create("p")
		);
		options.addOption(
			OptionBuilder
				.isRequired(false)
				.hasArg(false)
				.withArgName("minuts")
				.withDescription("永続的に監視する際のインターバル（単位：秒）")
				.withLongOpt("interval")
				.create("i"));
	}
	
	public static void main(String[] args) {
		new CrawlerMonitoringMain().execute(args);
	}
	
}
