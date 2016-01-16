package jp.co.dk.crawler.gdb.controler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
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
import jp.co.dk.property.exception.PropertyException;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class CrawlerSaveMain extends AbtractCrawlerControler {

	@Override
	public void execute() { 
		
		try (Neo4JDataStoreManager dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty())) {
			dsm.startTrunsaction();
			GCrawler crawler = new GCrawler(cmd.getOptionValue("url"), dsm);
			if (cmd.hasOption("all")) {
				while(true) {
					crawler.saveAll();
	 				crawler.saveAllUrl();
	 				
	 				GUrl url = (GUrl)crawler.getPage().getUrl();
	 				
	 				class TmpUrl {
	 					private int savedCount;
	 					private GUrl url;
	 					TmpUrl(GUrl url) throws CrawlerReadException {
	 						this.url        = url;
	 						this.savedCount = url.getSavedCount();
	 					}
	 				}
	 				
	 				List<TmpUrl> tmpUrlList = new ArrayList<TmpUrl>();
	 				List<GUrl> sameHostUrlList = url.getAllUrlBySameHost();
	 				for (GUrl sameHostUrl : sameHostUrlList) tmpUrlList.add(new TmpUrl(sameHostUrl));
	 				tmpUrlList.sort(new Comparator<TmpUrl>() {
						@Override
						public int compare(TmpUrl o1, TmpUrl o2) {
							return o1.savedCount - o2.savedCount;
						}
					});
	 				crawler = new GCrawler(tmpUrlList.get(0).url.toString(), dsm);
	 				Thread.sleep(1 * 1000);
				}
			} else {
 				crawler.saveAll();
 				crawler.saveAllUrl();
			}
			System.exit(0);
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
		} catch (CrawlerReadException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (InterruptedException e) {
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
		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withArgName("保存対象のURL").withDescription("保存対象のURL").create("url"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withDescription("そのURLに関するデータをすべて保存するか").create("all"));
	}
	
	public static void main(String[] args) {
		new CrawlerSaveMain().execute(args);
	}
	
}
