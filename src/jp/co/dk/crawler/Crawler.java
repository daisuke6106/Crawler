package jp.co.dk.crawler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.PageManager;
import jp.co.dk.browzer.PageRedirectHandler;
import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.Errors;
import jp.co.dk.crawler.dao.Links;
import jp.co.dk.crawler.dao.record.LinksRecord;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.Element;
import jp.co.dk.document.File;
import jp.co.dk.document.html.HtmlDocument;
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
		this.dsm = dataStoreManager;
		((CrawlerPageManager)super.pageManager).dsm = dataStoreManager;// orz
	}
	
	/**
	 * 現在アクティブになっているページオブジェクトをデータストアへ保存する。
	 * @throws DataStoreManagerException 
	 */
	public void save() throws CrawlerException {
		jp.co.dk.crawler.Page page = (jp.co.dk.crawler.Page)this.getPage();
		page.save();
	}
	
	public void saveImage() throws CrawlerException, BrowzingException, DataStoreManagerException {
		jp.co.dk.crawler.Page activePage = (jp.co.dk.crawler.Page)this.getPage();
		File file = activePage.getDocument();
		if (!(file instanceof HtmlDocument)) return;
		HtmlDocument htmlDocument = (HtmlDocument)file;
		List<Element> imageElements = htmlDocument.getElement(HtmlElementName.IMG);
		for (Element imageElement : imageElements) {
			try {
				jp.co.dk.crawler.Page nextPage = (jp.co.dk.crawler.Page)this.move((jp.co.dk.browzer.html.element.Image)imageElement);
				this.addLinks(activePage, nextPage);
				nextPage.save();
				this.back();
			} catch (BrowzingException e) {
				this.errorHandler(e);
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
	protected void errorHandler (Throwable throwable) throws CrawlerException, DataStoreManagerException {
		Errors errors = (Errors)this.dsm.getDataAccessObject(CrawlerDaoConstants.ERRORS);
		String message = throwable.getMessage();
		StackTraceElement[] stackTraceElements = throwable.getStackTrace();
		Date createDate = new Date();
		Date updateDate = new Date();
		errors.insert(fileId, timeId, message, stackTraceElements, createDate, updateDate);
		return ;
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
	protected void addLinks(Page beforePage, Page toPage) throws DataStoreManagerException, CrawlerException {
		Links links = (Links)this.dsm.getDataAccessObject(CrawlerDaoConstants.LINKS);
		String             from_protcol   = beforePage.getProtocol();
		String             from_host      = beforePage.getHost();
		List<String>       from_path      = beforePage.getPathList();
		String             from_filename  = beforePage.getFileName();
		Map<String,String> from_parameter = beforePage.getParameter();
		String             to_protcol     = toPage.getProtocol();
		String             to_host        = toPage.getHost();
		List<String>       to_path        = toPage.getPathList();
		String             to_filename    = toPage.getFileName();
		Map<String,String> to_parameter   = toPage.getParameter();
		Date createDate = new Date();
		Date updateDate = new Date();
		LinksRecord savedLinksRecord = links.select(from_protcol, from_host, from_path, from_filename, from_parameter, to_protcol, to_host, to_path, to_filename, to_parameter, createDate, updateDate);
		if (savedLinksRecord == null) {
			links.insert(from_protcol, from_host, from_path, from_filename, from_parameter, to_protcol, to_host, to_path, to_filename, to_parameter, createDate, updateDate);
		}
	}
	
	@Override
	protected Page ceatePage(String url) throws BrowzingException {
		return new jp.co.dk.crawler.Page(url, this.dsm);
	}
	
	@Override
	protected PageManager createPageManager(Page page, PageRedirectHandler handler) {
		return new CrawlerPageManager(this.dsm, page, handler);
	}
	
	@Override
	protected PageManager createPageManager(Page page, PageRedirectHandler handler, int maxNestLevel) {
		return new CrawlerPageManager(this.dsm, page, handler, maxNestLevel);
	}
}

class CrawlerPageManager extends PageManager{
	
	protected DataStoreManager dsm;
	
	CrawlerPageManager(DataStoreManager dsm, Page page, PageRedirectHandler pageRedirectHandler) {
		super(page, pageRedirectHandler);
		this.dsm = dsm;
	}
	
	CrawlerPageManager(DataStoreManager dsm, Page page, PageRedirectHandler pageRedirectHandler, int maxNestLevel) {
		super(page, pageRedirectHandler, maxNestLevel);
		this.dsm = dsm;
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
}

class ExceptionHandler {
	
	
	
}