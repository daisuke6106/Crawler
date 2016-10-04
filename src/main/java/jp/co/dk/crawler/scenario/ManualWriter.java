package jp.co.dk.crawler.scenario;

public interface ManualWriter {

	/**
	 * <p>このクラスの使用法タイトルを返却します。</p>
	 * @return このアクションクラスのタイトル
	 */
	public String manualTitle() ;
	
	/**
	 * <p>このクラスの使用法の概要を返却します。</p>
	 * @param lineseparater 改行文字
	 * @return このアクションクラスの概要説明文
	 */
	public String manualText(String lineseparater) ;
	
	/**
	 * <p>このクラスの引数内容を返却します。</p>
	 * @param lineseparater 改行文字
	 * @return このアクションクラスの引数内容
	 */
	public default String manualArguments(String lineseparater) {
		ManualArgument[] manualArgumentList = this.getManualArgument();
		if (manualArgumentList == null || manualArgumentList.length == 0) {
			return "nothing";
		} else {
			StringBuilder manualArgumentStr = new StringBuilder();
			for (ManualArgument manualArgument : manualArgumentList) manualArgumentStr.append(manualArgument.toString()).append(lineseparater);
			return manualArgumentStr.toString();
		}
	}
	
	public ManualArgument[] getManualArgument();
}
