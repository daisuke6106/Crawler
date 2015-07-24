package jp.co.dk.crawler;

import java.util.ArrayList;
import java.util.List;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.PageEventHandler;
import jp.co.dk.browzer.PageRedirectHandler;
import jp.co.dk.browzer.Url;
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
import jp.co.dk.crawler.rdb.MoveInfo;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.Element;
import jp.co.dk.document.ElementSelector;
import jp.co.dk.document.File;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.exception.DocumentFatalException;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.HtmlElement;
import jp.co.dk.document.html.constant.HtmlElementName;

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
	 * 指定の遷移可能要素に遷移し、そのページ情報を保存します。
	 * 正常に遷移し、ページ情報がセーブできた場合、MoveInfo.MOVEを返却します。
	 * 
	 * 指定された遷移可能要素にURLが設定されていなかった場合は、その時点でMoveInfo.NON_MOVEDを返却し、処理を終了します。
	 * 
	 * 指定の遷移可能要素に遷移した際に、ページが存在しなかったなどで遷移できなかった場合、本クラスのerrorHandlerメソッドにエラー制御が移譲され、MoveInfo.NON_MOVEDが返却されます。
	 * 
	 * 
	 * @param movable 遷移可能要素
	 * @return 遷移状態（MOVE=指定のページに遷移済み且つ保存済み、NON_MOVED=未遷移状態）
	 * @throws CrawlerException errorHandlerメソッドにてエラーにて終了と判定された場合
	 * @throws DataStoreManagerException データストアへの保存処理に失敗した場合
	 * @throws PageAccessException ページデータの取得に失敗した場合
	 * @throws DocumentFatalException 
	 */
	public MoveInfo moveWithSave(MovableElement movable) throws CrawlerException, DataStoreManagerException, DocumentFatalException, PageAccessException {
		Page activePage = this.getPage();
		Url activeUrl   = activePage.getUrl();
		Url nextUrl;
		try {
			nextUrl = new Url(movable.getUrl());
		} catch (PageIllegalArgumentException e) {
			return MoveInfo.NON_MOVED;
		}
		try {
			this.move(movable);
			this.save();
			return MoveInfo.MOVED;
		} catch (CrawlerSaveException | PageIllegalArgumentException | PageAccessException | PageRedirectException | PageMovableLimitException e) {
			this.errorHandler(activePage, nextUrl, e);
			return MoveInfo.NON_MOVED;
		}
	}
	
	/**
	 * 現在アクティブになっているページの情報と、そのページが参照するIMG、SCRIPT、LINKタグが参照するページをデータストアへ保存します。<p/>
	 * 
	 * @throws PageAccessException       アクティブになっているページのページデータの取得に失敗した場合
	 * @throws DocumentException         アクティブになっているページのドキュメントオブジェクトの生成に失敗した場合
	 * @throws CrawlerException          例外が発生し、「errorHandler」にて処理を停止すると判定された場合
	 * @throws DataStoreManagerException データストアの操作に失敗した場合
	 */
	public void saveAll() throws PageAccessException, DocumentException, CrawlerException, CrawlerSaveException, DataStoreManagerException {
		jp.co.dk.crawler.rdb.Page activePage = (jp.co.dk.crawler.rdb.Page)this.getPage();
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
	 * @throws PageAccessException       アクティブになっているページのページデータの取得に失敗した場合
	 * @throws DocumentException         アクティブになっているページのドキュメントオブジェクトの生成に失敗した場合
	 * @throws CrawlerException          例外が発生し、「errorHandler」にて処理を停止すると判定された場合
	 * @throws DataStoreManagerException データストアの操作に失敗した場合
	 */
	public void saveImage() throws PageAccessException, DocumentException, CrawlerException, DataStoreManagerException {
		jp.co.dk.crawler.rdb.Page activePage = (jp.co.dk.crawler.rdb.Page)this.getPage();
		File file = activePage.getDocument();
		if (!(file instanceof HtmlDocument)) return;
		HtmlDocument htmlDocument = (HtmlDocument)file;
		List<Element> elements = htmlDocument.getElement(new ImageElementSelector());
		for (Element element : elements) {
			jp.co.dk.browzer.html.element.Image castedElement = (jp.co.dk.browzer.html.element.Image)element;
			this.moveWithSave(castedElement);
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
	 * @throws PageAccessException       アクティブになっているページのページデータの取得に失敗した場合
	 * @throws DocumentException         アクティブになっているページのドキュメントオブジェクトの生成に失敗した場合
	 * @throws CrawlerException          例外が発生し、「errorHandler」にて処理を停止すると判定された場合
	 * @throws DataStoreManagerException データストアの操作に失敗した場合
	 */
	public void saveScript() throws PageAccessException, DocumentException, CrawlerException, DataStoreManagerException {
		jp.co.dk.crawler.rdb.Page activePage = (jp.co.dk.crawler.rdb.Page)this.getPage();
		File file = activePage.getDocument();
		if (!(file instanceof HtmlDocument)) return;
		HtmlDocument htmlDocument = (HtmlDocument)file;
		List<Element> elements = htmlDocument.getElement(new ScriptElementSelector());
		for (Element element : elements) {
			jp.co.dk.browzer.html.element.Script castedElement = (jp.co.dk.browzer.html.element.Script)element;
			this.moveWithSave(castedElement);
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
	public void saveLink() throws PageAccessException, DocumentException, CrawlerException, DataStoreManagerException {
		jp.co.dk.crawler.rdb.Page activePage = (jp.co.dk.crawler.rdb.Page)this.getPage();
		File file = activePage.getDocument();
		if (!(file instanceof HtmlDocument)) return;
		HtmlDocument htmlDocument = (HtmlDocument)file;
		List<Element> elements = htmlDocument.getElement(new LinkElementSelector());
		for (Element element : elements) {
			jp.co.dk.browzer.html.element.Link castedElement = (jp.co.dk.browzer.html.element.Link)element;
			this.moveWithSave(castedElement);
			this.back();
		}
	}
	
	/**
	 * 現在アクティブになっているページを保存する。
	 * 
	 * @return 保存結果（true=保存された、false=すでにデータが存在するため、保存されなかった）
	 * @throws CrawlerException データストアの登録時に必須項目が設定されていなかった場合
	 */
	public abstract boolean save() throws CrawlerSaveException;
	
	/**
	 * クローリング実行中に例外が発生した場合、その際のエラー処理方法を定義します。<p/>
	 * エラー発生後、本メソッドにてハンドリング処理を実施後、処理を継続させる場合、本メソッド内にて例外を送出させることなく、処理を完了させてください。
	 * エラー発生後、本メソッドにてハンドリング処理を実施後、処理を停止させる場合、本メソッド内にて例外を送出させてください。
	 * 
	 * @param throwable 発生した例外オブジェクト
	 * @throws CrawlerException エラーハンドリング後、処理を停止させる場合
	 * @throws DataStoreManagerException データストア関連処理に失敗した場合
	 * @throws PageAccessException ページアクセスに失敗した場合
	 * @throws DocumentFatalException 暗号化処理にて致命的例外が発生した場合
	 */
	protected abstract void errorHandler (Page beforePage, Url nextPageUrl, Throwable throwable) throws CrawlerException, DataStoreManagerException, DocumentFatalException, PageAccessException;
	
	@Override
	protected abstract AbstractPageManager createPageManager(String url, PageRedirectHandler handler) throws PageIllegalArgumentException, PageAccessException ;
	
	@Override
	protected abstract AbstractPageManager createPageManager(String url, PageRedirectHandler handler, int maxNestLevel) throws PageIllegalArgumentException, PageAccessException ;
	
	@Override
	protected abstract AbstractPageRedirectHandler createPageRedirectHandler();
	
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