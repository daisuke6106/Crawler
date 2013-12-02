package jp.co.dk.crawler.controler;

import java.util.ArrayList;
import java.util.List;

import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.document.Element;
import jp.co.dk.document.ElementSelector;
import jp.co.dk.document.File;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.HtmlElement;
import jp.co.dk.document.html.constant.HtmlElementName;

public class CrawlingRule {
	
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

class AnchorElementSelector implements ElementSelector {
	@Override
	public boolean judgment(Element element) {
		if (!(element instanceof HtmlElement)) return false;
		HtmlElement htmlElement = (HtmlElement)element;
		if (htmlElement.getElementType() == HtmlElementName.A && htmlElement.hasAttribute("href")) return true;
		return false;
	}
	
}

class LinkElementSelector implements ElementSelector {
	@Override
	public boolean judgment(Element element) {
		if (!(element instanceof HtmlElement)) return false;
		HtmlElement htmlElement = (HtmlElement)element;
		if (htmlElement.getElementType() == HtmlElementName.LINK && htmlElement.hasAttribute("href")) return true;
		return false;
	}
}

class ScriptElementSelector implements ElementSelector {
	@Override
	public boolean judgment(Element element) {
		if (!(element instanceof HtmlElement)) return false;
		HtmlElement htmlElement = (HtmlElement)element;
		if (htmlElement.getElementType() == HtmlElementName.SCRIPT && htmlElement.hasAttribute("src")) return true;
		return false;
	}
}

class ImageElementSelector implements ElementSelector {
	@Override
	public boolean judgment(Element element) {
		if (!(element instanceof HtmlElement)) return false;
		HtmlElement htmlElement = (HtmlElement)element;
		if (htmlElement.getElementType() == HtmlElementName.IMG && htmlElement.hasAttribute("src")) return true;
		return false;
	}
	
}