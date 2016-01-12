package jp.co.dk.crawler;

import java.util.ArrayList;
import java.util.List;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.PageEventHandler;
import jp.co.dk.browzer.PageRedirectHandler;
import jp.co.dk.browzer.event.PrintPageEventHandler;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageMovableLimitException;
import jp.co.dk.browzer.exception.PageRedirectException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.eventhandler.LogPageEventHandler;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.document.Element;
import jp.co.dk.document.ElementSelector;
import jp.co.dk.document.File;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.HtmlElement;
import jp.co.dk.document.html.constant.HtmlElementName;
import jp.co.dk.document.html.element.selector.ImageHasSrcElementSelector;
import jp.co.dk.document.html.element.selector.LinkHasRefElementSelector;
import jp.co.dk.document.html.element.selector.ScriptHasSrcElementSelector;

public abstract class AbstractCrawler extends Browzer {
	
	/**
	 * コンストラクタ<p/>
	 * 指定のＵＲＬ、クローラクラスのインスタンスを生成する。
	 * 
	 * @param url 接続先URL
	 * @throws CrawlerInitException クローラの初期化処理に失敗した場合
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	public AbstractCrawler(String url) throws CrawlerInitException, PageIllegalArgumentException, PageAccessException { 
		super(url);
	}
	
	/**
	 * 現在アクティブになっているページを保存する。
	 * 
	 * @return 保存結果（true=保存された、false=すでにデータが存在するため、保存されなかった）
	 * @throws CrawlerException データストアの登録時に必須項目が設定されていなかった場合
	 */
	public abstract boolean save() throws CrawlerSaveException;
	
	@Override
	protected abstract AbstractPageManager createPageManager(String url, PageRedirectHandler handler) throws PageIllegalArgumentException, PageAccessException ;
	
	@Override
	protected abstract AbstractPageManager createPageManager(String url, PageRedirectHandler handler, int maxNestLevel) throws PageIllegalArgumentException, PageAccessException ;
	
	@Override
	protected abstract AbstractPageRedirectHandler createPageRedirectHandler();
	
	
	/**
	 * 現在アクティブになっているページの情報と、そのページが参照するIMG、SCRIPT、LINKタグが参照するページをデータストアへ保存します。<p/>
	 * 
	 * @throws PageAccessException          アクティブになっているページのページデータの取得に失敗した場合
	 * @throws DocumentException            アクティブになっているページのドキュメントオブジェクトの生成に失敗した場合
	 * @throws PageMovableLimitException    ページ遷移可能上限数に達した場合
	 * @throws PageRedirectException        遷移に失敗した場合
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws CrawlerSaveException         保存に失敗した場合
	 */
	public void saveAll() throws PageAccessException, DocumentException, PageIllegalArgumentException, PageRedirectException, PageMovableLimitException, CrawlerSaveException {
		jp.co.dk.crawler.AbstractPage activePage = (jp.co.dk.crawler.AbstractPage)this.getPage();
		activePage.save();
		this.saveImage();
		this.saveScript();
		this.saveLink();
		this.pageManager.removeChild();
	}
	
	/**
	 * 現在アクティブになっているページに記載されているすべてのIMGタグを元にそのページに遷移し、データストアへの保存を実施します。<p/>
	 * アクティブページがHTMLでない場合、何もせずに処理を終了します。<br/>
	 * <br/>
	 * アクティブページからページ遷移した場合に、そのページに接続できないなどの状態が発生した場合、「errorHandler」メソッドにエラー処理制御が移譲されます。<br/>
	 * 「errorHandler」にてCrawlerExceptionをthrowした場合、その時点で処理が終了します。<br/>
	 * 継続させたい場合、なにもthrowさせず、処理を完了させてください。<br/>
	 * 「errorHandler」内の処理を定義する場合、本クラスを継承し、「errorHandler」をオーバーライドし、処理を記載してください。<br/>
	 * 
	 * @throws PageAccessException          アクティブになっているページのページデータの取得に失敗した場合
	 * @throws DocumentException            アクティブになっているページのドキュメントオブジェクトの生成に失敗した場合
	 * @throws PageMovableLimitException    ページ遷移可能上限数に達した場合
	 * @throws PageRedirectException        遷移に失敗した場合
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws CrawlerSaveException         保存に失敗した場合
	 */
	public void saveImage() throws PageAccessException, DocumentException, PageIllegalArgumentException, PageRedirectException, PageMovableLimitException, CrawlerSaveException {
		jp.co.dk.crawler.AbstractPage activePage = (jp.co.dk.crawler.AbstractPage)this.getPage();
		File file = activePage.getDocument();
		if (!(file instanceof HtmlDocument)) return;
		HtmlDocument htmlDocument = (HtmlDocument)file;
		List<Element> elements = htmlDocument.getElement(new ImageHasSrcElementSelector());
		for (Element element : elements) {
			jp.co.dk.browzer.html.element.Image castedElement = (jp.co.dk.browzer.html.element.Image)element;
			this.move(castedElement);
			this.save();
			this.back();
		}
	}
	
