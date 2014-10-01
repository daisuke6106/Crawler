package jp.co.dk.crawler.controler;

import java.util.ArrayList;
import java.util.List;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.Crawler;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.logger.Logger;
import jp.co.dk.logger.LoggerFactory;
import jp.co.dk.property.exception.PropertyException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class CrawlerMain {
	
	/**
	 * ---OptionBuilder---
	 * 
	 * hasArg（オプションの後にパラメータが必須か）
	 * そのオプションが引数をとるかどうかを決定するもの。
	 * 例えば"-o"オプションであればこのメソッドを引数なしでコールするか、
	 * trueを引数にしてコールすることになる。"-p"オプションであればそもそもこのメソッドをコールしないか、あえてするならばfalseを引数にしてコールする。
	 * （int型をとる多重定義メソッドもあって、この場合はオプション引数としてとる値の個数のリミットを設定できる模様。）
	 * 
	 * isRequired（オプションそのものが必須か）
	 * そのオプションが必須のものであるかどうかを決定するもの。hasArgメソッド同様にあえてboolean型の引数を設定することもできる。
	 * 
	 * withArgName（パラメータ名）
	 * このメソッドはヘルプとか使用方法の表示に関わってくるメタ情報を設定するもので、上記の例で言えば"option-argument"の部分を決定するもの。
	 * 
	 * withDescription（Usage出力用の説明）
	 * withArgNameと同じくヘルプとか使用方法の表示に関わってくるメタ情報を設定するもの。
	 * 
	 * withLongOpt（オプションの別名）
	 * "--help"などのようにイニシャルではなくワードを用いて指定する場合の名前を設定するもの。これを設定しない場合どうなるのかよくわからないが、
	 * 実際のところアプリのユーザのためにもコードの可読性のためにも、設定しておいた方がいいと思う。
	 * 
	 * create（指定の名前でオプション作成）
	 * このメソッドによりOption型インスタンスが得られる。例えば'o'とか'p'とかの文字を設定する。前掲の例のように文字列でもよい。
	 * 
	 * 
	 * ---CommandLine---
	 * getArgs
	 * いかなるオプションにも紐付かない引数をString[]として取得できる。
	 * つまりコマンドライン・オプションやコマンドライン・オプション引数ではない、コマンドライン引数そのもののみを取得できる。
	 * 例えばMyCmdの例でいえば"foobar"が格納された配列が返される。
	 * 
	 * getOptionValue
	 * 第1引数にオプション名を指定してオプション引数を取得する。
	 * 第2引数をとる多重定義メソッドが存在して、こちらは第2引数を設定することで、オプションが設定されていないときのデフォルト値とすることができる。
	 * 
	 * hasOption
	 * オプション名を引数にとって、そのオプションがコマンドラインで入力されたかどうかをチェックできる。
	 * 例えば"-p"のようなフラグオプションが設定されているかどうかを確認できる。
	 * 
	 * @param args 起動引数
	 */
	public static void main(String[] args) {
		
		Options options = new Options();
		List<Option> optionList = new ArrayList<Option>();
		Option  url = OptionBuilder.isRequired(true).hasArg(true).withArgName("保存対象のURL").withDescription("保存対象のURL").create( "url" );
		Option  all = OptionBuilder.isRequired(false).hasArg(false).withDescription("そのURLに関するデータをすべて保存するか").create( "all" );
		options.addOption(url);
		options.addOption(all);
		CommandLine cmd = null;
		try {
			cmd = new PosixParser().parse(options, args);
		} catch (ParseException e) {
			HelpFormatter help = new HelpFormatter();
			help.printHelp("crawler", options, true);
			System.exit(1);
		}
		DataStoreManager dsm    = null;
		try {
			dsm = new DataStoreManager(new DataStoreManagerProperty());
		} catch (DataStoreManagerException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		} catch (PropertyException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
		
		try {
			dsm.startTrunsaction();
			Crawler crawler = new Crawler(cmd.getOptionValue("url"), dsm);
			if (cmd.hasOption(all.getOpt())) {
				crawler.saveAll();
			} else {
 				crawler.save();
			}
			dsm.finishTrunsaction();
		} catch (CrawlerInitException | PageIllegalArgumentException | PageAccessException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (CrawlerSaveException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (DataStoreManagerException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (DocumentException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		} catch (CrawlerException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(1);
		}
		
	}
}
