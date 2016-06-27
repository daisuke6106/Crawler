package jp.co.dk.crawler.gdb.controler;

/**
 * CrawleScenarioは、クローリングを行う際の挙動を定義するクラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public abstract class CrawleScenario {
	
	/**
	 * 開始ページのＵＲＬを返却する。
	 * 
	 * @return 開始ページ
	 */
	protected abstract String getStartPage();
}
