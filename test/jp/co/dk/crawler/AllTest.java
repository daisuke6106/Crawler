package jp.co.dk.crawler;

import jp.co.dk.crawler.dao.mysql.CrawlerErrorsMysqlImplTest;
import jp.co.dk.crawler.dao.mysql.DocumentsMysqlImplTest;
import jp.co.dk.crawler.dao.mysql.LinksMysqlImplTest;
import jp.co.dk.crawler.dao.mysql.PagesMysqlImplTest;
import jp.co.dk.crawler.dao.mysql.RedirectErrorsMysqlImplTest;
import jp.co.dk.crawler.dao.mysql.UrlsMysqlImplTest;
import jp.co.dk.crawler.dao.record.PagesRecordTest;
import jp.co.dk.crawler.exception.CrawlerExceptionTest;
import jp.co.dk.crawler.exception.CrawlerFatalExceptionTest;
import jp.co.dk.crawler.exception.CrawlerInitExceptionTest;
import jp.co.dk.crawler.exception.CrawlerSaveExceptionTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 

	CrawlerTest.class,
	PageTest.class,
	
	jp.co.dk.crawler.controler.AllTest.class,
	jp.co.dk.crawler.dao.mysql.AllTest.class,
	jp.co.dk.crawler.dao.record.AllTest.class,
	jp.co.dk.crawler.exception.AllTest.class,
	
})
public class AllTest {}
 