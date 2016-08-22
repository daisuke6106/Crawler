package jp.co.dk.crawler.scenario.action;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.html.element.MovableElement;

public class PrintMoveAction extends MoveAction {

	public PrintMoveAction(String[] args) throws MoveActionFatalException {
		super(args);
	}

	@Override
	public void beforeAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		System.out.println("[BEFORE MOVE] NOW_URL=[" + browzer.getPage().getURL() + "] MOVE_TO=[" + movable.getUrl() + "]");
	}
	
	@Override
	public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		System.out.println("[AFTER  MOVE] NOW_URL=[" + browzer.getPage().getURL() + "]");
	}
	
	@Override
	public void errorAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		System.err.println("[   ERROR   ] NOW_URL=[" + browzer.getPage().getURL() + "]");
	}
}
