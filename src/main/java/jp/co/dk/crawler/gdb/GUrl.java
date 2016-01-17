package jp.co.dk.crawler.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.crawler.AbstractUrl;
import jp.co.dk.crawler.exception.CrawlerReadException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStore;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.Node;
import jp.co.dk.neo4jdatastoremanager.NodeSelector;
import jp.co.dk.neo4jdatastoremanager.cypher.Cypher;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerCypherException;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerException;

public class GUrl extends AbstractUrl {
	
	/** データストアマネージャ */
	protected Neo4JDataStoreManager dataStoreManager;
	
	/**
	 * <p>指定のURLパターンに合致するURLを一覧にして返却します。</p>
	 * URLパターンは正規表現を使用することができ、そのパターンに合致するURLを検索し、一覧にして返却します。
	 * 
	 * @param url 検索対象のURLパターン
	 * @param dataStoreManager データストアマネージャ
	 * @return 検索対象のURLパターンに合致するURL一覧
	 * @throws CrawlerReadException URL情報の読み込みに失敗した場合
	 */
	public static List<GUrl> wellKnownUrlList(String url, Neo4JDataStoreManager dataStoreManager) throws CrawlerReadException {
		try {
			List<GUrl> urlList = new ArrayList<GUrl>();
			Neo4JDataStore dataStore = dataStoreManager.getDataAccessObject("URL");
			List<Node> urlNodeList = dataStore.selectNodeList(new Cypher("MATCH(url:URL) WHERE url.url=~? RETURN url").setParameter(url));
			for (Node urlNode : urlNodeList) urlList.add(new GUrl(urlNode.getPropertyString("url"), dataStoreManager));
			return urlList;
		} catch (Neo4JDataStoreManagerCypherException | PageIllegalArgumentException e) {
			throw new CrawlerReadException(FAILE_TO_READ_URL, url,e); 
		}
	}
	
	/**
	 * <p>コンストラクタ</p>
	 * 指定のURL文字列、データストアマネージャをを基にＵＲＬを表すインスタンスを生成する。
	 * 
	 * @param url URL文字列
	 * @param dataStoreManager データストアマネージャ
	 * @throws PageIllegalArgumentException URL文字列がnullまたは、空文字だった場合
	 */
	public GUrl(String url, Neo4JDataStoreManager dataStoreManager) throws PageIllegalArgumentException {
		super(url);
		this.dataStoreManager = dataStoreManager;
	}
	
	/** 
	 * データストアマネージャを設定する。
	 * 
	 * @param dataStoreManager データストアマネージャー
	 */
	public void setDataStoreManager(Neo4JDataStoreManager dataStoreManager) {
		this.dataStoreManager = dataStoreManager;
	}
	
