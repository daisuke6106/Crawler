package jp.co.dk.crawler.action.module;


import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jp.co.dk.browzer.Browzer;
import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.Url;
import jp.co.dk.browzer.exception.MoveActionException;
import jp.co.dk.browzer.exception.MoveActionFatalException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageSaveException;
import jp.co.dk.browzer.html.element.MovableElement;
import jp.co.dk.crawler.action.MoveActionName;
import jp.co.dk.crawler.message.CrawlerMessage;
import jp.co.dk.document.Element;
import jp.co.dk.document.ElementSelector;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.HtmlElement;
import jp.co.dk.document.html.constant.HtmlElementName;
import jp.co.dk.logger.Loggable;
import static jp.co.dk.crawler.message.CrawlerMessage.*;

/**
 * FileSaveMoveActionは、指定のページを指定されたディレクトリに指定の名称で保存します。
 * 
 * @version 1.0
 * @author D.Kanno
 */
@MoveActionName(
		name           = "file_save_full",  
		manualTitle    = "ページデータ保存（全情報）",
		manualText     = "ページに関する全情報を指定の基底ディレクトリに指定フォーマットのディレクトリを作成し、その中に保存します。",
		manualArgument = {
			"基底ディレクトリへのパス", 
			"ディレクトリ名（%title=HTMLのタイトル、%date{yyyyMMddHHmmss}=日付、{X}内には日付フォーマット、%index{X}=連番、{X}内には桁数）"
		},
		manualExample  = {"file_save_full('/tmp/,'%date{yyyyMMdd_HHmmss}_%index{10}')と指定した場合、/tmp/ディレクトリに「20161201_235959_0000000010」というディレクトリが作成され、その中にページ情報を保存します。"}
	)
public class FileSaveFullMoveAction extends AbstractFileSaveFullMoveAction {
	
	private boolean isOverride = true;
	
	/**
	 * <p>コンストラクタ</p>
	 * 遷移時アクションの実行時に使用する引数を基にインスタンスを生成します。<br/>
	 * 引数[0]=保存先ディレクトリ<br/>
	 * 引数[1]=ファイル名<br/>
	 *  
	 * @param args アクション時の引数
	 * @throws MoveActionFatalException 引数が不正な場合
	 */
	public FileSaveFullMoveAction(String[] args) throws MoveActionFatalException {
		super(args);
		if (args.length != 2) throw new MoveActionFatalException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"保存先ディレクトリとファイルフォーマットが指定されていません。", args.toString()});
		this.setDirBase(args[0]);
		this.setDir(args[1]);
	}
	
	@Override
	protected void save(File dirbase, String dir, MovableElement movable, Browzer browzer) throws MoveActionException, MoveActionFatalException {
		java.io.File path = new java.io.File(new StringBuilder(dirbase.getAbsolutePath()).append('/').append(dir).toString());
		if (path.exists()) {
			this.logger.warn(new Loggable() {
				@Override
				public String printLog(String lineSeparator) {
					return CrawlerMessage.ERROR_FILE_APLREADY_EXISTS_IN_THE_SPECIFIED_PATH.getMessage(path.getAbsolutePath());
				}
			});
			return;
		}
		if (!path.mkdirs()) throw new MoveActionException(CrawlerMessage.ERROR_FAILE_TO_CREATE_DIR, path.getAbsolutePath());
		
		// ページオブジェクトを取得
		Page page = browzer.getPage();
		
		// URLを保存
		StringBuilder urlData = new StringBuilder();
		urlData.append("URL=").append(page.getURL()).append(System.lineSeparator());
		urlData.append("URL_HASH=").append(page.getURL().hashCode()).append(System.lineSeparator());
		urlData.append("PROTOCOL=").append(page.getProtocol().toString()).append(System.lineSeparator());
		urlData.append("HOST=").append(page.getHost().toString()).append(System.lineSeparator());
		urlData.append("PATH=").append(page.getPath().toString()).append(System.lineSeparator());
		urlData.append("FILENAME=").append(page.getFileName().toString()).append(System.lineSeparator());
		urlData.append("PARAMETER=").append(page.getParameter().toString()).append(System.lineSeparator());
		this.save(path, "url", urlData.toString().getBytes());
		
		// ヒストリーを保存
		StringBuilder historyUrlList = new StringBuilder();
		for (Url url : browzer.getHistoryUrlList()) historyUrlList.append(url.toString()).append(System.lineSeparator());
		this.save(path, "history", historyUrlList.toString().getBytes());
		
		// リクエストヘッダを保存
		this.save(path, "request_header", browzer.getPage().getRequestHeader().toString().getBytes());

		// レスポンスヘッダを保存
		this.save(path, "response_header", browzer.getPage().getResponseHeader().toString().getBytes());
		
		// データを保存
		try {
			browzer.save(path, "data");
		} catch (PageAccessException | PageSaveException | DocumentException e) {
			throw new MoveActionException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"保存に失敗しました。", "data"});
		}

		// リンク一覧を保存
		try {
			if (browzer.getPage().getDocument() instanceof HtmlDocument) {
				HtmlDocument htmlDocument = (HtmlDocument) browzer.getPage().getDocument();
				Set<String> anchorList = new HashSet<String>();
				List<Element> anchorElementList = htmlDocument.getElement(new ElementSelector() {
					@Override
					public boolean judgment(Element element) {
						if (!(element instanceof HtmlElement)) return false;
						HtmlElement htmlElement = (HtmlElement)element;
						if (htmlElement.getElementType() == HtmlElementName.A && htmlElement.hasAttribute("href") && !htmlElement.getAttribute("href").equals("")) return true;
						return false;
					}
				});
				for (Element anchorElement : anchorElementList) {
					HtmlElement anchorHtmlElement = (HtmlElement)anchorElement;
					anchorList.add(anchorHtmlElement.getAttribute("href"));
				}
				StringBuilder linkList = new StringBuilder();
				for (String url : anchorList) linkList.append(url).append(System.lineSeparator());
				this.save(path, "link", linkList.toString().getBytes());
			}
		} catch (PageAccessException | DocumentException e) {
			throw new MoveActionException(FAILE_TO_MOVEACTION_GENERATION, new String[]{"保存に失敗しました。", "anchor"});
		}
		
		// ログに出力する。
		this.logger.info(new Loggable() {
			@Override
			public String printLog(String lineSeparator) {
				return CrawlerMessage.SUCCESS_TO_SAVE_FILE.getMessage(path.getAbsolutePath());
			}
		});
		
		// ページ保存が完了した為、メモリからその情報をクリアします。
		page.clearData();
	}
}
