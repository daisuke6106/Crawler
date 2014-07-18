package jp.co.dk.crawler.eventhandler;

import jp.co.dk.browzer.event.PrintPageEventHandler;
import jp.co.dk.logger.Logger;
import jp.co.dk.logger.LoggerFactory;

public class LogPageEventHandler extends PrintPageEventHandler {
	
	/** ロガーインスタンス */
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	protected void print(String str) {
		this.logger.info(str);
	}
}
