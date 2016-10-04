package jp.co.dk.crawler.scenario;

public class ManualArgument {
	
	/** 引数名 */
	private String argumentName;
	
	/** タイプ */
	private ManualArgumentType type;
	
	/** 必須 */
	private boolean isRequired;
	
	public ManualArgument(String argumentName, ManualArgumentType type, boolean isRequired) {
		this.argumentName = argumentName;
		this.type = type;
		this.isRequired = isRequired;
	}
	
	@Override
	public String toString() {
		StringBuilder argStr = new StringBuilder();
		argStr.append(this.argumentName);
		argStr.append("(").append(this.type).append(")");
		if (this.isRequired) {
			argStr.append(" ").append("※必須");
		}
		return argStr.toString();
	}
}

