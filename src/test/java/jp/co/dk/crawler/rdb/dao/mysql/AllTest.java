package jp.co.dk.crawler.rdb.dao.mysql;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 
	CrawlerErrorsMysqlImplTest.class,
	DocumentsMysqlImplTest.class,
	LinksMysqlImplTest.class,
	PagesMysqlImplTest.class,
	RedirectErrorsMysqlImplTest.class,
	UrlsMysqlImplTest.class,
})
public class AllTest {}
 