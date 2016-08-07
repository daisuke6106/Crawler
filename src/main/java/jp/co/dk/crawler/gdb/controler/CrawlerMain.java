package jp.co.dk.crawler.gdb.controler;

import java.util.List;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.browzer.scenario.action.MoveAction;
import jp.co.dk.crawler.AbstractCrawler;
import jp.co.dk.crawler.controler.AbstractCrawlerScenarioControler;
import jp.co.dk.crawler.exception.CrawlerInitException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.gdb.GCrawler;
import jp.co.dk.document.Element;
import jp.co.dk.document.File;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.HtmlElement;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerException;
import jp.co.dk.neo4jdatastoremanager.property.Neo4JDataStoreManagerProperty;
import jp.co.dk.property.exception.PropertyException;

public class CrawlerMain extends AbstractCrawlerScenarioControler {

	protected Neo4JDataStoreManager dsm;
	
	{
		try {
			this.dsm = new Neo4JDataStoreManager(new Neo4JDataStoreManagerProperty());
		} catch (Neo4JDataStoreManagerException | PropertyException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
	public static void main(String[] args) {
		new CrawlerMain().execute(args);
	}

	@Override
	public AbstractCrawler createBrowzer(String url) throws CrawlerInitException, PageIllegalArgumentException, PageAccessException {
		return new GCrawler(url, this.dsm);
	}

	@Override
	protected MoveAction createNoneMoveAction(String[] arguments) {
		return new MoveAction(){};
	}
	
	@Override
	protected MoveAction createPrintMoveAction(String[] arguments) {
		if (arguments.length == 0) {
			return new MoveAction(){
				public void beforeAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
					System.out.println("[BEFORE MOVE] NOW_URL=[" + browzer.getPage().getURL() + "] MOVE_TO=[" + movable.getUrl() + "]");
				}
				
				public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
					System.out.println("[AFTER  MOVE] NOW_URL=[" + browzer.getPage().getURL() + "]");
				}
				
				public void errorAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
					System.err.println("[   ERROR   ] NOW_URL=[" + browzer.getPage().getURL() + "]");
				}
			};
		} else {
			return new MoveAction(){
				public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
					try {
						File file = browzer.getPage().getDocument();
						if (file instanceof HtmlDocument) {
							HtmlDocument htmlDocument = (HtmlDocument)file;
							List<Element> htmlElementList = htmlDocument.getNode(arguments[0]);
							for (Element element : htmlElementList) { 
								HtmlElement htmlElement = (HtmlElement)element;
								System.out.println(htmlElement.getContent());
							}
							
						}
					} catch (PageAccessException | DocumentException e) {
						
					}
				}
			};
		}
	}

	@Override
	protected MoveAction createSaveMoveAction(String[] arguments) {
		return new MoveAction(){
			public void beforeAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
				
			}
			
			public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
				GCrawler gcrawler = (GCrawler)browzer;
				try {
					gcrawler.save();
				} catch (CrawlerSaveException e) {
					System.out.println(e.getMessage());
					System.exit(1);
				}
			}
			
			public void errorAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
				
			}
		};
	}

}

