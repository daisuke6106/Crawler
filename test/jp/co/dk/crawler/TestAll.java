package jp.co.dk.crawler;

import jp.co.dk.crawler.dao.mysql.TestLinksMysqlImpl;
import jp.co.dk.crawler.dao.mysql.TestPagesMysqlImpl;
import jp.co.dk.crawler.exception.TestCrawlerException;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	TestCrawler.class,
	
	TestLinksMysqlImpl.class,
	TestPagesMysqlImpl.class,
	
	TestCrawlerException.class,
})
public class TestAll {}
 