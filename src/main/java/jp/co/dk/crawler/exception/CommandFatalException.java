package jp.co.dk.crawler.exception;

public class CommandFatalException extends Exception {
	
	/**	シリアルバージョンID */
	private static final long serialVersionUID = -6899458810763234858L;
	
	protected final int returnCode;
	
	public CommandFatalException(int returnCode) {
		this.returnCode = returnCode;
	}
	
	public int getRetuenCode() {
		return this.returnCode;
	}
}
