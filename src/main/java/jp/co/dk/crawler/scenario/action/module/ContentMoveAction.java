package jp.co.dk.crawler.scenario.action.module;

import java.util.List;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.scenario.action.MoveAction;
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
public class ContentMoveAction extends MoveAction {

	/**
	 * <p>コンストラクタ</p>
	 * 遷移時アクションの実行時に使用する引数を基にインスタンスを生成します。
	 *  
	 * @param args アクション時の引数
	 * @throws MoveActionFatalException 引数が不正な場合
	 */
	public ContentMoveAction(String[] args) throws MoveActionFatalException {
		super(args);
	}
	
	@Override
	public void afterAction(MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		try {
			File file = browzer.getPage().getDocument();
			if (file instanceof HtmlDocument) {
				HtmlDocument htmlDocument = (HtmlDocument)file;
				List<HtmlElement> htmlElementList = htmlDocument.getNode(args[0]);
				for (Element element : htmlElementList) { 
					HtmlElement htmlElement = (HtmlElement)element;
					System.out.println(htmlElement.getContent());
				}
				
			}
		} catch (PageAccessException | DocumentException e) {
			
		}
	}
}
