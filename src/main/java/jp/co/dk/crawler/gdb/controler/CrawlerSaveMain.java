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

			// インターバル（単位：秒）
			long intervalTime;
			String intervalStr = cmd.getOptionValue("i");
			if (intervalStr != null) {
				intervalTime = Long.parseLong(intervalStr);
			} else {
				intervalTime = 10;
			}
			
			// クローラを生成する。
			GCrawler crawler = null;
			try {
				crawler = new GCrawler(startUrl, dsm);
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
			// 現在のページを保存する。
			crawler.save();
			
			// 保存後、指定時間スリープ
			this.sleep(intervalTime);
			
			// 保存対象のURLパターンに合致するアンカーを取得する。
			List<A> saveAnchorList = crawler.getAnchor(saveUrlPattern);
			
			// その中からまだ訪れていないページを抽出する。
			List<A> unknownSaveAnchoerList = new ArrayList<>();
			for (A saveAnchor : saveAnchorList) {
				int count = ((GCrawlerA)saveAnchor).getUrlObj().getSavedCountByCache();
				if (count == 0) unknownSaveAnchoerList.add(saveAnchor);
			}
			
			// まだ訪れていないページの保存を開始する。
			for (A saveAnchor : unknownSaveAnchoerList) {
				this.logger.info("[" + new Date() + "]: SAVE " + saveAnchor.getHref());
				// ページに移動する
				try {
					crawler.move(saveAnchor);
				} catch (PageRedirectException | PageMovableLimitException e) {
					// エラーになった場合は無視
					this.logger.info("[" + new Date() + "]: ERROR " + saveAnchor.getHref() + " " + e.getMessage());
					continue;
				}
				// ページを保存する
				crawler.save();
				// 元いたページに戻る
				crawler.back();
				// 保存後、指定時間スリープ
				this.sleep(intervalTime);
			}
			
			// 移動対象のURLパターンに合致するアンカーを取得する。
			List<A> moveAnchorList = crawler.getAnchor(moveUrlPattern);
			
			// その中からまだ訪れていないページを抽出する。
			List<A> unknownMoveAnchoerList = new ArrayList<>();
			for (A moveAnchor : moveAnchorList) {
				int count = ((GCrawlerA)moveAnchor).getUrlObj().getSavedCountByCache();
				if (count == 0) unknownMoveAnchoerList.add(moveAnchor);
			}
			
			// まだ訪れていないページの保存を開始する。
			for (A moveAnchor : unknownMoveAnchoerList) {
				this.logger.info("[" + new Date() + "]: MOVE " + moveAnchor.getHref());
				// ページに移動する
				try {
					crawler.move(moveAnchor);
				} catch (PageRedirectException | PageMovableLimitException e) {
					// エラーになった場合は無視
					this.logger.info("[" + new Date() + "]: ERROR " + moveAnchor.getHref() + " " + e.getMessage());
					continue;
				}
				// 正常に遷移元ページ情報はメモリーから除去
				crawler.removeParent();
				// 保存後、指定時間スリープ
				this.sleep(intervalTime);
			}
			
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
	
	/**
	 * 指定時間スリープする。
	 * @param intervalTime 秒
	 */
	private void sleep(long intervalTime) {
		try {
			Thread.sleep(intervalTime * 1000);
		} catch (InterruptedException e) {
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
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true ).withArgName("URL"   ).withDescription("保存対象のURLパターン（正規表現にて指定）").withLongOpt("save_pattern").create("s"));
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true ).withArgName("URL"   ).withDescription("移動先のURLパターン（正規表現にて指定）").withLongOpt("move_pattern").create("m"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(true ).withArgName("minuts").withDescription("インターバル（単位：秒）").withLongOpt("interval").create("i"));
	}
	
	public static void main(String[] args) {
		new CrawlerSaveMain().execute(args);
	}
	
}
