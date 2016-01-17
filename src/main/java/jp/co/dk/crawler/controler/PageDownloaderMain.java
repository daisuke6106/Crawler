package jp.co.dk.crawler.controler;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageSaveException;
import jp.co.dk.document.exception.DocumentException;

import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class PageDownloaderMain extends AbtractCrawlerControler {

	@Override
	public void execute() { 
		try {
			Page page = new Page(cmd.getOptionValue("t_url"));
			String name;
			if (cmd.hasOption("name")) {
				name = cmd.getOptionValue("name");
			} else {
				name = page.getFileName();
			}
			java.io.File dir;
			if (cmd.hasOption("dir")) {
				dir = new java.io.File(cmd.getOptionValue("dir"));
			} else {
				dir = new java.io.File(new java.io.File(".").getAbsoluteFile().getParent());
			}
			page.save(dir, name);
			
		} catch (PageIllegalArgumentException | PageAccessException | PageSaveException | DocumentException e) {
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
		options.addOption(OptionBuilder.isRequired(false).hasArg(true).withArgName("保存先パス").withDescription("読込対象のハッシュ").create("dir"));
		options.addOption(OptionBuilder.isRequired(false).hasArg(true).withArgName("ファイル名").withDescription("ファイル名").create("name"));
		
	}
	
	public static void main(String[] args) {
		new PageDownloaderMain().execute(args);
		System.exit(0);
	}
	
}
