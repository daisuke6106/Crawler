package jp.co.dk.crawler.scenario.module;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.html.element.A;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.CrawlerPage;
import jp.co.dk.crawler.scenario.ManualArgument;
import jp.co.dk.crawler.scenario.MoveScenario;
import jp.co.dk.crawler.scenario.MoveScenarioName;
import jp.co.dk.document.Element;
import jp.co.dk.document.ElementSelector;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.html.HtmlElement;
import jp.co.dk.logger.Loggable;
import static jp.co.dk.crawler.message.CrawlerMessage.*;

/**
 * <p>RegExpMoveScenarioは、正規表現を基に、遷移先定義を行うシナリオクラスです。</p>
 * 
 * @version 1.0
 * @author D.Kanno
 */
@MoveScenarioName(
	name           = "docreg",  
	manualTitle    = "指定要素内アンカー取得",
	manualText     = "指定要素内から指定のURLパターンを持つアンカーを取得し、遷移先とします。URLパターンには正規表現が指定出来ます。",
	manualArgument = {
		"要素を指定する文字列（タグ、ID、クラス）", "取得対象アンカーのURLパターン（正規表現記述）"
	},
	manualExample  = {
		"docreg('body','http://example.com/.+.html')と指定した場合、bodyタグの中に存在するアンカーの中からhrefに「http://example.com/.+.html」形式のURLを保持するアンカーを取得する。"
	}
)
public class DocRegExpMoveScenario extends MoveScenario {
	
	/** ドキュメント指定文字列 */
	protected String elementSelector;
	
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
	public DocRegExpMoveScenario(String[] argumentList) throws MoveActionFatalException {
		super(argumentList);
		if (argumentList.length != 2) throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"引数の個数が不正です。", DocRegExpMoveScenario.class.toString()});
		this.elementSelector = argumentList[0];
		this.urlPatternStr   = argumentList[1];
		if (this.elementSelector == null || this.elementSelector.equals("")) throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"要素指定文字列が定義されていません。", this.elementSelector});
		if (this.urlPatternStr   == null || this.urlPatternStr.equals("")  ) throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"URL(正規表現)が定義されていません。", this.urlPatternStr});
		try {
			this.urlPattern = Pattern.compile(this.urlPatternStr);
		} catch (PatternSyntaxException e) {
			throw new MoveActionFatalException(FAILE_TO_SCENARIO_GENERATION, new String[]{"URL(正規表現)が不正です。", this.urlPatternStr});
		}
	}
	
	
	@Override
	protected List<A> getMoveableElement(CrawlerPage page) throws MoveActionException, MoveActionFatalException {
		this.logger.info(new Loggable(){
			@Override
			public String printLog(String lineSeparator) {
				return "ページ=[" +  page.getURL() + "]からドキュメント=[" + elementSelector + "]に存在するパターン=[" + urlPatternStr + "]に合致するURLを取得します。";
			}});
		
		List<A> moveableElementList = new ArrayList<>();
		try {
			jp.co.dk.document.File file = page.getDocument();
			if (!(file instanceof jp.co.dk.document.html.HtmlDocument)) throw new MoveActionException(FAILE_TO_SCENARIO_EXECUTE, new String[]{"HTMLではありません。", DocRegExpMoveScenario.class.toString()});
			
			jp.co.dk.document.html.HtmlDocument htmlDocument = (jp.co.dk.document.html.HtmlDocument)file;
			List<HtmlElement> htmlElementList = htmlDocument.getNode(this.elementSelector);
			
			for (HtmlElement htmlElement : htmlElementList) {
				List<jp.co.dk.document.Element> elementList = htmlElement.getElement(new ElementSelector() {
					@Override
					public boolean judgment(Element element) {
						if (element instanceof MovableElement) {
							MovableElement movableElement = (MovableElement)element;
							return urlPattern.matcher(movableElement.getUrl()).find();
						}
						return false;
					}
				});
				this.logger.info(new Loggable(){
					@Override
					public String printLog(String lineSeparator) {
						return "- ページ=[" +  page.getURL() + "]からドキュメント=[" + elementSelector + "]に存在するパターン=[" + urlPatternStr + "]に合致するURLを" + elementList.size() + "個取得しました。";
					}});
				for (jp.co.dk.document.Element element : elementList) {
					MovableElement movableElement = (MovableElement)element;
					if (addedQueueUrlList.contains(movableElement.getUrl())) {
						this.logger.info(new Loggable(){
							@Override
							public String printLog(String lineSeparator) {
								return "- キューに追加済みのため、スキップ URL[" +  movableElement.getUrl() + "]";
							}});
					} else {
						this.logger.info(new Loggable(){
							@Override
							public String printLog(String lineSeparator) {
								return "- キューに未追加のため、追加します URL[" +  movableElement.getUrl() + "]";
							}});
						addedQueueUrlList.add(movableElement.getUrl());
						moveableElementList.add(((A)element));
					}
				}
			}
		} catch (PageAccessException | DocumentException e) {
			throw new MoveActionException(FAILE_TO_SCENARIO_EXECUTE, new String[]{"ページデータの取得に失敗しました。", DocRegExpMoveScenario.class.toString()}, e);
		}
		return moveableElementList;
	}

	@Override
	public String manualTitle() {
		return "";
	}


	@Override
	public String manualText(String lineseparater) {
		return "HTMLから指定の要素を選択し、その中から正規表現に合致したアンカー一覧を取得する。";
	}


	@Override
	public ManualArgument[] getManualArgument() {
		// TODO Auto-generated method stub
		return null;
	}
}
