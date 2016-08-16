package jp.co.dk.crawler.scenario;

import java.util.List;
import java.util.regex.Pattern;

import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.html.element.A;
import jp.co.dk.crawler.AbstractCrawler;
import jp.co.dk.crawler.scenario.action.MoveAction;
import jp.co.dk.document.exception.DocumentException;

public class RegExpMoveScenario extends MoveScenario {
	
	protected String urlPatternStr;
	
	protected Pattern urlPattern;
	
	public RegExpMoveScenario(String urlPatternStr, Pattern urlPattern, List<MoveAction> moveActionList) {
		super(moveActionList);
		this.urlPatternStr = urlPatternStr;
		this.urlPattern    = urlPattern;
	}

	public String getUrlPattern() {
		return this.urlPatternStr;
	}
	
	@Override
	public String toString() {
		return this.urlPatternStr;
	}

	@Override
	public void addTask(AbstractCrawler abstractCrawler) throws MoveActionException, MoveActionFatalException {
		try {
			List<A> anchorList = abstractCrawler.getAnchor(this.urlPattern);
			for (A anchor : anchorList) {
				moveableQueue.add(new QueueTask(anchor, this.moveActionList));
			}
		} catch (PageAccessException | DocumentException e) {
			
		}
	}
}
