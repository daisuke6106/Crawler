package jp.co.dk.crawler.dao.record;

import jp.co.dk.datastoremanager.DataConvertable;
import jp.co.dk.datastoremanager.Record;
import jp.co.dk.datastoremanager.database.DataBaseRecord;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/** 
 * リンク元のURLと、リンク先のURL情報を管理するリンクステーブルのレコードを表すレコードクラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class LinksRecord implements DataConvertable {
	
	/** リンク元−プロトコル */
	protected String fromProtocol;
	
	/** リンク元−ホスト名 */
	protected String fromHost;
	
	/** リンク元−パス-ハッシュ値 */
	protected int fromH_path;
	
	/** リンク元−パラメータ-ハッシュ値 */
	protected int fromH_parameter;
	
	/** リンク先−プロトコル */
	protected String toProtocol;
	
	/** リンク先−ホスト名 */
	protected String toHost;
	
	/** リンク先−パス-ハッシュ値 */
	protected int toH_path;
	
	/** リンク先−パラメータ-ハッシュ値 */
	protected int toH_parameter;
	

	@Override
	public DataConvertable convert(DataBaseRecord record) throws DataStoreManagerException {
		LinksRecord linksRecord = new LinksRecord();
		linksRecord.fromProtocol    = record.getString("FROM_PROTOCOL");
		linksRecord.fromHost        = record.getString("FROM_HOSTNAME");
		linksRecord.fromH_path      = record.getInt("FROM_H_PATH");
		linksRecord.fromH_parameter = record.getInt("FROM_H_PARAM");
		linksRecord.toProtocol      = record.getString("TO_PROTOCOL");
		linksRecord.toHost          = record.getString("TO_HOSTNAME");
		linksRecord.toH_path        = record.getInt("TO_H_PATH");
		linksRecord.toH_parameter   = record.getInt("TO_H_PARAM");
		return linksRecord;
	}

	@Override
	public DataConvertable convert(Record record) throws DataStoreManagerException {
		LinksRecord linksRecord = new LinksRecord();
		linksRecord.fromProtocol    = record.getString(1);
		linksRecord.fromHost        = record.getString(2);
		linksRecord.fromH_path      = record.getInt(3);
		linksRecord.fromH_parameter = record.getInt(4);
		linksRecord.toProtocol      = record.getString(5);
		linksRecord.toHost          = record.getString(6);
		linksRecord.toH_path        = record.getInt(7);
		linksRecord.toH_parameter   = record.getInt(8);
		return linksRecord;
	}
}
