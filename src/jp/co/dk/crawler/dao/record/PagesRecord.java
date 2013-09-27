package jp.co.dk.crawler.dao.record;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.DataConvertable;
import jp.co.dk.datastoremanager.Record;
import jp.co.dk.datastoremanager.database.DataBaseRecord;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

import static jp.co.dk.crawler.message.CrawlerMessage.*;

/**
 * PagesRecordは、PAGESテーブルの単一のレコードを表すクラス。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class PagesRecord implements DataConvertable{
	
	/** プロトコル */
	protected String protocol;
	
	/** ホスト名 */
	protected String host;
	
	/** パス */
	protected List<String> path;
	
	/** パラメータ */
	protected Map<String, String> parameter; 
	
	/** リクエストヘッダー */
	protected Map<String, String> requestHeader;
	
	/** レスポンスヘッダー */
	protected Map<String, String> responceHeader;
	
	/** コンテンツ */
	protected byte[] contents;
	
	/** 作成日時 */
	protected Date createDate;
	
	/** 更新日時 */
	protected Date updateDate;
	
	/**
	 * コンストラクタ<p/>
	 * 指定のパラメータを元にページレコードのインスタンスを生成します。<br/>
	 * 必須パラメータが設定されていない場合、例外を送出します。<br/>
	 * <br/>
	 * pathと、parameterに限り設定されていない場合（nullの場合）、空のリスト、マップインスタンスで置き換えます。<br/>
	 * 
	 * @param protcol        プロトコル文字列（必須）
	 * @param host           ホスト名（必須）
	 * @param path           パスリスト（設定されていない場合、空のリストで置き換え）
	 * @param parameter      パラメータマップ（設定されていない場合、空のマップで置き換え）
	 * @param requestHeader  リクエストヘッダ
	 * @param responceHeader レスポンスヘッダ
	 * @param contents       コンテンツデータ（バイト配列）
	 * @param createDate     登録日付
	 * @param updateDate     更新日付
	 * @throws CrawlerException 必須オブジェクトが設定されていなかった場合
	 */
	public PagesRecord(String protcol, String host, List<String> path, Map<String, String> parameter, Map<String, String> requestHeader, Map<String, String> responceHeader, byte[] contents, Date createDate, Date updateDate ) throws CrawlerException{
		if (protcol == null || protcol.equals("")) throw new CrawlerException(PARAMETER_IS_NOT_SET, "protocol");
		if (host == null    || host.equals(""))    throw new CrawlerException(PARAMETER_IS_NOT_SET, "host");
		if (path == null) path = new ArrayList<String>();
		if (parameter == null) parameter = new HashMap<String, String>();
		this.protocol       = protcol;
		this.host           = host;
		this.path           = new ArrayList<String>(path);
		this.parameter      = new HashMap<String,String>(parameter);
		this.requestHeader  = new HashMap<String,String>(requestHeader);
		this.responceHeader = new HashMap<String,String>(responceHeader);
		this.contents       = contents;
		this.createDate     = createDate;
		this.updateDate     = updateDate;
	}
	
	@Override
	public DataConvertable convert(DataBaseRecord arg0)	throws DataStoreManagerException {
		return null;
	}

	@Override
	public DataConvertable convert(Record arg0)	throws DataStoreManagerException {
		return null;
	}

}
