package jp.co.dk.crawler;

public enum PageStatus {
	
	/** 状態：未セーブ状態 */
	NON_SAVE,
	
	/** 状態：正常にセーブ済み */
	SUCCESS_SAVED,
	
	/** 状態：リダイレクト時にエラー状態としてセーブ済み */
	ERROR_SAVED_BY_REDIRECT,
	
	/** 状態：エラー状態としてセーブ済み */
	ERROR_SAVED_BY_CRAWL,
}