	@Override
	public boolean save() throws CrawlerSaveException {
		try {
			Neo4JDataStore dataStore = this.dataStoreManager.getDataAccessObject("URL");
			
			Node urlNode = dataStore.selectNode(new Cypher("MATCH(url:URL{url:?})RETURN url").setParameter(this.toString()));
			if (urlNode != null) return false;
			
			Node endnode = dataStore.selectNode(new Cypher("MATCH(host:HOST{name:?})RETURN host").setParameter(this.getHost()));
			if (endnode == null) {
				endnode = dataStore.createNode();
				endnode.addLabel(CrawlerNodeLabel.HOST);
				endnode.setProperty("name", this.getHost());
			}
			for(String path : this.getPathList()){
				List<Node> findNodes = endnode.getOutGoingNodes(new NodeSelector(){
					@Override
					public boolean isSelect(org.neo4j.graphdb.Node node) {
						if (node.hasProperty("name")) {
							String pathName = (String)node.getProperty("name");
							if (path.equals(pathName)) return true;
						}
						return false;
					}
				});
				if (findNodes.size() == 0) {
					Node newEndnode = dataStore.createNode();
					newEndnode.addLabel(CrawlerNodeLabel.PATH);
					newEndnode.setProperty("name", path);
					endnode.addOutGoingRelation(CrawlerRelationshipLabel.CHILD, newEndnode);
					endnode = newEndnode;
				} else {
					endnode = findNodes.get(0);
				}
			}
			Map<String, Object> parameter = new HashMap<String, Object>(this.getParameter());
			if (parameter.size() != 0) {
				int parameterID = parameter.hashCode();
				if (endnode.getOutGoingNodes(new NodeSelector() {
						@Override
						public boolean isSelect(org.neo4j.graphdb.Node node) {
							if (node.hasProperty("parameter_id")) {
								int id = ((Integer)node.getProperty("parameter_id")).intValue();
								if (id == parameterID) return true;
							}
							return false;
						}
					}).size() == 0
				) {
					Node newEndnode = dataStore.createNode();
					newEndnode.addLabel(CrawlerNodeLabel.PARAMETER);
					newEndnode.setProperty("parameter_id", parameter.hashCode());
					newEndnode.setProperty(parameter);
					endnode.addOutGoingRelation(CrawlerRelationshipLabel.CHILD, newEndnode);
					endnode = newEndnode;
				}
			}
			
			List<Node> urlNodeList = endnode.getOutGoingNodes(new NodeSelector(){
				@Override
				public boolean isSelect(org.neo4j.graphdb.Node node) {
					if (node.hasLabel(CrawlerNodeLabel.URL)) return true;
					return false;
				}
			});
			if (urlNodeList.size() == 0) {
				Node newUrlNode = dataStore.createNode();
				newUrlNode.addLabel(CrawlerNodeLabel.URL);
				newUrlNode.setProperty("url_id", this.hashCode());
				newUrlNode.setProperty("url", this.toString());
				endnode.addOutGoingRelation(CrawlerRelationshipLabel.CHILD, newUrlNode);
				endnode = newUrlNode;
			}
		} catch (Neo4JDataStoreManagerException | Neo4JDataStoreManagerCypherException e) {
			throw new CrawlerSaveException(DATASTOREMANAGER_CAN_NOT_CREATE, e);
		}
		return true;
	}
	
	/**
	 * このURLを表すノードオブジェクトを取得し返却します。
	 * 
	 * @return URLを表すノードオブジェクト
	 * @throws Neo4JDataStoreManagerCypherException データストアからの読み込みに失敗した場合
	 */
	public Node getUrlNode() throws Neo4JDataStoreManagerCypherException {
		Neo4JDataStore dataStore = this.dataStoreManager.getDataAccessObject("URL");
		return dataStore.selectNode(new Cypher("MATCH(url:URL{url:?})RETURN url").setParameter(this.toString()));
	}
	
	/**
	 * このURLと同じホストにあるURLを一覧にして返却します。
	 * 同じホストが存在しない場合、空のインスタンスを返却します。
	 * 
	 * @return 同じホストのURL
	 * @throws CrawlerReadException データストアからの読み込みに失敗した場合
	 */
	public List<GUrl> getAllUrlBySameHost() throws CrawlerReadException {
		List<GUrl> childUrlList = new ArrayList<GUrl>();
		Neo4JDataStore dataStore = this.dataStoreManager.getDataAccessObject("URL");
		try {
			List<Node> urlNodeList = dataStore.selectNodeList(new Cypher("match(host:HOST{name:?})-[:CHILD*1..]->(url:URL) return url").setParameter(this.getHost()));
			for (Node urlNode : urlNodeList) childUrlList.add(new GUrl(urlNode.getPropertyString("url"), this.dataStoreManager));
		} catch (Neo4JDataStoreManagerCypherException | PageIllegalArgumentException e) {
			throw new CrawlerReadException(FAILE_TO_READ_URL, this.getHost(), e);
		}
		return childUrlList;
	}
	
	/**
	 * このURLのページ情報ですでに保存している件数を取得します。
	 * 
	 * @return 保存済みの件数
	 * @throws CrawlerReadException データストアからの読み込みに失敗した場合
	 */
	public int getSavedCount() throws CrawlerReadException {
		Neo4JDataStore dataStore = this.dataStoreManager.getDataAccessObject("URL");
		try {
			return dataStore.selectInt(new Cypher("MATCH(url:URL{url:?})-->(page:PAGE) RETURN COUNT(page)").setParameter(this.toString())).intValue();
		} catch (Neo4JDataStoreManagerCypherException e) {
			throw new CrawlerReadException(FAILE_TO_READ_URL, this.getHost(), e);
		}
	}
}