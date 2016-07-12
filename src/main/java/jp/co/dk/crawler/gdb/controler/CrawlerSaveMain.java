package jp.co.dk.crawler.gdb.controler;

import java.io.IOException;
import java.util.ArrayList;
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
import jp.co.dk.crawler.gdb.html.element.GCrawlerA;
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
			Pattern saveUrlPattern = null;
			try {
				saveUrlPattern = Pattern.compile(saveUrlPatternStr);
			} catch (PatternSyntaxException e) { 
				System.out.println(e.getMessage());
				System.exit(1);
			}

			// 移動対象のURLパターン
			String moveUrlPatternStr = cmd.getOptionValue("m");
			Pattern moveUrlPattern = null;
			try {
				moveUrlPattern = Pattern.compile(moveUrlPatternStr);
			} catch (PatternSyntaxException e) { 
				System.out.println(e.getMessage());
				System.exit(1);
			}
			
			// 移動対象URLを保存するか？
			boolean saveMoveUrl = cmd.hasOption("b");
			
			// インターバル（単位：秒）
			long intervalTime;
			String intervalStr = cmd.getOptionValue("i");
			if (intervalStr != null) {
				intervalTime = Long.parseLong(intervalStr);
			} else {
				intervalTime = 10;
			}
			
			// クローラを生成する。
			try {
				this.crawler = new _CSGCrawler(startUrl, saveUrlPattern, moveUrlPattern, intervalTime, dsm);
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
				((_CSGCrawler)this.crawler).save();
				// 保存後、指定時間スリープ
				((_CSGCrawler)this.crawler).sleep();
			}
			
			
			// このページに存在する移動対象のURLパターンに合致するURLリストを取得する。
			List<A> moveAnchorList = ((_CSGCrawler)this.crawler).moveAnchorList();
			
			// このページにある保存対象のURLパターンに合致するページを保存する。
			((_CSGCrawler)this.crawler).saveBySavePattern();
			
			
			System.exit(0);
		} catch (Neo4JDataStoreManagerException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (PageAccessException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (PageIllegalArgumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (DocumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (CrawlerSaveException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (CrawlerReadException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}catch (RuntimeException e) {
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

class _CSGCrawler extends GCrawler {
	
	protected Pattern saveUrlPattern;
	
	protected Pattern moveUrlPattern;
	
	protected long intervalTime;
	
	public _CSGCrawler(String url, Pattern saveUrlPattern, Pattern moveUrlPattern, long intervalTime, Neo4JDataStoreManager dataStoreManager) throws CrawlerInitException, PageIllegalArgumentException, PageAccessException {
		super(url, dataStoreManager);
		this.saveUrlPattern = saveUrlPattern;
		this.moveUrlPattern = moveUrlPattern;
		this.intervalTime   = intervalTime;
	}
	
	public List<A> moveAnchorList() throws PageAccessException, DocumentException, PageIllegalArgumentException, CrawlerReadException {
		// 移動対象のURLパターンに合致するアンカーを取得する。
		return this.getAnchor(moveUrlPattern);
	}
	
	public List<A> unknownMoveAnchorList() throws PageAccessException, DocumentException, PageIllegalArgumentException, CrawlerReadException {
		// 移動対象のURLパターンに合致するアンカーを取得する。
		List<A> moveAnchorList = this.getAnchor(moveUrlPattern);
		
		// その中からまだ訪れていないページを抽出する。
		List<A> unknownMoveAnchoerList = new ArrayList<>();
		for (A moveAnchor : moveAnchorList) {
			int count = ((GCrawlerA)moveAnchor).getUrlObj().getSavedCountByCache();
			if (count == 0) unknownMoveAnchoerList.add(moveAnchor);
		}
		return unknownMoveAnchoerList;
	}
	
	public void saveBySavePattern() throws DocumentException, PageIllegalArgumentException, PageAccessException, CrawlerReadException, CrawlerSaveException {
		
		// まだ訪れていないページの保存を開始する。
		for (A saveAnchor : this.unknownSaveAnchorList()) {
			this.logger.info("[" + new Date() + "]: SAVE " + saveAnchor.getHref());
			// ページに移動する
			try {
				this.move(saveAnchor);
			} catch (PageRedirectException | PageMovableLimitException e) {
				// エラーになった場合は無視
				this.logger.info("[" + new Date() + "]: ERROR " + saveAnchor.getHref() + " " + e.getMessage());
				continue;
			}
			// ページを保存する
			this.save();
			// 元いたページに戻る
			this.back();
			// 保存後、指定時間スリープ
			this.sleep();
		}
		
		// 保存がひと通り完了したら遷移先の情報をクリアする。
		this.removeChild();
		
	}

	public List<A> unknownSaveAnchorList() throws PageAccessException, DocumentException, PageIllegalArgumentException, CrawlerReadException {
		// 保存対象のURLパターンに合致するアンカーを取得する。
		List<A> saveAnchorList = this.getAnchor(this.saveUrlPattern);
		
		// その中からまだ訪れていないページを抽出する。
		List<A> unknownSaveAnchoerList = new ArrayList<>();
		for (A saveAnchor : saveAnchorList) {
			int count = ((GCrawlerA)saveAnchor).getUrlObj().getSavedCountByCache();
			if (count == 0) unknownSaveAnchoerList.add(saveAnchor);
		}
		return unknownSaveAnchoerList;
	}
	
	/**
	 * 指定時間スリープする。
	 */
	protected void sleep() {
		try {
			Thread.sleep(this.intervalTime * 1000);
		} catch (InterruptedException e) {
		}
	}
}
