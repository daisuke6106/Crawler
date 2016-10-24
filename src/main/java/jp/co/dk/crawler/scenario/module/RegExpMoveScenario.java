package jp.co.dk.crawler.scenario.module;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.html.element.A;
import jp.co.dk.crawler.CrawlerPage;
import jp.co.dk.crawler.scenario.MoveScenario;
import jp.co.dk.crawler.scenario.MoveScenarioName;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.logger.Loggable;
import static jp.co.dk.crawler.message.CrawlerMessage.*;

/**
 * <p>RegExpMoveScenarioは、正規表現を基に、遷移先定義を行うシナリオクラスです。</p>
 * 
 * @version 1.0
 * @author D.Kanno
 */
@MoveScenarioName(name="reg")
public class RegExpMoveScenario extends MoveScenario {
	
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
	 * @param argumentList シナリオ引数
	 * @throws MoveActionFatalException 構文解析に失敗した場合
	 */
	public RegExpMoveScenario(String[] argumentList) throws MoveActionFatalException {
		super(argumentList);
		if (argumentList.length != 1) throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"引数の個数が不正です。", RegExpMoveScenario.class.toString()});
		this.urlPatternStr  = argumentList[0];
		if (this.urlPatternStr == null || this.urlPatternStr.equals("")) throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"URL(正規表現)が定義されていません。", this.urlPatternStr});
		try {
			this.urlPattern     = Pattern.compile(this.urlPatternStr);
		} catch (PatternSyntaxException e) {
			throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"URL(正規表現)が不正です。", this.urlPatternStr});
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
				this.logger.info(new Loggable(){
					@Override
					public String printLog(String lineSeparator) {
						return "- キューに追加済みのため、スキップ URL[" +  castedAnchor.getUrl() + "]";
					}});
			} else {
				this.logger.info(new Loggable(){
					@Override
					public String printLog(String lineSeparator) {
						return "- キューに未追加のため、追加します URL[" +  castedAnchor.getUrl() + "]";
					}});
				addedQueueUrlList.add(castedAnchor.getUrl());
				moveableElementList.add(castedAnchor);
			}
			
		}
		return moveableElementList;
	}
}
