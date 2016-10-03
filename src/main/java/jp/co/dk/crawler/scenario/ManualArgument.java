package jp.co.dk.crawler.scenario;

public class ManualArgument {
	
	/** 引数名 */
	private String argumentName;
	
	/** タイプ */
	private ArgumentType type;
	
	/** 必須 */
	private boolean isRequired;
	
	ManualArgument(String argumentName, ArgumentType type, boolean isRequired) {
		this.argumentName = argumentName;
		this.type = type;
		this.isRequired = isRequired;
	}
}

enum ArgumentType {
	/** 文字列 */
	STRING,
	/** 数値 */
	NUMBER,
	;
}