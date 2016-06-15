package jp.co.dk.crawler.gdb.controler;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageMovableLimitException;
import jp.co.dk.browzer.exception.PageRedirectException;
import jp.co.dk.browzer.html.element.A;
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

public class CrawlerSaveMain extends AbtractCrawlerControler {

	@Override
	public void execute() { 
		
		try (Neo4JDataStoreManager dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty())) {
			// トランザクションを開始
			dsm.startTrunsaction();
			
			// 走査開始URL
			String startUrl      = cmd.getOptionValue("s");
			// 走査対象のURLパターン
			String urlPatternStr = cmd.getOptionValue("p");
			Pattern urlPattern;
			try {
				urlPattern = Pattern.compile(urlPatternStr);
			} catch (PatternSyntaxException e) { 
				System.out.println(e.getMessage());
				System.exit(1);
			}
			
			// インターバル（単位：秒）
			long intervalTime;
			String intervalStr = cmd.getOptionValue("i");
			if (intervalStr != null) {
				intervalTime = Long.parseLong(intervalStr);
			} else {
				intervalTime = 10;
			}
			
			// クローラを生成する。
			GCrawler crawler = new GCrawler(startUrl, dsm);
			// 現在のページを保存する。
			crawler.save();
			
			// 指定のパターンに合致するURLを取得する。
			List<A> anchorList = crawler.getAnchor(urlPattern);
			
			do {
				
				
				
				
				
				
				
				
				
				
				List<GUrl> wellKnownUrlList = GUrl.wellKnownUrlList(cmd.getOptionValue("u"), dsm);
				for (GUrl url : wellKnownUrlList) {
					System.out.println("[" + new Date() + "]:" + url.getURL());
					
					try {
						GCrawler crawler = new GCrawler(url.getURL(), dsm);
						if (isAllOption) {
							crawler.saveAll();
							crawler.saveAllUrl();
						} else {
							crawler.save();
							crawler.saveAllUrl();
						}
						Thread.sleep(intervalTime * 1000);
					} catch (PageAccessException e) {
						System.out.println("[" + new Date() + "]:" + e.toString());
					} 
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
			e.printStackTrace();
			System.exit(255);
		}
	}
	
	private void save(GCrawler crawler, A anchor, Pattern p) throws PageIllegalArgumentException, PageAccessException, PageRedirectException, PageMovableLimitException, CrawlerSaveException, DocumentException {
		crawler.move(anchor);
		crawler.save();
		List<A> anchorList = crawler.getAnchor(p);
		for (A nextanchor : anchorList) {
			save(crawler, nextanchor, p);
		}
	}
	
	@Override
	protected String getCommandName() {
		return "crawler_save";
	}
	
	@Override
	protected void getOptions(Options options){
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true ).withArgName("URL"   ).withDescription("走査のスタートページ").withLongOpt("start_url").create("s"));
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true ).withArgName("URL"   ).withDescription("走査対象のURLパターン（正規表現にて指定）").withLongOpt("crawle_pattern").create("p"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(true ).withArgName("minuts").withDescription("インターバル（単位：秒）").withLongOpt("interval").create("i"));
	}
	
	public static void main(String[] args) {
		new CrawlerSaveMain().execute(args);
	}
	
}
