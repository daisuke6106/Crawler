package jp.co.dk.crawler.scenario.action;

import java.util.List;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.document.Element;
import jp.co.dk.document.File;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.HtmlElement;

public class ContentMoveAction extends MoveAction {

	public ContentMoveAction(String[] args) {
		super(args);
	}
	
	public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		try {
			File file = browzer.getPage().getDocument();
			if (file instanceof HtmlDocument) {
				HtmlDocument htmlDocument = (HtmlDocument)file;
				List<Element> htmlElementList = htmlDocument.getNode(args[0]);
				for (Element element : htmlElementList) { 
					HtmlElement htmlElement = (HtmlElement)element;
					System.out.println(htmlElement.getContent());
				}
				
			}
		} catch (PageAccessException | DocumentException e) {
			
		}
	}
}
