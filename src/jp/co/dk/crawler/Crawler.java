package jp.co.dk.crawler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.PageManager;
import jp.co.dk.browzer.PageRedirectHandler;
import jp.co.dk.browzer.Url;
import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.browzer.http.header.ResponseHeader;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.CrawlerErrors;
import jp.co.dk.crawler.dao.RedirectErrors;
import jp.co.dk.crawler.dao.Links;
import jp.co.dk.crawler.dao.record.LinksRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.exception.CrawlerPageRedirectHandlerException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.Element;
import jp.co.dk.document.ElementSelector;
import jp.co.dk.document.File;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.HtmlElement;
import jp.co.dk.document.html.constant.HtmlElementName;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

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
	 * @throws DataStoreManagerException データストアプロパティの取得に失敗した場合
	 * @throws BrowzingException ページ情報の読み込みに失敗した場合
	 */
	public Crawler(String url, DataStoreManager dataStoreManager) throws CrawlerException, BrowzingException { 
		super(url, new CrawlerPageRedirectHandler(dataStoreManager));
		this.dsm         = dataStoreManager;
		this.pageManager = this.createPageManager(url, super.pageRedirectHandler);
	}
	
	/**
	 * 現在アクティブになっているページの情報と、そのページが参照するIMG、SCRIPT、LINKタグが参照するページをデータストアへ保存します。<p/>
	 * 
	 * @throws CrawlerException          例外が発生し、「errorHandler」にて処理を停止すると判定された場合
	 * @throws BrowzingException         アクティブになっているページのドュメントオブジェクトの生成に失敗した場合
	 * @throws DataStoreManagerException データストアの操作に失敗した場合
	 */
	public void saveAll() throws CrawlerException, BrowzingException, DataStoreManagerException {
		jp.co.dk.crawler.Page activePage = (jp.co.dk.crawler.Page)this.getPage();
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
	 * @throws CrawlerException          例外が発生し、「errorHandler」にて処理を停止すると判定された場合
	 * @throws BrowzingException         アクティブになっているページのドュメントオブジェクトの生成に失敗した場合
	 * @throws DataStoreManagerException データストアの操作に失敗した場合
	 */
	public void saveImage() throws CrawlerException, BrowzingException, DataStoreManagerException {
		jp.co.dk.crawler.Page activePage = (jp.co.dk.crawler.Page)this.getPage();
		File file = activePage.getDocument();
		if (!(file instanceof HtmlDocument)) return;
		HtmlDocument htmlDocument = (HtmlDocument)file;
		List<Element> elements = htmlDocument.getElement(HtmlElementName.IMG);
		for (Element element : elements) {
			String url = null;
			try {
				jp.co.dk.browzer.html.element.Image castedElement = (jp.co.dk.browzer.html.element.Image)element;
				url = castedElement.getSrc();
				jp.co.dk.crawler.Page nextPage = (jp.co.dk.crawler.Page)this.move(castedElement);
				this.addLinks(activePage.getUrl(), nextPage.getUrl());
				nextPage.save();
				this.back();
			} catch (BrowzingException e) {
				this.errorHandler(activePage, new Url(url), e);
			}
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
	 * @throws CrawlerException          例外が発生し、「errorHandler」にて処理を停止すると判定された場合
	 * @throws BrowzingException         アクティブになっているページのドュメントオブジェクトの生成に失敗した場合
	 * @throws DataStoreManagerException データストアの操作に失敗した場合
	 */
	public void saveScript() throws CrawlerException, BrowzingException, DataStoreManagerException {
		jp.co.dk.crawler.Page activePage = (jp.co.dk.crawler.Page)this.getPage();
		File file = activePage.getDocument();
		if (!(file instanceof HtmlDocument)) return;
		HtmlDocument htmlDocument = (HtmlDocument)file;
		List<Element> elements = htmlDocument.getElement(new ElementSelector() {
			@Override
			public boolean judgment(Element element) {
				if (!(element instanceof HtmlElement)) return false;
				HtmlElement htmlElement = (HtmlElement)element;
				if (htmlElement.getElementType() == HtmlElementName.SCRIPT && htmlElement.hasAttribute("src")) return true;
				return false;
			}
		});
		for (Element element : elements) {
			String url = null;
			try {
				jp.co.dk.browzer.html.element.Script castedElement = (jp.co.dk.browzer.html.element.Script)element;
				url = castedElement.getSrc();
				jp.co.dk.crawler.Page nextPage = (jp.co.dk.crawler.Page)this.move(castedElement);
				this.addLinks(activePage.getUrl(), nextPage.getUrl());
				nextPage.save();
				this.back();
			} catch (BrowzingException e) {
				this.errorHandler(activePage, new Url(url), e);
			}
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
	 * @throws CrawlerException          例外が発生し、「errorHandler」にて処理を停止すると判定された場合
	 * @throws BrowzingException         アクティブになっているページのドュメントオブジェクトの生成に失敗した場合
	 * @throws DataStoreManagerException データストアの操作に失敗した場合
	 */
	public void saveLink() throws CrawlerException, BrowzingException, DataStoreManagerException {
		jp.co.dk.crawler.Page activePage = (jp.co.dk.crawler.Page)this.getPage();
		File file = activePage.getDocument();
		if (!(file instanceof HtmlDocument)) return;
		HtmlDocument htmlDocument = (HtmlDocument)file;
		List<Element> elements = htmlDocument.getElement(new ElementSelector() {
			@Override
			public boolean judgment(Element element) {
				if (!(element instanceof HtmlElement)) return false;
				HtmlElement htmlElement = (HtmlElement)element;
				if (htmlElement.getElementType() == HtmlElementName.LINK && htmlElement.hasAttribute("href")) return true;
				return false;
			}
		});
		for (Element element : elements) {
			String url = null;
			try {
				jp.co.dk.browzer.html.element.Link castedElement = (jp.co.dk.browzer.html.element.Link)element;
				url = castedElement.getHref();
				jp.co.dk.crawler.Page nextPage = (jp.co.dk.crawler.Page)this.move(castedElement);
				this.addLinks(activePage.getUrl(), nextPage.getUrl());
				nextPage.save();
				this.back();
			} catch (BrowzingException e) {
				this.errorHandler(activePage, new Url(url), e);
			}
		}
	}
	
	/**
	 * クローリング実行中に例外が発生した場合、その際のエラー処理方法を定義します。<p/>
	 * エラー発生後、本メソッドにてハンドリング処理を実施後、処理を継続させる場合、本メソッド内にて例外を送出させることなく、処理を完了させてください。
	 * エラー発生後、本メソッドにてハンドリング処理を実施後、処理を停止させる場合、本メソッド内にて例外を送出させてください。
	 * 
	 * @param throwable 発生した例外オブジェクト
	 * @throws CrawlerException エラーハンフドリング後、処理を停止させる場合
	 * @throws DataStoreManagerException データストア関連処理に失敗した場合
	 */
	protected void errorHandler (Page beforePage, Url nextPageUrl, Throwable throwable) throws CrawlerException, DataStoreManagerException {
		if (throwable instanceof CrawlerPageRedirectHandlerException) {
			CrawlerPageRedirectHandlerException crawlerPageRedirectHandlerException = (CrawlerPageRedirectHandlerException) throwable;
			jp.co.dk.crawler.Page errorPage = (jp.co.dk.crawler.Page)crawlerPageRedirectHandlerException.getPage();
			this.addLinks(beforePage.getUrl(), errorPage.getUrl());
			RedirectErrors errors = (RedirectErrors)this.dsm.getDataAccessObject(CrawlerDaoConstants.REDIRECT_ERRORS);
			long fileId = errorPage.getFileId();
			long timeId = errorPage.getTimeId();
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
	 * @throws DataStoreManagerException 登録処理に失敗した場合
	 * @throws CrawlerException 必須項目が不足している場合
	 */
	protected void addLinks(Url beforePageUrl, Url toPageUrl) throws DataStoreManagerException, CrawlerException {
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
		if (savedLinksRecord == null) {
			links.insert(from_protcol, from_host, from_path, from_parameter, to_protcol, to_host, to_path, to_parameter, createDate, updateDate);
		}
	}
	
	@Override
	protected PageManager createPageManager(String url, PageRedirectHandler handler) throws BrowzingException {
		return new CrawlerPageManager(this.dsm, url, handler);
	}
	
	@Override
	protected PageManager createPageManager(String url, PageRedirectHandler handler, int maxNestLevel) throws BrowzingException {
		return new CrawlerPageManager(this.dsm, url, handler, maxNestLevel);
	}
}

class CrawlerPageManager extends PageManager{
	
	protected DataStoreManager dsm;
	
	CrawlerPageManager(DataStoreManager dsm, String url, PageRedirectHandler pageRedirectHandler) throws BrowzingException {
		super(url, pageRedirectHandler);
		this.dsm = dsm;
		jp.co.dk.crawler.Page page = (jp.co.dk.crawler.Page)super.getPage(); // orz
		page.dataStoreManager = dsm;
	}
	
	CrawlerPageManager(DataStoreManager dsm, String url, PageRedirectHandler pageRedirectHandler, int maxNestLevel) throws BrowzingException {
		super(url, pageRedirectHandler, maxNestLevel);
		this.dsm = dsm;
		jp.co.dk.crawler.Page page = (jp.co.dk.crawler.Page)super.getPage();// orz
		page.dataStoreManager = dsm;
	}
	
	protected CrawlerPageManager(DataStoreManager dsm, PageManager parentPage, Page page,  PageRedirectHandler pageRedirectHandler, int nestLevel, int maxNestLevel){
		super(parentPage, page, pageRedirectHandler, nestLevel, maxNestLevel);
		this.dsm = dsm;
	}
	
	@Override
	public Page createPage(String url) throws BrowzingException {
		return new jp.co.dk.crawler.Page(url, this.dsm);
	}
	
	@Override
	protected PageManager createPageManager(PageManager pageManager, Page page, PageRedirectHandler pageRedirectHandler, int nextLevel, int maxNestLevel) {
		return new CrawlerPageManager(this.dsm, pageManager, page, pageRedirectHandler, nextLevel, maxNestLevel);
	}
}

class CrawlerPageRedirectHandler extends PageRedirectHandler {
	
	protected DataStoreManager dsm;
	
	CrawlerPageRedirectHandler(DataStoreManager dsm) throws CrawlerException {
		if (dsm == null) throw new CrawlerException(DETASTORETYPE_IS_NOT_SET);
		this.dsm = dsm;
	}
	
	@Override
	protected Page ceatePage(String url) throws BrowzingException {
		return new jp.co.dk.crawler.Page(url, this.dsm);
	}
	
	@Override
	protected Page redirectBy_SERVER_ERROR(ResponseHeader header, Page page) throws BrowzingException {
		try {
			return super.redirectBy_SERVER_ERROR(header, page);
		} catch (BrowzingException e) {
			throw new CrawlerPageRedirectHandlerException(e, (jp.co.dk.crawler.Page)page);
		}
		
	}
	
	@Override
	protected Page redirectBy_CLIENT_ERROR(ResponseHeader header, Page page) throws BrowzingException {
		try {
			return super.redirectBy_CLIENT_ERROR(header, page);
		} catch (BrowzingException e) {
			throw new CrawlerPageRedirectHandlerException(e, (jp.co.dk.crawler.Page)page);
		}
	}
}

