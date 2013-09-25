package jp.co.dk.crawler.dao.mysql;

import static org.junit.Assert.*;
import jp.co.dk.crawler.TestCrawlerFoundation;
import jp.co.dk.crawler.dao.Pages;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

import org.junit.Test;

public class TestPagesMysqlImpl extends TestCrawlerFoundation{

	@Test
	public void createTable() {
		try {
			Pages pages = new PagesMysqlImpl(super.getAccessableDataBaseAccessParameter());
			pages.createTable();
			pages.dropTable();
		} catch (DataStoreManagerException e) {
			fail(e);
		}
	}
}
