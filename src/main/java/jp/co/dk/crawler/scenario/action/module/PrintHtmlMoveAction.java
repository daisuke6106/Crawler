package jp.co.dk.crawler.scenario.action.module;

import static jp.co.dk.crawler.message.CrawlerMessage.FAILE_TO_MOVEACTION_GENERATION;

import java.util.List;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.scenario.action.MoveAction;
import jp.co.dk.crawler.scenario.action.MoveActionName;
import jp.co.dk.document.Element;
import jp.co.dk.document.File;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.HtmlElement;

/**
 * ContentMoveActionは、指定のページの該当のタグの要素内容を標準出力に表示します。
 * 
 * @version 1.0
 * @author D.Kanno
 */
@MoveActionName(
	name           = "print_htmlelement",  
	manualTitle    = "HTML要素出力",
	manualText     = "HTMLから指定の要素を抽出し標準出力に出力します。ページがHTML以外の場合、標準出力に出力しない。",
	manualArgument = {"要素指定文字列"},
	manualExample  = {
		"print_htmlelement('body a')を指定した場合、bodyタグ内に存在するaタグを抽出し出力します。",
		"print_htmlelement('.classname a')を指定した場合、class=\"classname\"が設定されているタグ内に存在するaタグを抽出し出力します。",
		"print_htmlelement('#idname a')を指定した場合、id=\"idname\"タグ内に存在するaタグを抽出し出力します。"
	}
)
public class PrintHtmlMoveAction extends MoveAction {

	/** 要素指定文字列 */
	protected String tagStr;
	
	/**
	 * <p>コンストラクタ</p>
	 * 遷移時アクションの実行時に使用する引数を基にインスタンスを生成します。
	 *  
	 * @param args アクション時の引数
	 * @throws MoveActionFatalException 引数が不正な場合
	 */
	public PrintHtmlMoveAction(String[] args) throws MoveActionFatalException {
		super(args);
		if (args.length != 1) throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"要素指定文字列が設定されていません。", args.toString()});
		this.tagStr = this.args[0];
	}
	
	@Override
	public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		try {
			File file = browzer.getPage().getDocument();
			if (file instanceof HtmlDocument) {
				HtmlDocument htmlDocument = (HtmlDocument)file;
				List<HtmlElement> hitElements = htmlDocument.getNode(this.tagStr);
				for (Element element : hitElements) { 
					HtmlElement htmlElement = (HtmlElement)element;
					System.out.println(htmlElement.toString());
				}
				
			}
		} catch (PageAccessException | DocumentException e) {
			
		}
	}
}
