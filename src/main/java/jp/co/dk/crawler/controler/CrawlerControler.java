package jp.co.dk.crawler.controler;

import java.util.List;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageMovableLimitException;
import jp.co.dk.browzer.exception.PageRedirectException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.rdb.Crawler;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.html.element.A;

public class CrawlerControler {
	protected Crawler crawler;
	
	CrawlerControler(Crawler crawler) {
		this.crawler = crawler;
	}
	
	void crawl() throws PageAccessException, DocumentException, CrawlerException, CrawlerSaveException, DataStoreManagerException, PageIllegalArgumentException, PageRedirectException, PageMovableLimitException {
		this.crawler.saveAll();
		this.crawler.getDataStoreManager().commit();
		List<A> excludeHtmlAnchorList = this.crawler.getPage().getAnchorExcludeHtml();
		for (A anchor : excludeHtmlAnchorList) {
			this.crawler.move((MovableElement) anchor);
			this.crawler.saveAll();
			this.crawler.getDataStoreManager().commit();
			this.crawler.back();
		}
	}
}
