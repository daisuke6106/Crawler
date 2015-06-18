package jp.co.dk.crawler.rdb;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.PageEventHandler;
import jp.co.dk.browzer.PageManager;
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
import jp.co.dk.crawler.exception.CrawlerPageRedirectHandlerException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.message.CrawlerMessage;
import jp.co.dk.crawler.rdb.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.rdb.dao.CrawlerErrors;
import jp.co.dk.crawler.rdb.dao.Links;
import jp.co.dk.crawler.rdb.dao.RedirectErrors;
import jp.co.dk.crawler.rdb.dao.record.LinksRecord;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.Element;
import jp.co.dk.document.ElementSelector;
import jp.co.dk.document.File;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.exception.DocumentFatalException;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.HtmlElement;
import jp.co.dk.document.html.constant.HtmlElementName;

/**
 * Crawlerは、ネットワーク上に存在するHTML、XML、ファイルを巡回し、指定された出力先へ保存を行う処理を制御するクラス。<p/>
 * 保存先には、MySql等のRDBのデータストアに保存される。<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class Crawler extends Browzer{
	
	/** データストアマネージャー */
	protected DataStoreManager dsm;
	
	/**
	 * コンストラクタ<p/>
	 * 指定のデータストアマネージャを元に、クローラクラスのインスタンスを生成する。<br/>
	 * データストアマネージャが設定されていなかった場合、例外を送出する。
	 * 
	 * @param dataStoreManager データストアマネージャ
	 * @throws CrawlerInitException クローラの初期化処理に失敗した場合
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	public Crawler(String url, DataStoreManager dataStoreManager) throws CrawlerInitException, PageIllegalArgumentException, PageAccessException { 
		super(url);
		if (dataStoreManager == null) throw new CrawlerInitException(CrawlerMessage.DATASTOREMANAGER_IS_NOT_SET);
		this.dsm                 = dataStoreManager;
		this.pageRedirectHandler = new CrawlerPageRedirectHandler(dataStoreManager, this.getPageEventHandler());
		super.pageManager = this.createPageManager(url, this.pageRedirectHandler);
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
			this.addLinks(activeUrl, nextUrl);
			this.move(movable);
			this.save();
			return MoveInfo.MOVED;
		} catch (CrawlerSaveException | PageIllegalArgumentException | PageAccessException | PageRedirectException | PageMovableLimitException e) {
			this.errorHandler(activePage, nextUrl, e);
			return MoveInfo.NON_MOVED;
		}
	}
	
	/**
	 * 現在アクティブになっているページを保存する。
	 * 
	 * @return 保存結果（true=保存された、false=すでにデータが存在するため、保存されなかった）
	 * @throws CrawlerException データストアの登録時に必須項目が設定されていなかった場合
	 */
	public boolean save() throws CrawlerSaveException {
		jp.co.dk.crawler.rdb.Page page = (jp.co.dk.crawler.rdb.Page) this.getPage();
		return page.save();
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
	protected void errorHandler (Page beforePage, Url nextPageUrl, Throwable throwable) throws CrawlerException, DataStoreManagerException, DocumentFatalException, PageAccessException {
		if (throwable instanceof CrawlerPageRedirectHandlerException) {
			CrawlerPageRedirectHandlerException crawlerPageRedirectHandlerException = (CrawlerPageRedirectHandlerException) throwable;
			jp.co.dk.crawler.rdb.Page errorPage = (jp.co.dk.crawler.rdb.Page)crawlerPageRedirectHandlerException.getPage();
			this.addLinks(beforePage.getUrl(), errorPage.getUrl());
			RedirectErrors errors = (RedirectErrors)this.dsm.getDataAccessObject(CrawlerDaoConstants.REDIRECT_ERRORS);
			String fileId = errorPage.getFileId();
			long   timeId = errorPage.getTimeId();
			String message = throwable.getMessage();
			StackTraceElement[] stackTraceElements = throwable.getStackTrace();
			Date createDate = new Date();
			Date updateDate = new Date();
			errors.insert(fileId, timeId, message, stackTraceElements, createDate, updateDate);
			return ;
		} else {
			this.addLinks(beforePage.getUrl(), nextPageUrl);
			CrawlerErrors errors = (CrawlerErrors)this.dsm.getDataAccessObject(CrawlerDaoConstants.CRAWLER_ERRORS);
			String protcol                = nextPageUrl.getProtocol();
			String host                   = nextPageUrl.getHost();
			List<String> path             = nextPageUrl.getPathList();
			Map<String, String> parameter = nextPageUrl.getParameter();
			String message                = throwable.getMessage();
			StackTraceElement[] stackTraceElements = throwable.getStackTrace();
			Date createDate = new Date();
			Date updateDate = new Date();
			errors.insert(protcol, host, path, parameter, message, stackTraceElements, createDate, updateDate);
		}
		
	}
	
	/**
	 * 遷移元ページと遷移先ページの情報を元にLINKSテーブルにその情報を書き込こみます。<p/>
	 * すでにレコードが存在する場合、何も行いません。
	 * 
	 * @param beforePage 遷移元ページ情報
	 * @param toPage     遷移先ページ情報
	 * @return true=保存をおこなった場合、false=すでにレコードが存在した場合
	 * @throws DataStoreManagerException 登録処理に失敗した場合
	 * @throws CrawlerException 必須項目が不足している場合
	 */
	protected boolean addLinks(Url beforePageUrl, Url toPageUrl) throws DataStoreManagerException, CrawlerException {
		Links links = (Links)this.dsm.getDataAccessObject(CrawlerDaoConstants.LINKS);
		String             from_protcol   = beforePageUrl.getProtocol();
		String             from_host      = beforePageUrl.getHost();
		List<String>       from_path      = beforePageUrl.getPathList();
		Map<String,String> from_parameter = beforePageUrl.getParameter();
		String             to_protcol     = toPageUrl.getProtocol();
		String             to_host        = toPageUrl.getHost();
		List<String>       to_path        = toPageUrl.getPathList();
		Map<String,String> to_parameter   = toPageUrl.getParameter();
		Date createDate = new Date();
		Date updateDate = new Date();
		LinksRecord savedLinksRecord = links.select(from_protcol, from_host, from_path, from_parameter, to_protcol, to_host, to_path, to_parameter, createDate, updateDate);
		if (savedLinksRecord != null) return false;
		links.insert(from_protcol, from_host, from_path, from_parameter, to_protcol, to_host, to_path, to_parameter, createDate, updateDate);
		return true;
	}
	
	public DataStoreManager getDataStoreManager() {
		return this.dsm;
	}
	
	@Override
	protected PageManager createPageManager(String url, PageRedirectHandler handler) throws PageIllegalArgumentException, PageAccessException {
		return new CrawlerPageManager(this.dsm, url, handler, this.pageEventHandlerList);
	}
	
	@Override
	protected PageManager createPageManager(String url, PageRedirectHandler handler, int maxNestLevel) throws PageIllegalArgumentException, PageAccessException {
		return new CrawlerPageManager(this.dsm, url, handler, this.pageEventHandlerList, maxNestLevel);
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