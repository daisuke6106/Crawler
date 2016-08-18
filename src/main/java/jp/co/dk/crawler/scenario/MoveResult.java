package jp.co.dk.crawler.scenario;

public enum MoveResult {
	
	/** 遷移に成功 */
	SuccessfullTransition,
	
	/** 遷移に失敗 */
	FailureToTransition,
	
	/** 遷移非許可 */
	UnAuthorizedTransition;
}
