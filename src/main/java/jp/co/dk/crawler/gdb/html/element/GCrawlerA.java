package jp.co.dk.crawler.gdb.html.element;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.gdb.GPage;
import jp.co.dk.crawler.gdb.GUrl;
import jp.co.dk.document.html.HtmlElement;

public class GCrawlerA extends jp.co.dk.browzer.html.element.A {
	
	public GCrawlerA(HtmlElement element, Page page) {
		super(element, page);
	}

	@Override
	public GUrl getUrlObj() throws PageIllegalArgumentException {
		return new GUrl(this.getHref(), ((GPage)this.page).getDataStoreManager());
	}
	
}
