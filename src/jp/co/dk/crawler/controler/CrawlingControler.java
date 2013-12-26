package jp.co.dk.crawler.controler;

import java.util.ArrayList;
import java.util.List;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MoveAction;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.Crawler;
import jp.co.dk.crawler.MoveInfo;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.document.Element;
import jp.co.dk.document.ElementSelector;
import jp.co.dk.document.File;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.HtmlElement;
import jp.co.dk.document.html.constant.HtmlElementName;

public class CrawlingControler {
	
	void control(int sleepTime, Crawler crawler) throws BrowzingException, CrawlerException, DataStoreManagerException {
		List<Element> refsElements = this.getRefsElements(crawler.getPage());
		for (Element element : refsElements) {
			if (!(element instanceof MovableElement)) continue;
			MovableElement movableElement = (MovableElement)element;
			MoveInfo moveInfo = crawler.moveWithSave(movableElement);
			// 遷移していた場合、元のページに戻ります。
			if (MoveInfo.MOVED == moveInfo) crawler.back();
		}
		
		List<Element> anchorElements = this.getRefsElements(crawler.getPage());
		for (Element element : anchorElements) {
			if (!(element instanceof MovableElement)) continue;
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
	 */
	List<Element> getRefsElements(Page page) throws BrowzingException {
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
	 */
	List<Element> getAnchorElements(Page page) throws BrowzingException {
		List<Element> elementList = new ArrayList<Element>();
		File file = page.getDocument();
		if (file instanceof HtmlDocument) return elementList;
		HtmlDocument htmlDocument = (HtmlDocument)file;
		List<Element> anchors = htmlDocument.getElement(new AnchorElementSelector());
		elementList.addAll(anchors);
		return elementList;
	}
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