	/**
	 * 現在アクティブになっているページに記載されているすべてのSCRIPTタグを元にそのページに遷移し、データストアへの保存を実施します。<p/>
	 * アクティブページがHTMLでない場合、何もせずに処理を終了します。<br/>
	 * <br/>
	 * アクティブページからページ遷移した場合に、そのページに接続できないなどの状態が発生した場合、「errorHandler」メソッドにエラー処理制御が移譲されます。<br/>
	 * 「errorHandler」にてCrawlerExceptionをthrowした場合、その時点で処理が終了します。<br/>
	 * 継続させたい場合、なにもthrowさせず、処理を完了させてください。<br/>
	 * 「errorHandler」内の処理を定義する場合、本クラスを継承し、「errorHandler」をオーバーライドし、処理を記載してください。<br/>
	 * 
	 * @throws PageAccessException          アクティブになっているページのページデータの取得に失敗した場合
	 * @throws DocumentException            アクティブになっているページのドキュメントオブジェクトの生成に失敗した場合
	 * @throws PageMovableLimitException    ページ遷移可能上限数に達した場合
	 * @throws PageRedirectException        遷移に失敗した場合
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws CrawlerSaveException         保存に失敗した場合
	 */
	public void saveScript() throws PageAccessException, DocumentException, PageIllegalArgumentException, PageRedirectException, PageMovableLimitException, CrawlerSaveException {
		jp.co.dk.crawler.AbstractPage activePage = (jp.co.dk.crawler.AbstractPage)this.getPage();
		File file = activePage.getDocument();
		if (!(file instanceof HtmlDocument)) return;
		HtmlDocument htmlDocument = (HtmlDocument)file;
		List<Element> elements = htmlDocument.getElement(new ScriptHasSrcElementSelector());
		for (Element element : elements) {
			jp.co.dk.browzer.html.element.Script castedElement = (jp.co.dk.browzer.html.element.Script)element;
			this.move(castedElement);
			this.save();
			this.back();
		}
	}
	
	/**
	 * 現在アクティブになっているページに記載されているすべてのLINKタグを元にそのページに遷移し、データストアへの保存を実施します。<p/>
	 * アクティブページがHTMLでない場合、何もせずに処理を終了します。<br/>
	 * <br/>
	 * アクティブページからページ遷移した場合に、そのページに接続できないなどの状態が発生した場合、「errorHandler」メソッドにエラー処理制御が移譲されます。<br/>
	 * 「errorHandler」にてCrawlerExceptionをthrowした場合、その時点で処理が終了します。<br/>
	 * 継続させたい場合、なにもthrowさせず、処理を完了させてください。<br/>
	 * 「errorHandler」内の処理を定義する場合、本クラスを継承し、「errorHandler」をオーバーライドし、処理を記載してください。<br/>
	 * 
	 * @throws PageAccessException       アクティブになっているページのページデータの取得に失敗した場合
	 * @throws DocumentException         アクティブになっているページのドキュメントオブジェクトの生成に失敗した場合
	 * @throws CrawlerException          例外が発生し、「errorHandler」にて処理を停止すると判定された場合
	 * @throws DataStoreManagerException データストアの操作に失敗した場合
	 */
	public void saveLink() throws PageAccessException, DocumentException, PageIllegalArgumentException, PageRedirectException, PageMovableLimitException, CrawlerSaveException {
		jp.co.dk.crawler.AbstractPage activePage = (jp.co.dk.crawler.AbstractPage)this.getPage();
		File file = activePage.getDocument();
		if (!(file instanceof HtmlDocument)) return;
		HtmlDocument htmlDocument = (HtmlDocument)file;
		List<Element> elements = htmlDocument.getElement(new LinkHasRefElementSelector());
		for (Element element : elements) {
			jp.co.dk.browzer.html.element.Link castedElement = (jp.co.dk.browzer.html.element.Link)element;
			this.move(castedElement);
			this.save();
			this.back();
		}
	}
	
	@Override
	protected List<PageEventHandler> getPageEventHandler() {
		List<PageEventHandler> list = new ArrayList<PageEventHandler>();
		list.add(new PrintPageEventHandler());
		list.add(new LogPageEventHandler());
		return list;
	}
}

class MovableElementSelector implements ElementSelector {
	@Override
	public boolean judgment(Element element) {
		if (element instanceof MovableElement) return true;
		return false;
	}
}

class AnchorExcludeHTMLElementSelector implements ElementSelector {
	@Override
	public boolean judgment(Element element) {
		if (!(element instanceof HtmlElement)) return false;
		HtmlElement htmlElement = (HtmlElement)element;
		if (htmlElement.getElementType() == HtmlElementName.A 
			&& htmlElement.hasAttribute("href") 
			&& !htmlElement.getAttribute("href").equals("")
			&& (!htmlElement.getAttribute("href").endsWith(".html")
				|| !htmlElement.getAttribute("href").endsWith(".htm"))
		) return true;
		return false;
	}
}

class AnchorIncludeHTMLElementSelector implements ElementSelector {
	@Override
	public boolean judgment(Element element) {
		if (!(element instanceof HtmlElement)) return false;
		HtmlElement htmlElement = (HtmlElement)element;
		if (htmlElement.getElementType() == HtmlElementName.A && htmlElement.hasAttribute("href") && (!htmlElement.getAttribute("href").equals("")) ) return true;
		return false;
	}
}
