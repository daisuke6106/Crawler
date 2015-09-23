package jp.co.dk.crawler.exception;

public class CommandException extends RuntimeException {
	
	/**	シリアルバージョンID */
	private static final long serialVersionUID = 2928831487428204617L;
	
	protected final int returnCode;
	
	public CommandException(int returnCode) {
		this.returnCode = returnCode;
	}
	
	public int getRetuenCode() {
		return this.returnCode;
	}
}
