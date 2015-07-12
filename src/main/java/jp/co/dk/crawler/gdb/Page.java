package jp.co.dk.crawler.gdb;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.Url;
import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.AbstractPage;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.crawler.rdb.dao.CrawlerDaoConstants;
import jp.co.dk.crawler.rdb.dao.Documents;
import jp.co.dk.crawler.rdb.dao.Pages;
import jp.co.dk.crawler.rdb.dao.Urls;
import jp.co.dk.crawler.rdb.dao.record.DocumentsRecord;
import jp.co.dk.datastoremanager.DaoConstants;
import jp.co.dk.datastoremanager.DataAccessObjectFactory;
import jp.co.dk.datastoremanager.DataStoreManager;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;
import jp.co.dk.datastoremanager.gdb.AbstractDataBaseAccessObject;
import jp.co.dk.datastoremanager.gdb.Cypher;
import jp.co.dk.document.exception.DocumentFatalException;
import static jp.co.dk.crawler.message.CrawlerMessage.*;

/**
 * PAGEはクローラにて使用される単一のページを表すクラスです。<p/>
 * 本クラスにて接続先のページ情報のデータストアへの保存、データストアからの読み込みを行います。<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class Page extends AbstractPage {
	
	/** アクセス日付フォーマット */
	protected SimpleDateFormat accessDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	/**
	 * コンストラクタ<p/>
	 * 指定のURL、データストアマネージャのインスタンスを元に、ページオブジェクトのインスタンスを生成します。
	 * 
	 * @param url              URL文字列
	 * @param dataStoreManager データストアマネージャ
	 * @throws PageIllegalArgumentException URLが指定されていない、不正なURLが指定されていた場合
	 * @throws PageAccessException ページにアクセスした際にサーバが存在しない、ヘッダが不正、データの取得に失敗した場合
	 */
	public Page(String url, DataStoreManager dataStoreManager) throws PageIllegalArgumentException, PageAccessException {
		super(url, dataStoreManager);
	}

	@Override
	public boolean save() throws CrawlerSaveException {
		try {
			jp.co.dk.datastoremanager.gdb.AbstractDataBaseAccessObject dataStore = (jp.co.dk.datastoremanager.gdb.AbstractDataBaseAccessObject)this.dataStoreManager.getDataAccessObject("PAGE");
			Cypher pageData = new Cypher("CREATE(PAGE{url:{1},accessdate:{2},sha256:{3},data:{4}})");
			pageData.setParameter(this.url.toString());
			pageData.setParameter(this.accessDateFormat.format(this.getCreateDate()));
			pageData.setParameter(this.getData().getHash());
			pageData.setParameter(this.getData().getBytesToBase64String());
			dataStore.execute(pageData);
		} catch (ClassCastException | DataStoreManagerException e) {
			throw new CrawlerSaveException(DATASTOREMANAGER_CAN_NOT_CREATE);
		} catch (PageAccessException e) {
			throw new CrawlerSaveException(FAILE_TO_GET_PAGE, this.url.toString());
		}
		return false;
	}

	@Override
	public boolean isSaved() throws CrawlerSaveException {
		try {
			jp.co.dk.datastoremanager.gdb.AbstractDataBaseAccessObject dataStore = (jp.co.dk.datastoremanager.gdb.AbstractDataBaseAccessObject)this.dataStoreManager.getDataAccessObject("PAGE");
			Cypher pageData = new Cypher("MATCH(page:PAGE{url:{1},accessdate:{2},sha256:{3},data:{4}})");
			pageData.setParameter(this.url.toString());
			pageData.setParameter(this.accessDateFormat.format(this.getCreateDate()));
			pageData.setParameter(this.getData().getHash());
			pageData.setParameter(this.getData().getBytesToBase64String());
			dataStore.execute(pageData);
		} catch (ClassCastException | DataStoreManagerException e) {
			throw new CrawlerSaveException(DATASTOREMANAGER_CAN_NOT_CREATE);
		} catch (PageAccessException e) {
			throw new CrawlerSaveException(FAILE_TO_GET_PAGE, this.url.toString());
		}
		return false;
	}

	@Override
	public boolean isLatest() throws CrawlerSaveException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Url createUrl(String url) throws PageIllegalArgumentException {
		return new jp.co.dk.crawler.gdb.Url(url, this.dataStoreManager);
	}
	
}


