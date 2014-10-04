package jp.co.dk.crawler.controler;

import java.util.ArrayList;
import java.util.List;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.Crawler;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.property.DataStoreManagerProperty;
import jp.co.dk.document.Element;
import jp.co.dk.document.ElementSelector;
import jp.co.dk.document.File;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.HtmlElement;
import jp.co.dk.document.html.constant.HtmlElementName;
import jp.co.dk.property.exception.PropertyException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

public class CrawlerMain {
	
	protected Options options = new Options();
	
	protected CommandLine cmd;
	
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
	public void main(String[] args) {
		this.options.addOption(OptionBuilder.isRequired(true).hasArg(true).withArgName("保存対象のURL").withDescription("保存対象のURL").create("url"));
		this.options.addOption(OptionBuilder.isRequired(false).hasArg(false).withDescription("そのURLに関するデータをすべて保存するか").create("all"));
		try {
			this.cmd = new PosixParser().parse(options, args);
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
			if (cmd.hasOption("all")) {
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
	
	
	/**
	 * 指定のページが参照している以下の要素を取得します。<p/>
	 * <br/>
	 * 指定のページがHTML以外である場合、空のリストを返却します。<br/>
	 * <br/>
	 * ・link  （hrefに値が設定されている要素のみ）<br/>
	 * ・script（srcに値が設定されている要素のみ）<br/>
	 * ・image （srcに値が設定されている要素のみ）<br/>
	 * 
	 * @param page 要素取得先ページ
	 * @return 要素一覧
	 * @throws BrowzingException ページ情報の読み込みに失敗した場合
	 * @throws DocumentException 
	 */
	List<Element> getRefsElements(Page page) throws BrowzingException, DocumentException {
		List<Element> elementList = new ArrayList<Element>();
		File file = page.getDocument();
		if (file instanceof HtmlDocument) return elementList;
		HtmlDocument htmlDocument = (HtmlDocument)file;
		List<Element> links   = htmlDocument.getElement(new LinkElementSelector());
		List<Element> scripts = htmlDocument.getElement(new ScriptElementSelector());
		List<Element> imgs    = htmlDocument.getElement(new ImageElementSelector());
		elementList.addAll(links);
		elementList.addAll(scripts);
		elementList.addAll(imgs);
		return elementList;
	}
	
	/**
	 * 指定のページが参照している以下の要素を取得します。<p/>
	 * <br/>
	 * 指定のページがHTML以外である場合、空のリストを返却します。<br/>
	 * <br/>
	 * ・anchor（hrefに値が設定されている要素のみ）<br/>
	 * 
	 * @param page 要素取得先ページ
	 * @return 要素一覧
	 * @throws BrowzingException ページ情報の読み込みに失敗した場合
	 * @throws DocumentException 
	 */
	List<Element> getAnchorElements(Page page) throws BrowzingException, DocumentException {
		List<Element> elementList = new ArrayList<Element>();
		File file = page.getDocument();
		if (file instanceof HtmlDocument) return elementList;
		HtmlDocument htmlDocument = (HtmlDocument)file;
		List<Element> anchors = htmlDocument.getElement(new AnchorElementSelector());
		elementList.addAll(anchors);
		return elementList;
	}
}

class MovableElementSelector implements ElementSelector {
	@Override
	public boolean judgment(Element element) {
		if (element instanceof MovableElement) return true;
		return false;
	}
	
}

class AnchorElementSelector implements ElementSelector {
	@Override
	public boolean judgment(Element element) {
		if (!(element instanceof HtmlElement)) return false;
		HtmlElement htmlElement = (HtmlElement)element;
		if (htmlElement.getElementType() == HtmlElementName.A && htmlElement.hasAttribute("href") && (!htmlElement.getAttribute("href").equals("")) ) return true;
		return false;
	}
	
}

class LinkElementSelector implements ElementSelector {
	@Override
	public boolean judgment(Element element) {
		if (!(element instanceof HtmlElement)) return false;
		HtmlElement htmlElement = (HtmlElement)element;
		if (htmlElement.getElementType() == HtmlElementName.LINK && htmlElement.hasAttribute("href") && (!htmlElement.getAttribute("href").equals("")) ) return true;
		return false;
	}
}

class ScriptElementSelector implements ElementSelector {
	@Override
	public boolean judgment(Element element) {
		if (!(element instanceof HtmlElement)) return false;
		HtmlElement htmlElement = (HtmlElement)element;
		if (htmlElement.getElementType() == HtmlElementName.SCRIPT && htmlElement.hasAttribute("src") && (!htmlElement.getAttribute("src").equals(""))) return true;
		return false;
	}
}

class ImageElementSelector implements ElementSelector {
	@Override
	public boolean judgment(Element element) {
		if (!(element instanceof HtmlElement)) return false;
		HtmlElement htmlElement = (HtmlElement)element;
		if (htmlElement.getElementType() == HtmlElementName.IMG && htmlElement.hasAttribute("src") && (!htmlElement.getAttribute("src").equals(""))) return true;
		return false;
	}
	
}