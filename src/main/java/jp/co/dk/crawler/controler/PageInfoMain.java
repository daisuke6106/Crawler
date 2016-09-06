package jp.co.dk.crawler.controler;

import java.util.List;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.document.Element;
import jp.co.dk.document.File;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.element.A;
import jp.co.dk.document.html.element.selector.ImageHasSrcElementSelector;
import jp.co.dk.document.html.element.selector.LinkHasRefElementSelector;
import jp.co.dk.document.html.element.selector.ScriptHasSrcElementSelector;
import jp.co.dk.document.json.JsonDocument;
import jp.co.dk.document.xml.XmlDocument;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class PageInfoMain extends AbtractCrawlerControler {

	@Override
	public void execute() { 
		try {
			Page page = new Page(cmd.getOptionValue("u"));
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
			if (cmd.hasOption("N")) {
				jp.co.dk.document.File file = page.getDocument();
				jp.co.dk.document.html.HtmlDocument htmlDocument = (jp.co.dk.document.html.HtmlDocument)file;
				List<jp.co.dk.morphologicalanalysis.Token> tokens = htmlDocument.getNounsByTitle();
				for (jp.co.dk.morphologicalanalysis.Token tolken : tokens) System.out.println(tolken.toString());
			}
		} catch (PageIllegalArgumentException | PageAccessException | DocumentException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		System.exit(0);
	}
	
	@Override
	protected String getCommandName() {
		return "page_info";
	}
	
	@SuppressWarnings("all")
	@Override
	protected void getOptions(Options options){
		options.addOption(OptionBuilder.isRequired(true ).hasArg(true ).withArgName("読込対象のURL").withDescription("読込対象のURL").withLongOpt("target_url").create("u"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(true ).withArgName("読込対象のハッシュ").withDescription("読込対象のハッシュ").withLongOpt("target_hash").create("h"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("リクエストヘッダを表示").withDescription("リクエストヘッダを表示").withLongOpt("requestheader").create("q"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("レスポンスヘッダを表示").withDescription("レスポンスヘッダを表示").withLongOpt("responceheader").create("s"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("データサイズを表示").withDescription("データサイズを表示").withLongOpt("datasize").create("l"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("アクセス日付を表示").withDescription("アクセス日付を表示").withLongOpt("accessdate").create("a"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("データハッシュを表示").withDescription("データハッシュを表示").withLongOpt("hash").create("h"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("データ本体を表示").withDescription("データ本体を表示").withLongOpt("data").create("d"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("アンカー情報を表示").withDescription("アンカー情報を表示").withLongOpt("anchor").create("A"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("イメージ情報を表示").withDescription("イメージ情報を表示").withLongOpt("image").create("I"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("スクリプト情報を表示").withDescription("スクリプト情報を表示").withLongOpt("script").create("S"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("リンク情報を表示").withDescription("リンク情報を表示").withLongOpt("link").create("L"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("ページ表示情報を表示").withDescription("ページ表示情報を表示").withLongOpt("pageinfo").create("C"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("ページ表示情報にある名詞を表示").withDescription("ページ表示情報にある名詞を表示").withLongOpt("nouns").create("N"));
	}
	
	public static void main(String[] args) {
		new PageInfoMain().execute(args);
		System.exit(0);
	}
	
}
