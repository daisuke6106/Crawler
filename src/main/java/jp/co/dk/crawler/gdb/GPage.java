package jp.co.dk.crawler.gdb;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.PageEventHandler;
import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageHeaderImproperException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.http.header.ContentsType;
import jp.co.dk.browzer.http.header.RequestHeader;
import jp.co.dk.browzer.http.header.ResponseHeader;
import jp.co.dk.browzer.http.header.record.ResponseRecord;
import jp.co.dk.crawler.AbstractPage;
import jp.co.dk.crawler.AbstractUrl;
import jp.co.dk.crawler.exception.CrawlerReadException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.document.ByteDump;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.exception.DocumentFatalException;
import jp.co.dk.document.html.HtmlDocument;
import jp.co.dk.document.html.element.A;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStore;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.Node;
import jp.co.dk.neo4jdatastoremanager.NodeSelector;
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
	protected static SimpleDateFormat accessDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
	/**
	 * <p>保存済みのURLを基にデータベースにアクセスし、ページオブジェクトを復元し返却します。</p>
	 * 指定のURLの情報が存在しなかった場合、空のリストを返却します。
	 * 
	 * @param url URL文字列
	 * @param dataStoreManager データストアマネージャ
	 * @return 指定のURLのページオブジェクト一覧
	 * @throws CrawlerReadException ページ情報の読み込みに失敗した場合
	 */
	public static List<GPage> read(String url, Neo4JDataStoreManager dataStoreManager) throws CrawlerReadException {
		
		List<GPage> gpageList = new ArrayList<GPage>();
		Neo4JDataStore dataStore = dataStoreManager.getDataAccessObject("PAGE");
		try {
			GUrl gUrl = new GUrl(url, dataStoreManager);
			List<Node> pageNodeList = dataStore.selectNodeList(new Cypher("MATCH(url:URL{url:?})-[:DATA]->(page:PAGE) RETURN page").setParameter(gUrl.toString()));
			for (Node pageNode : pageNodeList) {
				
				// リクエストヘッダを取得する。
				Map<String, String> requestHeader = pageNode.getOutGoingNodes(new NodeSelector() {
					@Override
					public boolean isSelect(org.neo4j.graphdb.Node node) {
						if (node.hasLabel(CrawlerNodeLabel.REQUEST_HEADER)) return true;
						return false;
					}
				}).get(0).getProperty();
				
				// レスポンスヘッダを取得する。
				Map<String, String> responseHeader = pageNode.getOutGoingNodes(new NodeSelector() {
					@Override
					public boolean isSelect(org.neo4j.graphdb.Node node) {
						if (node.hasLabel(CrawlerNodeLabel.RESPONSE_HEADER)) return true;
						return false;
					}
				}).get(0).getProperty();
				
				Map<String, List<String>> responseHeaderMap = new HashMap<String, List<String>>();
				for (Map.Entry<String, String> responseHeaderEntry : responseHeader.entrySet()) {
					String keyWithIndex = responseHeaderEntry.getKey();
					String value        = responseHeaderEntry.getValue();
					
					String[] splitedKey = keyWithIndex.split("\\$");
					String   key        = splitedKey[0];
					int      index      = Integer.parseInt(splitedKey[1]);
					
					if (key.equals("null")) key = null;
					
					List<String> savedValue = responseHeaderMap.get(key);
					if (savedValue == null) {
						savedValue = new ArrayList<String>();
						responseHeaderMap.put(key, savedValue);
					}
					savedValue.add(value);
				}
				
				// アクセス日付を取得する
				Date accessDate = accessDateFormat.parse(pageNode.getPropertyString("accessdate"));
				
				// データ本体を取得する
				String dataStr = pageNode.getPropertyString("data");
				ByteDump data = ByteDump.getByteDumpFromBase64String(dataStr);
				List<PageEventHandler> pageEventHandlerList = new ArrayList<PageEventHandler>();
				gpageList.add(new GPage(url, new RequestHeader(requestHeader), new ResponseHeader(responseHeaderMap), accessDate, data, pageEventHandlerList, dataStoreManager));
			}
		} catch (Neo4JDataStoreManagerCypherException | PageIllegalArgumentException | PageHeaderImproperException | ParseException e) {
			throw new CrawlerReadException(FAILE_TO_READ_PAGE, url, e);
		}
		return gpageList;
	}

	/**
	 * <p>コンストラクタ</p>
	 * 指定のURL、リクエストヘッダ、レスポンスヘッダ、データ、イベントハンドラを基にページオブジェクトのインスタンスを生成します。
	 * 本コンストラクタはすでに保存されているページ情報からページオブジェクトを復元する際に使用します。
	 * 
	 * @param url URL文字列
	 * @param requestHeader リクエストヘッダ
	 * @param responseHeader レスポンスヘッダ
	 * @param data データオブジェクト
	 * @param pageEventHandlerList イベントハンドラ
	 * @param dataStoreManager データストアマネージャ
	 * @throws PageIllegalArgumentException データが不正、もしくは不足していた場合
	 */
	private GPage(String url, RequestHeader requestHeader, ResponseHeader responseHeader, Date accessDate, ByteDump data, List<PageEventHandler> pageEventHandlerList, Neo4JDataStoreManager dataStoreManager) throws PageIllegalArgumentException {
		super(url, requestHeader, responseHeader, accessDate, data, pageEventHandlerList);
		this.dataStoreManager = dataStoreManager;
	}
	
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
			ResponseHeader responseHeader = this.getResponseHeader();
			
			ResponseRecord responseRecord = responseHeader.getResponseRecord();
			pageNode.setProperty("http_statuscode", responseRecord.getHttpStatusCode().getCode());
			pageNode.setProperty("http_version"   , responseRecord.getHttpVersion());
			
			ContentsType contentsType = responseHeader.getContentsType();
			if (contentsType != null) {
				pageNode.setProperty("content_type"   , contentsType.getType()   );
				pageNode.setProperty("content_subtype", contentsType.getSubType());
			} else {
				pageNode.setProperty("content_type"   , "");
				pageNode.setProperty("content_subtype", "");
			}
			
			pageNode.setProperty("hash"           , this.getData().getHash());
			pageNode.setProperty("data"           , this.getData().getBytesToBase64String());
			pageNode.setProperty("size"           , this.getData().length());
			
			Node requestHeaderNode = dataStore.createNode();
			requestHeaderNode.addLabel(CrawlerNodeLabel.REQUEST_HEADER);
			for (Map.Entry<String, String> requestHeaderProperty : this.getRequestHeader().getHeaderMap().entrySet()){
				requestHeaderNode.setProperty(requestHeaderProperty.getKey(), requestHeaderProperty.getValue());
			}
			pageNode.addOutGoingRelation(CrawlerRelationshipLabel.REQUEST_HEADER, requestHeaderNode);
			
			Node responseHeaderNode = dataStore.createNode();
			responseHeaderNode.addLabel(CrawlerNodeLabel.RESPONSE_HEADER);
			for (Map.Entry<String, List<String>> responseHeaderProperty : this.getResponseHeader().getHeaderMap().entrySet()){
				String key         = responseHeaderProperty.getKey();
				List<String> value = responseHeaderProperty.getValue();
				for (int i=0; i<value.size(); i++) responseHeaderNode.setProperty(key+"$"+i, value.get(i));
			}
			pageNode.addOutGoingRelation(CrawlerRelationshipLabel.RESPONSE_HEADER, responseHeaderNode);
			
			pageNode.setProperty("accessdate", accessDateFormat.format(this.getAccessDate()));
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
			int count = dataStore.selectInt(
					new Cypher("MATCH(url:URL{url:?})-[:DATA]->(page:PAGE{hash:?}) RETURN COUNT(page)")
					.setParameter(this.url.toString())
					.setParameter(this.getData().getHash())
				).intValue();
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
	
	public void saveAllUrl() throws PageAccessException, DocumentException, PageIllegalArgumentException, CrawlerSaveException, Neo4JDataStoreManagerCypherException {
		jp.co.dk.document.File document = this.getDocument();
		if (document instanceof HtmlDocument) {
			List<A> anchorList = this.getAnchor();
			Node pageNode = this.getPageNode();
			for (A anchor : anchorList) {
				String url = anchor.getHref();
				if (!url.equals("")) {
					GUrl gUrl;
					try {
						gUrl = (GUrl)this.createUrl(url);
					} catch (PageIllegalArgumentException e) {
						continue;
					}
					gUrl.save();
					Node urlNode = gUrl.getUrlNode();
					pageNode.addOutGoingRelation(CrawlerRelationshipLabel.ANCHOR, urlNode, "url", url);
				}
			}
		}
	}
	
	/**
	 * このページ情報の最新のＩＤを取得する。
	 * 
	 * @return このページ情報のＩＤ
	 * @throws Neo4JDataStoreManagerCypherException 
	 */
	public String getLatestAccessDate() throws Neo4JDataStoreManagerCypherException {
		Neo4JDataStore dataStore = this.dataStoreManager.getDataAccessObject("PAGE");
		return dataStore.selectString(new Cypher("MATCH(url:URL{url:?})-[:DATA]->(page:PAGE) return MAX(page.accessdate) ").setParameter(this.url.toString()));
	}
	
	public Node getPageNode() throws DocumentFatalException, PageAccessException, Neo4JDataStoreManagerCypherException {
		Neo4JDataStore dataStore = this.dataStoreManager.getDataAccessObject("PAGE");
		return dataStore.selectNode(new Cypher("MATCH(url:URL{url:?})-[:DATA]->(page:PAGE{hash:?}) return page")
		.setParameter(this.url.toString())
		.setParameter(this.getData().getHash()));
	}
	
	@Override
	public boolean isLatest() throws CrawlerSaveException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected AbstractUrl createUrl(String url) throws PageIllegalArgumentException {
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


