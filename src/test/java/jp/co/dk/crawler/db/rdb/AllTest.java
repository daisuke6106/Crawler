package jp.co.dk.crawler.db.rdb;

import jp.co.dk.crawler.db.rdb.dao.mysql.CrawlerErrorsMysqlImplTest;
import jp.co.dk.crawler.db.rdb.dao.mysql.DocumentsMysqlImplTest;
import jp.co.dk.crawler.db.rdb.dao.mysql.LinksMysqlImplTest;
import jp.co.dk.crawler.db.rdb.dao.mysql.PagesMysqlImplTest;
import jp.co.dk.crawler.db.rdb.dao.mysql.RedirectErrorsMysqlImplTest;
import jp.co.dk.crawler.db.rdb.dao.mysql.UrlsMysqlImplTest;
import jp.co.dk.crawler.db.rdb.dao.record.PagesRecordTest;
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
	
	jp.co.dk.crawler.db.rdb.controler.AllTest.class,
	jp.co.dk.crawler.db.rdb.dao.mysql.AllTest.class,
	jp.co.dk.crawler.db.rdb.dao.record.AllTest.class,
	jp.co.dk.crawler.exception.AllTest.class,
	
})
public class AllTest {}
 