package jp.co.dk.crawler.scenario;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.html.element.A;
import jp.co.dk.crawler.Crawler;
import jp.co.dk.crawler.CrawlerPage;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.logger.Loggable;

/**
 * <p>RegExpMoveScenarioは、正規表現を基に、遷移先定義を行うシナリオクラスです。</p>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class RegExpMoveScenario extends MoveScenario {
	
	/** 遷移先、遷移先でのイベント定義 */
	protected Pattern scenarioPattern = Pattern.compile("^(.+?)@(.+?)$");
	
	/** 遷移先定義（正規表現）文字列 */
	protected String urlPatternStr;
	
	/** 遷移先定義（正規表現） */
	protected Pattern urlPattern;
	
	/** キューに追加済みのURL一覧 */
	protected List<String> addedQueueUrlList = new ArrayList<>();
	
	/**
	 * <p>コンストラクタ</p>
	 * シナリオを表す文字列を基に、シナリオを表すインスタンスを生成します。
	 * 
	 * @param scenarioStr シナリオを表す文字列
	 * @throws MoveActionFatalException 構文解析に失敗した場合
	 */
	public RegExpMoveScenario(String scenarioStr) throws MoveActionFatalException {
		super(scenarioStr);
		Matcher scenarioMatcher = this.scenarioPattern.matcher(scenarioStr);
		if (scenarioMatcher.find()) {
			String urlPatternStr = scenarioMatcher.group(1);
			if (urlPatternStr == null || urlPatternStr.equals("")) throw new MoveActionFatalException(null);
			String actionStr     = scenarioMatcher.group(2);
			if (actionStr     == null || actionStr.equals("")) throw new MoveActionFatalException(null);
			try {
				this.urlPatternStr = urlPatternStr;
				this.urlPattern = Pattern.compile(urlPatternStr);
			} catch (PatternSyntaxException e) {
				throw new MoveActionFatalException(null);
			}
			this.moveActionList = this.createMoveActionList(actionStr);
		} else {
			try {
				this.urlPatternStr = scenarioStr;
				this.urlPattern = Pattern.compile(scenarioStr);
				this.moveActionList = new ArrayList<>();
			} catch (PatternSyntaxException e) {
				throw new MoveActionFatalException(null);
			}
		}
	}
	
	@Override
	public void start(Crawler crawler, long interval) throws MoveActionException, MoveActionFatalException {
		this.addTask((CrawlerPage)crawler.getPage());
		this.crawl(crawler, interval);
	}
	
	@Override
	public void crawl(Crawler crawler, long interval) throws MoveActionException, MoveActionFatalException {
		while(this.hasTask()) {
			QueueTask queueTask = this.popTask();
			crawler.change(queueTask.getMovableElement().getPage());
			MoveResult moveResult = crawler.move(queueTask);
			switch (moveResult) {
				// 遷移に成功した場合
				case SuccessfullTransition :
					if (this.hasChildScenario()) {
						this.addTask((CrawlerPage)crawler.getPage());
						this.getChildScenario().addTask((CrawlerPage)crawler.getPage());
						this.getChildScenario().crawl(crawler, interval);
					}
					crawler.back();
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
	protected List<A> getMoveableElement(CrawlerPage page) throws MoveActionException, MoveActionFatalException {
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
//				this.logger.info(new Loggable(){
//					@Override
//					public String printLog(String lineSeparator) {
//						return "- キューに追加済みのため、スキップ URL[" +  castedAnchor.getUrl() + "]";
//					}});
			} else {
//				this.logger.info(new Loggable(){
//					@Override
//					public String printLog(String lineSeparator) {
//						return "- キューに未追加のため、追加します URL[" +  castedAnchor.getUrl() + "]。";
//					}});
				addedQueueUrlList.add(castedAnchor.getUrl());
				moveableElementList.add(castedAnchor);
			}
			
		}
		return moveableElementList;
	}
}
