package jp.co.dk.crawler.scenario;

/**
 * <p>遷移制御定数クラス</p>
 * コントローラに対して遷移するか否かを通知するための定数クラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public enum MoveControl {
	/** 遷移する */
	Transition,
	
	/** 遷移しない */
	NotTransition,
}
