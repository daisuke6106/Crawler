package jp.co.dk.crawler.controler;

import java.util.List;

import jp.co.dk.browzer.exception.PageAccessException;
import jp.co.dk.browzer.exception.PageIllegalArgumentException;
import jp.co.dk.browzer.exception.PageMovableLimitException;
import jp.co.dk.browzer.exception.PageRedirectException;
import jp.co.dk.crawler.AbstractCrawler;
import jp.co.dk.crawler.exception.CrawlerSaveException;
import jp.co.dk.document.exception.DocumentException;

public abstract class AbtractCrawlerControler {
	
	protected AbstractCrawler crawler;
	
	AbtractCrawlerControler(String url) {
		this.crawler = this.createCrawler(url);
	}
	
	public void save() throws CrawlerSaveException {
		this.crawler.save();
	}
	
	public void saveAll() throws CrawlerSaveException, PageAccessException, PageIllegalArgumentException, PageRedirectException, PageMovableLimitException, DocumentException {
		this.crawler.saveAll();
	}
	
	protected abstract AbstractCrawler createCrawler(String url);
}
