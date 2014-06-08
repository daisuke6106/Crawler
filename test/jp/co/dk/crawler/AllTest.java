package jp.co.dk.crawler;

import jp.co.dk.crawler.dao.mysql.DocumentsMysqlImplTest;
import jp.co.dk.crawler.dao.mysql.LinksMysqlImplTest;
import jp.co.dk.crawler.dao.mysql.PagesMysqlImplTest;
import jp.co.dk.crawler.dao.mysql.UrlsMysqlImplTest;
import jp.co.dk.crawler.exception.CrawlerExceptionTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	CrawlerTest.class,
	
	DocumentsMysqlImplTest.class,
	LinksMysqlImplTest.class,
	PagesMysqlImplTest.class,
	UrlsMysqlImplTest.class,
	
	CrawlerExceptionTest.class,
})
public class AllTest {}
 