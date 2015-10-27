package jp.co.dk.crawler.gdb;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.http.header.ContentsType;
import jp.co.dk.browzer.http.header.ResponseHeader;
import jp.co.dk.browzer.http.header.record.ResponseRecord;
import jp.co.dk.crawler.AbstractPage;
import jp.co.dk.crawler.AbstractUrl;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.document.exception.DocumentException;
import jp.co.dk.document.exception.DocumentFatalException;
import jp.co.dk.document.html.element.A;
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
	protected static SimpleDateFormat accessDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	
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
				responseHeaderNode.setProperty(responseHeaderProperty.getKey(), responseHeaderProperty.getValue().toString());
			}
			pageNode.addOutGoingRelation(CrawlerRelationshipLabel.RESPONSE_HEADER, responseHeaderNode);
			
			pageNode.setProperty("accessdate", accessDateFormat.format(this.getCreateDate()));
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
		List<A> anchorList = this.getAnchor();
		Node pageNode = this.getPageNode();
		for (A anchor : anchorList) {
			String url = anchor.getHref();
			if (!url.equals("")) {
				GUrl gUrl = (GUrl)this.createUrl(url);
				gUrl.save();
				Node urlNode = gUrl.getUrlNode();
				pageNode.addOutGoingRelation(CrawlerRelationshipLabel.ANCHOR, urlNode, "url", url);
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
		return dataStore.selectNode(new Cypher("MATCH(page:PAGE{hash:?}) return page").setParameter(this.getData().getHash()));
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


