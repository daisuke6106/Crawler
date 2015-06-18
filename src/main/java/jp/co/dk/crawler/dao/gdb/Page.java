package jp.co.dk.crawler.dao.gdb;

import org.neo4j.graphdb.GraphDatabaseService;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.AbstractPage;
import jp.co.dk.crawler.exception.CrawlerSaveException;

public class Page extends AbstractPage {

	/** グラフデータベースサービス */
	protected GraphDatabaseService graphDB;
	
	/**
	 * コンストラクタ<p/>
	 * 指定のURL、グラフデータベースサービスのインスタンスを元に、ページオブジェクトのインスタンスを生成します。
	 * 
	 * @param url     URL文字列
	 * @param graphDB データストアマネージャ
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	public Page(String url, GraphDatabaseService graphDB) throws PageIllegalArgumentException, PageAccessException {
		super(url);
		this.graphDB = graphDB;
	}
	
	@Override
	protected Url createUrl(String url) throws PageIllegalArgumentException {
		return new Url(url, graphDB);
	}

	@Override
	public boolean save() throws CrawlerSaveException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaved() throws CrawlerSaveException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isLatest() throws CrawlerSaveException {
		// TODO Auto-generated method stub
		return false;
	}
}