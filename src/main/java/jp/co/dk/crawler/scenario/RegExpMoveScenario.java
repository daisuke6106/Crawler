package jp.co.dk.crawler.scenario;

import java.util.List;
import java.util.regex.Pattern;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.html.element.A;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.browzer.scenario.MoveControl;
import jp.co.dk.browzer.scenario.MoveScenario;
import jp.co.dk.browzer.scenario.action.MoveAction;
import jp.co.dk.crawler.AbstractCrawler;
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
	public List<A> getMoveAnchor(Browzer browzer) throws PageAccessException, DocumentException {
		return browzer.getAnchor(this.urlPattern);
	}
	
	@Override
	public MoveControl beforeAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		super.beforeAction(movable, browzer);
		AbstractCrawler crawler = (AbstractCrawler)browzer;
		if (crawler.isVisited(movable)) {
			return MoveControl.Interruption;
		} else {
			return MoveControl.Continuation;
		}
	}
	
	@Override
	public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		super.afterAction(movable, browzer);
		
	}
	
	@Override
	public void errorAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		super.errorAction(movable, browzer);
	}
	
	@Override
	public String toString() {
		return this.urlPatternStr;
	}
}
