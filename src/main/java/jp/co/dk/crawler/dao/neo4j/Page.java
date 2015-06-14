package jp.co.dk.crawler.dao.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;

public class Page extends jp.co.dk.browzer.Page {

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
}