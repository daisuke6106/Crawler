package jp.co.dk.crawler.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.browzer.scenario.action.MoveAction;
import jp.co.dk.document.exception.DocumentException;

public class RegExpAllMoveScenario extends RegExpMoveScenario {
	
	protected List<MovableElement> foundUrlPatternElement = new ArrayList<>();
	
	public RegExpAllMoveScenario(String urlPatternStr, Pattern urlPattern, List<MoveAction> moveActionList) {
		super(urlPatternStr, urlPattern, moveActionList);
	}
	
	@Override
	public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		super.afterAction(movable, browzer);
		try {
			this.foundUrlPatternElement.addAll(browzer.getAnchor(this.urlPattern));
		} catch (PageAccessException | DocumentException e) {
		}
	}
	
	@Override
	public void afterScenario(Browzer browzer)  throws MoveActionException, MoveActionFatalException {
		
	}

}
