package jp.co.dk.crawler.exception;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	CrawlerExceptionTest.class,
	CrawlerFatalExceptionTest.class,
	CrawlerInitExceptionTest.class,
	CrawlerSaveExceptionTest.class,
})
public class AllTest {}
 