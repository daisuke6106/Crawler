package jp.co.dk.crawler.rdb;

/**
 * MoveInfoは、moveした結果のページ遷移状態を定義します。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public enum MoveInfo {
	/** 遷移済み状態 */
	MOVED,
	/** 未遷移状態 */
	NON_MOVED
}