package jp.co.dk.crawler.exception;

public class CommandException extends RuntimeException {
	
	protected final int returnCode;
	
	public CommandException(int returnCode) {
		this.returnCode = returnCode;
	}
	
	public int getRetuenCode() {
		return this.returnCode;
	}
}
