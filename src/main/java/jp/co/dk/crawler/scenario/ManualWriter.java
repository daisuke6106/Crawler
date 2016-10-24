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
	
	/**
	 * 引数の一覧を返却する。
	 * 
	 * @return 引数を説明する引数マニュアル一覧
	 */
	public ManualArgument[] getManualArgument();
	
	/**
	 * <p>マニュアルを文字列として返却する。</p>
	 * 
	 * @param lineseparater 改行文字列
	 * @return マニュアルを文字列
	 */
	public default String toString(String lineseparater) {
		StringBuilder manual = new StringBuilder();
		manual.append(this.manualTitle()).append(lineseparater);
		return manual.toString();
	}
}
