package jp.co.dk.crawler.dao.record;

import jp.co.dk.datastoremanager.DataConvertable;
import jp.co.dk.datastoremanager.Record;
import jp.co.dk.datastoremanager.database.DataBaseRecord;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

/**
 * CountRecordは、各テーブルで実行したカウントを取得する処理の結果を保持する単一のレコードを表すクラス。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class CountRecord implements DataConvertable {
	
	/** カウント結果カラム名 */
	private String countColumnName;
	
	/** カウント結果 */
	private int count;
	
	/**
	 * 指定のカウントカラム名称を元に、カウント取得用のレコードオブジェクトを生成します。
	 * @param countColumnName
	 */
	public CountRecord(String countColumnName) {
		this.countColumnName = countColumnName;
	}
	
	@Override
	public DataConvertable convert(DataBaseRecord arg0)	throws DataStoreManagerException {
		count = arg0.getInt(this.countColumnName);
		return this;
	}
	@Override
	public DataConvertable convert(Record arg0)	throws DataStoreManagerException {
		this.count = arg0.getInt(1);
		return this;
	}
	
	/**
	 * このカウントの取得結果を返却します。
	 * @return カウント取得結果
	 */
	public int getCount() {
		return this.count;
	}
}
