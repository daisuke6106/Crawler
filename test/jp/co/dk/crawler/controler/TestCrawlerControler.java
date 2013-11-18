package jp.co.dk.crawler.controler;

import static org.junit.Assert.*;
import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.crawler.TestCrawlerFoundation;
import jp.co.dk.crawler.exception.CrawlerException;
import jp.co.dk.datastoremanager.exception.DataStoreManagerException;

import org.junit.Test;

public class TestCrawlerControler extends TestCrawlerFoundation{

	@Test
	public void save() {
		CrawlerControler controler = new CrawlerControler();
		try {
			controler.save("http://kanasoku.info/articles/32592.html");
		} catch (DataStoreManagerException e) {
			fail(e);
		} catch (CrawlerException e) {
			fail(e);
		} catch (BrowzingException e) {
			fail(e);
		}
	}

}
