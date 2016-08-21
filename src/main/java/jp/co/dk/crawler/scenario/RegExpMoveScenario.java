package jp.co.dk.crawler.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.html.element.A;
import jp.co.dk.crawler.AbstractCrawler;
import jp.co.dk.crawler.AbstractPage;
import jp.co.dk.crawler.scenario.action.MoveAction;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.logger.Loggable;

public class RegExpMoveScenario extends MoveScenario {
	
	protected String urlPatternStr;
	
	protected Pattern urlPattern;
	
	/** キューに追加済みのURL一覧 */
	protected List<String> addedQueueUrlList = new ArrayList<>();
	
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
	public void start(AbstractCrawler abstractCrawler, long interval) throws MoveActionException, MoveActionFatalException {
		// 初回に限り現在のページ情報からシナリオにタスクを追加する
		this.addTask((AbstractPage)abstractCrawler.getPage());
		// クローリングを開始する。
		this.crawl(abstractCrawler, interval);
	}
	
	@Override
	public void crawl(AbstractCrawler abstractCrawler, long interval) throws MoveActionException, MoveActionFatalException {
		while(this.hasTask()) {
			QueueTask queueTask = this.popTask();
			abstractCrawler.change(queueTask.getMovableElement().getPage());
			MoveResult moveResult = abstractCrawler.move(queueTask);
			switch (moveResult) {
				// 遷移に成功した場合
				case SuccessfullTransition :
					// 遷移のページにて本シナリオ内にある
					this.addTask(abstractCrawler.getPage());
					if (this.hasChildScenario()) {
						this.getChildScenario().crawl(abstractCrawler, interval);
					} else {
						for (jp.co.dk.browzer.html.element.A anchor : (List<jp.co.dk.browzer.html.element.A>)this.getMoveableElement((AbstractPage)abstractCrawler.getPage())) {
							this.moveableQueue.add(this.createTask(anchor, this.moveActionList));
						}
					}
					abstractCrawler.back();
					break;

				// 遷移に失敗した場合
				case FailureToTransition :
					break;
					
				// 遷移が許可されなかった場合
				case UnAuthorizedTransition :
					break;
			}
			try {
				Thread.sleep(interval * 1000);
			} catch (InterruptedException e) {}
		}
	}
	
	@Override
	protected List<A> getMoveableElement(AbstractPage page) throws MoveActionException, MoveActionFatalException {
		this.logger.info(new Loggable(){
			@Override
			public String printLog(String lineSeparator) {
				return "ページ=[" +  page.getURL() + "]からパターン=[" + urlPatternStr + "]に合致するURLを取得します。";
			}});
		List<A> moveableElementList = new ArrayList<>();
		List<jp.co.dk.document.html.element.A> anchorList;
		try {
			anchorList = page.getAnchor(this.urlPattern);
		} catch (PageAccessException | DocumentException e) {
			return moveableElementList;
		}
		this.logger.info(new Loggable(){
			@Override
			public String printLog(String lineSeparator) {
				return "- ページ=[" +  page.getURL() + "]からパターン=[" + urlPatternStr + "]に合致するURLを [" + anchorList.size() + "] 個抽出しました。";
			}});
		for (jp.co.dk.document.html.element.A anchor : anchorList) {
			A castedAnchor = (A)anchor;
			if (this.addedQueueUrlList.contains(castedAnchor.getUrl())) {
				this.logger.info(new Loggable(){
					@Override
					public String printLog(String lineSeparator) {
						return "- キューに追加済みのため、スキップ URL[" +  castedAnchor.getUrl() + "]";
					}});
			} else {
				this.logger.info(new Loggable(){
					@Override
					public String printLog(String lineSeparator) {
						return "- キューに未追加のため、追加します URL[" +  castedAnchor.getUrl() + "]。";
					}});
				addedQueueUrlList.add(castedAnchor.getUrl());
				moveableElementList.add(castedAnchor);
			}
			
		}
		return moveableElementList;
	}

	
}
