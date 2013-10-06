package jp.co.dk.crawler.dao.record;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Date;
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
public class DocuenmtsRecord implements DataConvertable{
	
	/** ファイルID */
	protected long fileId;
	
	/** ファイル名 */
	protected String filename;
	
	/** 拡張子 */
	protected String extention;
	
	/** 最終更新日付 */
	protected Date lastUpdateDate;
	
	/** コンテンツ */
	protected byte[] data;
	
	/** 作成日時 */
	protected Date createDate;
	
	/** 更新日時 */
	protected Date updateDate;
	
	/**
	 * プロトコル名を取得する。
	 * @return プロトコル名
	 */
	public String getProtocol() {
		return protocol;
	}
	
	/**
	 * ホスト名を取得する。
	 * @return ホスト名
	 */
	public String getHost() {
		return host;
	}
	
	/**
	 * パスリストのハッシュ値を取得する。
	 * @return パスリストのハッシュ値
	 */
	public int getH_path() {
		return h_path;
	}
	
	/**
	 * パラメータのハッシュ値を取得する。
	 * @return パラメータのハッシュ値
	 */
	public int getH_parameter() {
		return h_parameter;
	}
	
	/**
	 * パスリストを取得する。
	 * @return パスリスト
	 * @throws CrawlerException バイト配列からパスリストへ変換に失敗した場合
	 */
	@SuppressWarnings("unchecked")
	public List<String> getPath() throws CrawlerException {
		return (List<String>)this.convertBytesToObject(path);
	}

	/**
	 * パラメータマップを取得する。
	 * @return パラメータマップ
	 * @throws CrawlerException バイト配列からパラメータマップへ変換に失敗した場合
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getParameter() throws CrawlerException {
		return (Map<String, String>)this.convertBytesToObject(parameter);
	}
	
	/**
	 * リクエストヘッダーマップを取得する。
	 * @return リクエストヘッダーマップ
	 * @throws CrawlerException バイト配列からリクエストヘッダーマップへ変換に失敗した場合
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getRequestHeader() throws CrawlerException {
		return (Map<String, String>)this.convertBytesToObject(requestHeader);
	}
	
	/**
	 * レスポンスヘッダーマップを取得する。
	 * @return レスポンスヘッダーマップ
	 * @throws CrawlerException バイト配列からレスポンスヘッダーマップへ変換に失敗した場合
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getResponceHeader() throws CrawlerException {
		return (Map<String, String>)this.convertBytesToObject(responceHeader);
	}
	
	/**
	 * コンテンツデータを取得する。
	 * @return コンテンツデータ
	 */
	public byte[] getContents() {
		return contents;
	}
	
	/**
	 * 登録日時を取得する。
	 * @return 登録日時
	 */
	public Date getCreateDate() {
		return createDate;
	}
	
	/**
	 * 更新日時を取得する。
	 * @return 更新日時
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	
	@Override
	public DataConvertable convert(DataBaseRecord record)	throws DataStoreManagerException {
		DocuenmtsRecord pagesRecord    = new DocuenmtsRecord();
		pagesRecord.protocol       = record.getString("PROTOCOL");
		pagesRecord.host           = record.getString("HOSTNAME");
		pagesRecord.h_path         = record.getInt("H_PATH");
		pagesRecord.h_parameter    = record.getInt("H_PARAM");
		pagesRecord.path           = record.getBytes("PATH");
		pagesRecord.parameter      = record.getBytes("PARAMETER");
		pagesRecord.requestHeader  = record.getBytes("REQUEST_HEADER");
		pagesRecord.responceHeader = record.getBytes("RESPONCE_HEADER");
		pagesRecord.contents       = record.getBytes("DATA");
		pagesRecord.createDate     = record.getTimestamp("CREATE_DATE");
		pagesRecord.updateDate     = record.getTimestamp("UPDATE_DATE");
		return pagesRecord;
	}

	@Override
	public DataConvertable convert(Record record)	throws DataStoreManagerException {
		DocuenmtsRecord pagesRecord    = new DocuenmtsRecord();
		pagesRecord.protocol       = record.getString(1);
		pagesRecord.host           = record.getString(2);
		pagesRecord.h_path         = record.getInt(3);
		pagesRecord.h_parameter    = record.getInt(4);
		pagesRecord.path           = record.getBytes(5);
		pagesRecord.parameter      = record.getBytes(6);
		pagesRecord.requestHeader  = record.getBytes(7);
		pagesRecord.responceHeader = record.getBytes(8);
		pagesRecord.contents       = record.getBytes(9);
		pagesRecord.createDate     = record.getDate(10);
		pagesRecord.updateDate     = record.getDate(11);
		return pagesRecord;
	}
	
	/**
	 * 指定のバイト配列をインスタンスに変換して返却します。<p/>
	 * 入出力例外が発生した、バイト配列がインスタンスでなくクラスの変換に失敗した場合、例外を送出します。
	 * 
	 * @param bytes バイト配列
	 * @return インスタンス
	 * @throws CrawlerException 入出力例外が発生した、クラスの変換に失敗した場合
	 */
	protected Object convertBytesToObject(byte[] bytes) throws CrawlerException {
		try {
			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
			Object object = objectInputStream.readObject();
			objectInputStream.close();
			byteArrayInputStream.close();
			return object;
		} catch (IOException e) {
			throw new CrawlerException(FAILE_TO_CONVERT_TO_INSTANCE_OF_AN_OBJECT_FROM_A_BYTE_ARRAY, e);
		} catch (ClassNotFoundException e) {
			throw new CrawlerException(FAILE_TO_CONVERT_TO_INSTANCE_OF_AN_OBJECT_FROM_A_BYTE_ARRAY, e);
		}
	}
}
