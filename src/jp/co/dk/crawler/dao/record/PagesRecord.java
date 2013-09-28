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
	
	@Override
	public DataConvertable convert(DataBaseRecord arg0)	throws DataStoreManagerException {
		return null;
	}

	@Override
	public DataConvertable convert(Record arg0)	throws DataStoreManagerException {
		return null;
	}

}
