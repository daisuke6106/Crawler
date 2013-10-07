package jp.co.dk.crawler.exception;

import java.util.ArrayList;
import java.util.List;

import jp.co.dk.crawler.TestCrawlerFoundation;

import org.junit.Test;

import static jp.co.dk.datastoremanager.message.DataStoreManagerMessage.*;

public class TestCrawlerException extends TestCrawlerFoundation{

	@Test
	public void constractor() {
		// 指定のメッセージで、例外を生成されることを確認。
		new CrawlerException(PROPERTY_IS_NOT_SET);
		
		// 指定のメッセージと置換文字列で、例外を生成されることを確認。
		new CrawlerException(PROPERTY_IS_NOT_SET, "");
		
		// 指定のメッセージ、例外で、例外を生成されることを確認。
		new CrawlerException(PROPERTY_IS_NOT_SET, new Exception());
		
		// 指定のメッセージと置換文字列、例外で、例外を生成されることを確認。
		new CrawlerException(PROPERTY_IS_NOT_SET, "", new Exception());
		
		// 指定のメッセージと置換文字列一覧、例外で、例外を生成されることを確認。
		List<String> list2 = new ArrayList<String>();
		new CrawlerException(PROPERTY_IS_NOT_SET, list2, new Exception());
		
		// 指定のメッセージと置換文字列一覧、例外で、例外を生成されることを確認。
		String[] arrays2= {};
		new CrawlerException(PROPERTY_IS_NOT_SET, arrays2, new Exception());
		
	}

}
