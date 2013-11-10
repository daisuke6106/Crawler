package jp.co.dk.crawler;

import java.util.List;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.PageManager;
import jp.co.dk.browzer.PageRedirectHandler;
import jp.co.dk.browzer.exception.BrowzingException;
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
	
	public void saveImage() throws CrawlerException, BrowzingException {
		jp.co.dk.crawler.Page activePage = (jp.co.dk.crawler.Page)this.getPage();
		File file = activePage.getDocument();
		if (!(file instanceof HtmlDocument)) return;
		HtmlDocument htmlDocument = (HtmlDocument)file;
		List<Element> imageElements = htmlDocument.getElement(HtmlElementName.IMG);
		for (Element imageElement : imageElements) {
			this.move((jp.co.dk.browzer.html.element.Image)imageElement);
			this.save();
			this.back();
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