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

			// 走査開始URLを保存するか？
			boolean saveStartUrl = cmd.hasOption("a");
			
			// 保存対象のURLパターン
			String saveUrlPatternStr = cmd.getOptionValue("s");
			Pattern saveUrlPattern = Pattern.compile(saveUrlPatternStr);

			// 移動対象のURLパターン
			String moveUrlPatternStr = cmd.getOptionValue("m");
			Pattern moveUrlPattern = Pattern.compile(moveUrlPatternStr);
			
			// 移動対象URLを保存するか？
			boolean saveMoveUrl = cmd.hasOption("b");
			
			// インターバル（単位：秒）
			long intervalTime = 10;
			String intervalStr = cmd.getOptionValue("i");
			if (intervalStr != null) intervalTime = Long.parseLong(intervalStr);
			
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
			// 現在のページを保存する。（オプションにより保存すると指定されていた場合）
			if (saveStartUrl) {
				this.crawler.save();
			}
			
			// このページに存在する移動対象のURLパターンに合致するURLリストを取得する。
			List<A> moveAnchorList = this.crawler.getAnchor(moveUrlPattern);
			
			
			for(A moveAnchor : moveAnchorList) {
				try {
					this.crawler.move(moveAnchor, new MoveAction(){
						
					});
				// 遷移前に遷移先のURLが「訪問済みURLの一覧」にあった場合、無視
				} catch (MoveActionException e) {}
			}
			
			System.exit(0);
		} catch (PatternSyntaxException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (PageIllegalArgumentException | PageRedirectException | PageMovableLimitException | PageAccessException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (DocumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (CrawlerSaveException e) {
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
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true ).withArgName("URL"   ).withDescription("走査のスタートページを保存するか？").withLongOpt("start_url_save").create("a"));
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true ).withArgName("URL"   ).withDescription("移動先のURLパターン（正規表現にて指定）").withLongOpt("move_pattern").create("m"));
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true ).withArgName("URL"   ).withDescription("移動先ページを保存するか？").withLongOpt("move_pattern").create("b"));
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true ).withArgName("URL"   ).withDescription("保存対象のURLパターン（正規表現にて指定）").withLongOpt("move_save").create("s"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(true ).withArgName("minuts").withDescription("インターバル（単位：秒）").withLongOpt("interval").create("i"));
	}
	
	public static void main(String[] args) {
		new CrawlerSaveMain().execute(args);
	}
	
}

class CrawlerSaveMoveAction implements MoveAction {
	
	/** 移動対象URLを保存するか？ */
	private boolean saveMoveUrl;
	
	/** 移動対象のURLパターン */
	private Pattern moveUrlPattern;
	
	/** 保存対象のURLパターン */
	private Pattern saveUrlPattern;
	
	/** 保存時のインターバル */
	private long intervalTime;
	
	/** 訪問済みURLの一覧 */
	private List<String> visitedUrlList = new ArrayList<>();
	
	/** 移動対象のURL一覧に合致するアンカー一覧 */
	private List<A> moveUrlList;
	
	CrawlerSaveMoveAction(boolean saveMoveUrl, Pattern moveUrlPattern, Pattern saveUrlPattern, long intervalTime) {
		this.saveMoveUrl    = saveMoveUrl;
		this.moveUrlPattern = moveUrlPattern;
		this.saveUrlPattern = saveUrlPattern;
		this.intervalTime   = intervalTime;
	}
	
	@Override
	public void before(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		// 遷移前に遷移先のURLが「訪問済みURLの一覧」にあった場合
		// 以降の処理は行わせないよう例外を送出
		if (visitedUrlList.contains(movable.getUrl())) throw new MoveActionException(null);
	}
	
	@Override
	public void after(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		try {
			this.moveUrlList = browzer.getAnchor(this.moveUrlPattern);
			// 移動対象URLを保存する場合、保存を実行する。
			if (this.saveMoveUrl) ((GCrawler)browzer).save();
			// このページにある保存対象のURLパターンに合致するURLを保存する。
			((GCrawler)browzer).saveBySavePattern(this.saveUrlPattern, this.intervalTime);
			// 訪問済みURLの一覧にURLを追加する。
			visitedUrlList.add(movable.getUrl());
		} catch (PageIllegalArgumentException | PageAccessException | DocumentException	| CrawlerReadException | CrawlerSaveException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
}
