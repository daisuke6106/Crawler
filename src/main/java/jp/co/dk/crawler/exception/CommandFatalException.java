package jp.co.dk.crawler.exception;

public class CommandFatalException extends Exception {
	
	protected final int returnCode;
	
	public CommandFatalException(int returnCode) {
		this.returnCode = returnCode;
	}
	
	public int getRetuenCode() {
		return this.returnCode;
	}
}
