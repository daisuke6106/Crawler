package jp.co.dk.crawler.scenario;

/**
 * <p>遷移結果定数クラス</p>
 * コントローラに対して正常に遷移できたか失敗したかを通知するための定数クラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public enum MoveResult {
	
	/** 遷移に成功 */
	SuccessfullTransition,
	
	/** 遷移に失敗 */
	FailureToTransition,
	
	/** 遷移非許可 */
	UnAuthorizedTransition;
}
