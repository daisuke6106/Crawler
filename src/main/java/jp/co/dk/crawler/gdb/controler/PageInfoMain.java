package jp.co.dk.crawler.gdb.controler;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.controler.AbtractCrawlerControler;
import jp.co.dk.document.exception.DocumentException;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class PageInfoMain extends AbtractCrawlerControler {

	@Override
	public void execute() { 
		try {
			Page page = new Page(cmd.getOptionValue("t_url"));
			if (cmd.hasOption("q")) System.out.println("REQUEST_HEADER:" + page.getRequestHeader());
			if (cmd.hasOption("s")) System.out.println("RESPONSE_HEADER:" + page.getResponseHeader());
			if (cmd.hasOption("l")) System.out.println("SIZE:" + page.getData().length());
			if (cmd.hasOption("a")) System.out.println("ACCESS_DATE:" + page.getAccessDate());
			if (cmd.hasOption("h")) System.out.println("HASH:" + page.getData().getHash());
			if (cmd.hasOption("d")) System.out.println(page.getDocument().toString());
		
		} catch (PageIllegalArgumentException | PageAccessException | DocumentException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		System.exit(0);
	}
	
	@Override
	protected String getCommandName() {
		return "page_info";
	}
	
	@Override
	protected void getOptions(Options options){
		options.addOption(OptionBuilder.isRequired(true).hasArg(true).withArgName("読込対象のURL").withDescription("読込対象のURL").create("t_url"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(true).withArgName("読込対象のハッシュ").withDescription("読込対象のハッシュ").create("t_hash"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("リクエストヘッダを表示").withDescription("リクエストヘッダを表示").create("q"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("レスポンスヘッダを表示").withDescription("レスポンスヘッダを表示").create("s"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("データサイズを表示").withDescription("データサイズを表示").create("l"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("アクセス日付を表示").withDescription("アクセス日付を表示").create("a"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("データハッシュを表示").withDescription("データハッシュを表示").create("h"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(false).withArgName("データ本体を表示").withDescription("データ本体を表示").create("d"));
	}
	
	public static void main(String[] args) {
		new PageInfoMain().execute(args);
		System.exit(0);
	}
	
}
