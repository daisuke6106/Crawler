package jp.co.dk.crawler.db.gdb.controler;

import java.io.IOException;
import java.util.List;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.crawler.controler.AbtractCrawlerControler;
import jp.co.dk.crawler.db.gdb.GPage;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerReadException;
import jp.co.dk.document.Element;
import jp.co.dk.document.File;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.exception.DocumentFatalException;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.element.A;
import jp.co.dk.document.html.element.selector.ImageHasSrcElementSelector;
import jp.co.dk.document.html.element.selector.LinkHasRefElementSelector;
import jp.co.dk.document.html.element.selector.ScriptHasSrcElementSelector;
import jp.co.dk.document.json.JsonDocument;
import jp.co.dk.document.xml.XmlDocument;
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
			List<GPage> pageList = GPage.read(cmd.getOptionValue("t_url"), dsm);
			
			for (GPage page : pageList) {
				if (cmd.hasOption("t_hash") && !cmd.getOptionValue("t_hash").equals(page.getData().getHash())) continue; 
				if (cmd.hasOption("q")) System.out.println("REQUEST_HEADER:" + page.getRequestHeader());
				if (cmd.hasOption("s")) System.out.println("RESPONSE_HEADER:" + page.getResponseHeader());
				if (cmd.hasOption("l")) System.out.println("SIZE:" + page.getData().length());
				if (cmd.hasOption("a")) System.out.println("ACCESS_DATE:" + page.getAccessDate());
				if (cmd.hasOption("h")) System.out.println("HASH:" + page.getData().getHash());
				if (cmd.hasOption("d")) System.out.println(page.getDocument().toString());
				if (cmd.hasOption("A")) for (A anchor : page.getAnchor()) System.out.println(anchor.getHref());
				if (cmd.hasOption("I")) {
					File file = page.getDocument();
					if (file instanceof HtmlDocument) {
						HtmlDocument htmlDocument = (HtmlDocument)file;
						for (Element element : htmlDocument.getElement(new ImageHasSrcElementSelector())) System.out.println(element.getAttribute("src"));
					}
				}
				if (cmd.hasOption("S")) {
					File file = page.getDocument();
					if (file instanceof HtmlDocument) {
						HtmlDocument htmlDocument = (HtmlDocument)file;
						for (Element element : htmlDocument.getElement(new ScriptHasSrcElementSelector())) System.out.println(element.getAttribute("src"));
					}
				}
				if (cmd.hasOption("L")) {
					File file = page.getDocument();
					if (file instanceof HtmlDocument) {
						HtmlDocument htmlDocument = (HtmlDocument)file;
						for (Element element : htmlDocument.getElement(new LinkHasRefElementSelector())) System.out.println(element.getAttribute("href"));
					}
				}
				if (cmd.hasOption("C")) {
					File file = page.getDocument();
					if (file instanceof HtmlDocument) {
						HtmlDocument htmlDocument = (HtmlDocument)file;
						System.out.println(htmlDocument.getContent());
					} else if (file instanceof XmlDocument) {
						XmlDocument xmlDocument = (XmlDocument)file;
						System.out.println(xmlDocument.toString());
					} else if (file instanceof JsonDocument) {
						JsonDocument jsonDocument = (JsonDocument)file;
						System.out.println(jsonDocument.toString());
					}
				}
			}
		} catch (CrawlerInitException | Neo4JDataStoreManagerException | PropertyException | IOException | CrawlerReadException | DocumentFatalException | PageAccessException | DocumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		System.exit(0);
	}
	
	@SuppressWarnings("all")
	@Override
	protected void getOptions(Options options){
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true ).withArgName("読込対象のURL"        ).withDescription("読込対象のURL"        ).create("t_url"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(true ).withArgName("読込対象のハッシュ"    ).withDescription("読込対象のハッシュ"   ).create("t_hash"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("リクエストヘッダを表示").withDescription("リクエストヘッダを表示").create("q"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("レスポンスヘッダを表示").withDescription("レスポンスヘッダを表示").create("s"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("データサイズを表示"    ).withDescription("データサイズを表示"   ).create("l"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("アクセス日付を表示"    ).withDescription("アクセス日付を表示"   ).create("a"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("データハッシュを表示"  ).withDescription("データハッシュを表示" ).create("h"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("データ本体を表示"      ).withDescription("データ本体を表示"    ).create("d"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("アンカー情報を表示"    ).withDescription("アンカー情報を表示"  ).create("A"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("イメージ情報を表示"    ).withDescription("イメージ情報を表示"  ).create("I"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("スクリプト情報を表示"  ).withDescription("スクリプト情報を表示").create("S"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("リンク情報を表示"      ).withDescription("リンク情報を表示"    ).create("L"));
	}
	
	public static void main(String[] args) {
		new CrawlerReadMain().execute(args);
		System.exit(0);
	}

	@Override
	protected String getCommandName() {
		return "gcrawler_read";
	}

	@Override
	protected String getDescription() {
		StringBuilder description = new StringBuilder();
		description.append(this.getCommandName());
		description.append("は、Neo4Jに保存されている指定のURLのページ情報を読み込み、標準出力に出力します。").append(System.lineSeparator());
		return description.toString();
	}
	
}
