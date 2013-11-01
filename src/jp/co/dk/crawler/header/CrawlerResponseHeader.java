package jp.co.dk.crawler.header;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import jp.co.dk.browzer.exception.BrowzingException;
import jp.co.dk.browzer.http.header.HeaderField;

public class CrawlerResponseHeader extends jp.co.dk.browzer.http.header.ResponseHeader{
	
	protected SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
	
	public CrawlerResponseHeader(Map<String, List<String>> arg0) throws BrowzingException {
		super(arg0);
	}

	public Date getLastModified() {
		List<String> lastModifideList = this.getHeader(HeaderField.LAST_MODIFIED);
		String lastModifideStr = lastModifideList.get(0);
		try {
			return this.dateFormat.parse(lastModifideStr);
		} catch (ParseException e) {
			
		}
	}
}
