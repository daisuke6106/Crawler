package jp.co.dk.crawler.rdb;

import jp.co.dk.crawler.exception.CrawlerExceptionTest;
import jp.co.dk.crawler.exception.CrawlerFatalExceptionTest;
import jp.co.dk.crawler.exception.CrawlerInitExceptionTest;
import jp.co.dk.crawler.exception.CrawlerSaveExceptionTest;
import jp.co.dk.crawler.rdb.dao.mysql.CrawlerErrorsMysqlImplTest;
import jp.co.dk.crawler.rdb.dao.mysql.DocumentsMysqlImplTest;
import jp.co.dk.crawler.rdb.dao.mysql.LinksMysqlImplTest;
import jp.co.dk.crawler.rdb.dao.mysql.PagesMysqlImplTest;
import jp.co.dk.crawler.rdb.dao.mysql.RedirectErrorsMysqlImplTest;
import jp.co.dk.crawler.rdb.dao.mysql.UrlsMysqlImplTest;
import jp.co.dk.crawler.rdb.dao.record.PagesRecordTest;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ 

	CrawlerTest.class,
	PageTest.class,
	
	jp.co.dk.crawler.rdb.controler.AllTest.class,
	jp.co.dk.crawler.rdb.dao.mysql.AllTest.class,
	jp.co.dk.crawler.rdb.dao.record.AllTest.class,
	jp.co.dk.crawler.exception.AllTest.class,
	
})
public class AllTest {}
 