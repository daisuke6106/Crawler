package jp.co.dk.crawler.gdb;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jp.co.dk.browzer.Parameter;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.html.element.A;
import jp.co.dk.crawler.AbstractUrl;
import jp.co.dk.crawler.exception.CrawlerReadException;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStore;
import jp.co.dk.neo4jdatastoremanager.Neo4JDataStoreManager;
import jp.co.dk.neo4jdatastoremanager.Node;
import jp.co.dk.neo4jdatastoremanager.NodeSelector;
import jp.co.dk.neo4jdatastoremanager.cypher.Cypher;
import jp.co.dk.neo4jdatastoremanager.exception.Neo4JDataStoreManagerCypherException;

public class GUrl extends AbstractUrl {
	
	/** データストアマネージャ */
	protected Neo4JDataStoreManager dataStoreManager;
	
	/**
	 * <p>URLに関するインデックスを作成する。</>
	 * @param dataStoreManager データストアマネージャ
	 * @throws Neo4JDataStoreManagerCypherException Cypherの実行に失敗した場合
	 */
	public static void createIndex(Neo4JDataStoreManager dataStoreManager) throws Neo4JDataStoreManagerCypherException {
		Neo4JDataStore dataStore = dataStoreManager.getDataAccessObject("URL");
		dataStore.selectString(new Cypher("CREATE INDEX ON :PROTOCOL(name)"));
		dataStore.selectString(new Cypher("CREATE INDEX ON :HOST(name)"));
		dataStore.selectString(new Cypher("CREATE INDEX ON :PATH(name)"));
		dataStore.selectString(new Cypher("CREATE INDEX ON :PARAMETER(parameter_id)"));
		dataStore.selectString(new Cypher("CREATE INDEX ON :URL(url)"));
	}
	
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
			
			Node endnode;
			
			Node urlNode = dataStore.selectNode(new Cypher("MATCH(url:URL{url:?})RETURN url").setParameter(this.toString()));
			if (urlNode != null) return false;
			
			Node protocolnode = dataStore.selectNode(new Cypher("MATCH(protocol:PROTOCOL{name:?})RETURN protocol").setParameter(this.getProtocol()));
			if (protocolnode == null) {
				protocolnode = dataStore.createNode();
				protocolnode.addLabel(CrawlerNodeLabel.PROTOCOL);
				protocolnode.setProperty("name", this.getProtocol());
			}

			Node hostnode = dataStore.selectNode(new Cypher("MATCH(protocol:PROTOCOL{name:?})-->(host:HOST{name:?})RETURN host").setParameter(this.getProtocol()).setParameter(this.getHost()));
			if (hostnode == null) {
				hostnode = dataStore.createNode();
				hostnode.addLabel(CrawlerNodeLabel.HOST);
				hostnode.setProperty("name", this.getHost());
				protocolnode.addOutGoingRelation(CrawlerRelationshipLabel.CHILD, hostnode);
				endnode = hostnode;
			} else {
				endnode = hostnode;
			}
			
			for(String path : this.getPathList()){
				Node endPathNode = endnode.getOutGoingNode(new NodeSelector(){
					@Override
					public boolean isSelect(org.neo4j.graphdb.Node node) {
						if (node.hasProperty("name")) {
							String pathName = (String)node.getProperty("name");
							if (path.equals(pathName)) return true;
						}
						return false;
					}
				});
				if (endPathNode == null) {
					endPathNode = dataStore.createNode();
					endPathNode.addLabel(CrawlerNodeLabel.PATH);
					endPathNode.setProperty("name", path);
					endnode.addOutGoingRelation(CrawlerRelationshipLabel.CHILD, endPathNode);
					endnode = endPathNode;
				} else {
					endnode = endPathNode;
				}
			}
			
			
			Parameter parameter = this.getParameter();
			if (!parameter.isEmpty()) {
				int parameterID = parameter.hashCode();
				Node endParamNode = endnode.getOutGoingNode(new NodeSelector() {
					@Override
					public boolean isSelect(org.neo4j.graphdb.Node node) {
						if (node.hasLabel(CrawlerNodeLabel.PARAMETER)) {
							int id = ((Integer)node.getProperty("parameter_id")).intValue();
							if (id == parameterID) return true;
						}
						return false;
					}
				});
				
				if (endParamNode == null) {
					endParamNode = dataStore.createNode();
					endParamNode.addLabel(CrawlerNodeLabel.PARAMETER);
					endParamNode.setProperty("parameter_id", parameter.hashCode());
					for (Map.Entry<String, String> paramEntry : parameter.getParameter().entrySet()) endParamNode.setProperty(paramEntry.getKey(), paramEntry.getValue());
					endnode.addOutGoingRelation(CrawlerRelationshipLabel.CHILD, endParamNode);
					endnode = endParamNode;
				} else {
					endnode = endParamNode;
				}
			}
			
			boolean trailingSlash = this.hasTrailingSlash();
			List<Node> urlNodeList = endnode.getOutGoingNodes(new NodeSelector(){
				@Override
				public boolean isSelect(org.neo4j.graphdb.Node node) {
					if (node.hasLabel(CrawlerNodeLabel.URL) && new Boolean(trailingSlash).equals(node.getProperty("trailing_slash"))) return true;
					return false;
				}
			});
			if (urlNodeList.size() == 0) {
				Node newUrlNode = dataStore.createNode();
				newUrlNode.addLabel(CrawlerNodeLabel.URL);
				newUrlNode.setProperty("url_id"        , this.hashCode());
				newUrlNode.setProperty("url"           , this.toString());
				newUrlNode.setProperty("trailing_slash", this.hasTrailingSlash());
				endnode.addOutGoingRelation(CrawlerRelationshipLabel.CHILD, newUrlNode);
				endnode = newUrlNode;
			}
		} catch (Neo4JDataStoreManagerCypherException e) {
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
	
	/**
	 * このURLのページ情報ですでに保存している件数を取得します。
	 * 検索を行った場合、その結果はキャッシュされ、２回目以降は同じ値が返却されます。
	 * 
	 * @return 保存済みの件数
	 * @throws CrawlerReadException データストアからの読み込みに失敗した場合
	 */
	public int getSavedCountByCache() throws CrawlerReadException {
		if (this.savedcount >= 0) return this.savedcount;
		this.savedcount = this.getSavedCount();
		return this.savedcount;
	}
	private int savedcount = -1;
}