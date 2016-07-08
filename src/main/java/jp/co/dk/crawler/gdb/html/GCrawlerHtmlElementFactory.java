package jp.co.dk.crawler.gdb.html;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.html.element.A;
import jp.co.dk.crawler.gdb.html.element.GCrawlerA;
import jp.co.dk.document.html.HtmlElement;

/**
 * HtmlElementFactoryは、ブラウザにて使用するHTML要素の生成を行うファクトリクラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class GCrawlerHtmlElementFactory extends jp.co.dk.browzer.html.HtmlElementFactory {
	
	public GCrawlerHtmlElementFactory(Page page) {
		super(page);
	}

	@Override
	protected A createAnchor(HtmlElement htmlElement) {
		return new GCrawlerA(htmlElement, this.page);
	}
}
