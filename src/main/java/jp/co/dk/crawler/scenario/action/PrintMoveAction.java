package jp.co.dk.crawler.scenario.action;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.html.element.MovableElement;

public class PrintMoveAction extends MoveAction {

	public PrintMoveAction(String[] args) {
		super(args);
	}

	@Override
	public void beforeAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		
	}
	
	@Override
	public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		System.err.println("[SUCCESS]:URL=" + movable.getUrl() + ", TITLE=" + browzer.getPage().getSize());
	}
	
	@Override
	public void errorAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		System.err.println("[ ERROR ]:" + movable.getUrl());
	}
	
}
