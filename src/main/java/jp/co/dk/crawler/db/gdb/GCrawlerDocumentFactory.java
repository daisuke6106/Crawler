package jp.co.dk.crawler.db.gdb;

import static jp.co.dk.browzer.message.BrowzingMessage.ERROR_EXTENSION_IS_NOT_SET;

import java.io.InputStream;

import jp.co.dk.browzer.DocumentFactory;
import jp.co.dk.browzer.Page;
import jp.co.dk.browzer.contents.BrowzingExtension;
import jp.co.dk.browzer.http.header.ContentsType;
import jp.co.dk.crawler.db.gdb.html.GCrawlerHtmlElementFactory;
import jp.co.dk.document.File;
import jp.co.dk.document.exception.DocumentException;

public class GCrawlerDocumentFactory extends DocumentFactory {

	public GCrawlerDocumentFactory(Page page) {
		super(page);
	}

	@Override
	public File create(BrowzingExtension extension, InputStream inputStream) throws DocumentException {
		if (extension == null) return new jp.co.dk.document.File(inputStream);
		switch (extension) {
			case HTML:
				return new jp.co.dk.document.html.HtmlDocument(inputStream, new GCrawlerHtmlElementFactory(this.page));
			case XML:
				return new jp.co.dk.document.xml.XmlDocument(inputStream);
			case JSON:
				return new jp.co.dk.document.json.JsonDocument(inputStream);
			default:
				return new jp.co.dk.document.File(inputStream);
		}
	}
	
	@Override
	public File create(ContentsType contentsType, InputStream inputStream) throws DocumentException {
		if (contentsType == null) throw new DocumentException(ERROR_EXTENSION_IS_NOT_SET);
		switch (contentsType) {
			case TEXT_HTML:
				return new jp.co.dk.document.html.HtmlDocument(inputStream, new GCrawlerHtmlElementFactory(this.page));
			case TEXT_XML:
				return new jp.co.dk.document.xml.XmlDocument(inputStream);
			case APPLICATION_JSON:
				return new jp.co.dk.document.json.JsonDocument(inputStream);
			default:
				return new jp.co.dk.document.File(inputStream);
		}
	}

}
