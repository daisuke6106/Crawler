package jp.co.dk.crawler.controler;

import java.util.ArrayList;
import java.util.List;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.Crawler;
import jp.co.dk.crawler.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.dao.CrawlerErrors;
import jp.co.dk.crawler.dao.Documents;
import jp.co.dk.crawler.dao.Links;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.crawler.dao.RedirectErrors;
import jp.co.dk.crawler.dao.Urls;
import jp.co.dk.crawler.exception.CrawlerException;
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

public class CrawlingControler {
	
	
	void control(int sleepTime, Crawler crawler) throws BrowzingException, CrawlerException, DataStoreManagerException, DocumentException, CrawlerSaveException {
		try {
			Thread.sleep(sleepTime);
		} catch (InterruptedException e) {}
		crawler.saveAll();
		List<Element> anchorElements = this.getAnchorElements(crawler.getPage());
		for (Element element : anchorElements) {
			if (!(element instanceof MovableElement)) continue;
			crawler.move((MovableElement)element);
			this.control(sleepTime, crawler);
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
	

//	public void crawl(String url, CrawlingControler crawlingRule) throws CrawlerException, BrowzingException, DataStoreManagerException {
//		DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty();
//		DataStoreManager dataStoreManager = new DataStoreManager(dataStoreManagerProperty);
//		dataStoreManager.startTrunsaction();
//		
//		try {
//			Crawler crawler = new Crawler(url, dataStoreManager);
//			crawler.save();
//			List<Element> refsElements = crawlingRule.getRefsElements(crawler.getPage());
//			for (Element element : refsElements) {
//				
//			}
//		} catch (CrawlerException | BrowzingException e) {
//			throw e;
//		}
//		dataStoreManager.finishTrunsaction();
//	}
//	
//	/**
//	 * 
//	 * @param url
//	 * @throws DataStoreManagerException
//	 * @throws CrawlerException
//	 * @throws BrowzingException
//	 */
//	public void save(String url) throws DataStoreManagerException, CrawlerException, BrowzingException {
//		DataStoreManagerProperty dataStoreManagerProperty = new DataStoreManagerProperty();
//		DataStoreManager dataStoreManager = new DataStoreManager(dataStoreManagerProperty);
//		dataStoreManager.startTrunsaction();
//		Crawler crawler = new Crawler(url, dataStoreManager);
//		crawler.saveAll();
//		dataStoreManager.finishTrunsaction();
//	}
	
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