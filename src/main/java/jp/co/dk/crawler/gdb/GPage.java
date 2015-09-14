package jp.co.dk.crawler.gdb;

import java.text.SimpleDateFormat;

import jp.co.dk.browzer.Url;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.AbstractPage;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStore;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.Node;
import jp.co.dk.neo4jdatastoremanager.cypher.Cypher;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerCypherException;
import static jp.co.dk.crawler.message.CrawlerMessage.*;

/**
 * PAGEはクローラにて使用される単一のページを表すクラスです。<p/>
 * 本クラスにて接続先のページ情報のデータストアへの保存、データストアからの読み込みを行います。<br/>
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class GPage extends AbstractPage {
	
	/** データストアマネージャ */
	protected Neo4JDataStoreManager dataStoreManager;
	
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
	public GPage(String url, Neo4JDataStoreManager dataStoreManager) throws PageIllegalArgumentException, PageAccessException {
		super(url);
		this.dataStoreManager = dataStoreManager;
		((GUrl)this.url).setDataStoreManager(dataStoreManager);
	}

	@Override
	public boolean save() throws CrawlerSaveException {
		if (this.isSaved()) return false;
		try {
			Neo4JDataStore dataStore = this.dataStoreManager.getDataAccessObject("PAGE");
			Node pageNode = dataStore.createNode();
			pageNode.addLabel(CrawlerNodeLabel.PAGE);
			pageNode.setProperty("hash"      , this.getData().getHash());
			pageNode.setProperty("data"      , this.getData().getBytesToBase64String());
			pageNode.setProperty("accessdate", this.accessDateFormat.format(this.getCreateDate()));
			GUrl url = (GUrl)this.getUrl();
			url.save();
			Node urlNode = url.getUrlNode();
			urlNode.addOutGoingRelation(CrawlerRelationshipLabel.DATA, pageNode);
		} catch (Neo4JDataStoreManagerCypherException e) {
			throw new CrawlerSaveException(FAILE_TO_SAVE_PAGE, this.url.toString());
		} catch (PageAccessException e) {
			throw new CrawlerSaveException(FAILE_TO_GET_PAGE, this.url.toString());
		}
		return true;
	}
	
	@Override
	public boolean isSaved() throws CrawlerSaveException {
		try {
			Neo4JDataStore dataStore = this.dataStoreManager.getDataAccessObject("PAGE");
			int count = dataStore.selectInt(new Cypher("MATCH(page:PAGE{hash:?})RETURN COUNT(page)").setParameter(this.getData().getHash())).intValue();
			if (count == 0) {
				return false;
			} else {
				return true;
			}
		} catch (Neo4JDataStoreManagerCypherException e) {
			throw new CrawlerSaveException(FAILE_TO_SAVE_PAGE, this.url.toString());
		} catch (PageAccessException e) {
			throw new CrawlerSaveException(FAILE_TO_GET_PAGE, this.url.toString());
		}
	}
	
	/**
	 * このページ情報の最新のＩＤを取得する。
	 * 
	 * @return このページ情報のＩＤ
	 * @throws CrawlerSaveException 
	 */
	public int getLatestID() throws CrawlerSaveException {
//		class IDComvertable implements DataConvertable {
//			private String countname;
//			private int    id;
//			IDComvertable(String countname){
//				this.countname = countname;
//			}
//			@Override
//			public DataConvertable convert(DataBaseNode dataBaseNode) throws DataStoreManagerException {
//				this.id = dataBaseNode.getInt(this.countname);
//				return this;
//			}
//			int getID() {
//				return this.id;
//			}
//		}
//		
//		class StringComvertable implements DataConvertable {
//			private String countname;
//			private String result;
//			StringComvertable(String countname){
//				this.countname = countname;
//			}
//			@Override
//			public DataConvertable convert(DataBaseNode dataBaseNode) throws DataStoreManagerException {
//				this.result = dataBaseNode.getString(this.countname);
//				return this;
//			}
//			String getString() {
//				return this.result;
//			}
//		}
		
//		try {
//			Neo4JDataStore dataStore = this.dataStoreManager.getDataAccessObject("PAGE");
//			Cypher pageData = new Cypher("MATCH(page:PAGE{url:?}) RETURN MAX(page.accessdate) as ACCESSDATE");
//			pageData.setParameter(this.url.toString());
//			List<StringComvertable> resultList = dataStore.executeWithRetuen(pageData, new StringComvertable("ACCESSDATE"));
//			if (resultList.size() == 0 || resultList.get(0).getString() == null) {
//				return -1;
//			} else {
//				Cypher pageIDData = new Cypher("MATCH(page:PAGE{url:?, accessdate:?}) RETURN ID(page) as PAGEID");
//				pageIDData.setParameter(this.url.toString());
//				pageIDData.setParameter(resultList.get(0).getString());
//				return dataStore.executeWithRetuen(pageIDData, new IDComvertable("PAGEID")).get(0).getID();
//			}
//			
//		} catch (ClassCastException | DataStoreManagerException e) {
//			throw new CrawlerSaveException(DATASTOREMANAGER_CAN_NOT_CREATE);
//		}
		return 0;
	}
	
	@Override
	public boolean isLatest() throws CrawlerSaveException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected Url createUrl(String url) throws PageIllegalArgumentException {
		return new jp.co.dk.crawler.gdb.GUrl(url, this.dataStoreManager);
	}

	/** 
	 * データストアマネージャを設定する。
	 * 
	 * @param dataStoreManager データストアマネージャー
	 */
	public void setDataStoreManager(Neo4JDataStoreManager dataStoreManager) {
		this.dataStoreManager = dataStoreManager;
		((GUrl)this.url).setDataStoreManager(dataStoreManager);
	}
}


