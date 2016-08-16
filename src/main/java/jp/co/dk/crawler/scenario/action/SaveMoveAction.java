package jp.co.dk.crawler.scenario.action;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.AbstractCrawler;
import jp.co.dk.crawler.exception.CrawlerSaveException;

public class SaveMoveAction extends MoveAction {

	public SaveMoveAction(String[] args) {
		super(args);
	}

	@Override
	public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		AbstractCrawler crawler = (AbstractCrawler)browzer;
		try {
			crawler.save();
		} catch (CrawlerSaveException e) {
			throw new MoveActionException(null);
		}
	}
}