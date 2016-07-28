package jp.co.dk.crawler.gdb.controler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageMovableLimitException;
import jp.co.dk.browzer.exception.PageRedirectException;
import jp.co.dk.browzer.html.element.A;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.browzer.scenario.RegExpMoveScenario;
import jp.co.dk.browzer.scenario.action.MoveAction;
import jp.co.dk.crawler.controler.AbtractCrawlerControler;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerReadException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.gdb.GCrawler;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
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
			String startUrl      = cmd.getOptionValue("u");

			// 走査シナリオ
			String scenario = cmd.getOptionValue("s");
			
			// インターバル（単位：秒）
			long intervalTime = 1;
			String intervalStr = cmd.getOptionValue("i");
			if (intervalStr != null) intervalTime = Long.parseLong(intervalStr);
			
			// シナリオオブジェクトを生成
			RegExpMoveScenario regExpMoveScenario = RegExpMoveScenario.create(scenario);
			
			// クローラを生成する。
			try {
				this.crawler = new GCrawler(startUrl, dsm);
			} catch (CrawlerInitException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}catch (PageIllegalArgumentException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			} catch (PageAccessException e) {
				System.out.println(e.getMessage());
				System.exit(1);
			}
			
			// クローリング開始
			this.crawler.start(regExpMoveScenario, intervalTime);
			
			// 正常終了
			System.exit(0);
		} catch (MoveActionException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (PageIllegalArgumentException | PageRedirectException | PageMovableLimitException | PageAccessException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (DocumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (IOException | Neo4JDataStoreManagerException | RuntimeException e) {
			e.printStackTrace();
			System.exit(255);
		}
	}
	
	@Override
	protected String getCommandName() {
		return "crawler_save";
	}
	
	@Override
	@SuppressWarnings("all")
	protected void getOptions(Options options){
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true ).withArgName("URL"   ).withDescription("走査のスタートページ").withLongOpt("start_url").create("u"));
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true ).withArgName("URL"   ).withDescription("走査シナリオ").withLongOpt("scenario").create("s"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(true ).withArgName("minuts").withDescription("インターバル（単位：秒）").withLongOpt("interval").create("i"));
	}
	
	public static void main(String[] args) {
		new CrawlerSaveMain().execute(args);
	}
	
}

