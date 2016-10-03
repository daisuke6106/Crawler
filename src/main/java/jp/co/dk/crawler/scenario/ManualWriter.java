package jp.co.dk.crawler.scenario;

public interface ManualWriter {

	/**
	 * <p>このアクションクラスのタイトルを返却します。</p>
	 * @return このアクションクラスの概要説明文
	 */
	public default String manualTitle() {
		return "";
	}
	
	/**
	 * <p>このアクションクラスの概要を返却します。</p>
	 * @param lineseparater 改行文字
	 * @return このアクションクラスの概要説明文
	 */
	public default String manualText(String lineseparater) {
		return "";
	}
	
	/**
	 * 
	 * @param lineseparater
	 * @return
	 */
	public default String manualArguments(String lineseparater) {
		return "";
	}
	
}
