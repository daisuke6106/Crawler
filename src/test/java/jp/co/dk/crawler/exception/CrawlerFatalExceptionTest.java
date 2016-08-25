package jp.co.dk.crawler.exception;

import java.util.ArrayList;
import java.util.List;

import jp.co.dk.crawler.db.rdb.CrawlerFoundationTest;
import jp.co.dk.message.MessageInterface;
import static jp.co.dk.crawler.message.CrawlerMessage.*;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;

public class CrawlerFatalExceptionTest extends CrawlerFoundationTest {

	@Test
	public void constractor() {
		
		// メッセージオブジェクトのみ指定して実行した場合
		// 埋め込み文字列、throwクラスは取得できないこと
		CrawlerFatalException sut1 = new CrawlerFatalException(DETASTORETYPE_IS_NOT_SUPPORT);
		assertThat(sut1.getMessageObj(), is((MessageInterface)DETASTORETYPE_IS_NOT_SUPPORT));
		assertThat(sut1.getEmbeddedStrList().size(), is(0));
		assertThat(sut1.getThrowable(), nullValue());
		
		// メッセージオブジェクト、単一文字のみ指定して実行した場合
		// 指定の埋め込み文字列が取得できること
		// throwクラスは取得できないこと
		CrawlerFatalException sut2 = new CrawlerFatalException(DETASTORETYPE_IS_NOT_SUPPORT, "test");
		assertThat(sut2.getMessageObj(), is((MessageInterface)DETASTORETYPE_IS_NOT_SUPPORT));
		assertThat(sut2.getEmbeddedStrList().size(), is(1));
		assertThat(sut2.getEmbeddedStrList().get(0), is("test"));
		assertThat(sut2.getThrowable(), nullValue());
		
		// メッセージオブジェクト、単一文字のみ指定して実行した場合
		// 埋め込み文字列が取得できないこと
		// throwクラスは取得できること
		CrawlerFatalException sut3 = new CrawlerFatalException(DETASTORETYPE_IS_NOT_SUPPORT, new NullPointerException());
		assertThat(sut3.getMessageObj(), is((MessageInterface)DETASTORETYPE_IS_NOT_SUPPORT));
		assertThat(sut3.getEmbeddedStrList().size(), is(0));
		assertThat(sut3.getThrowable(), instanceOf(NullPointerException.class));
		
		// メッセージオブジェクト、単一文字のみ指定して実行した場合
		// 指定の埋め込み文字列が取得できること
		// throwクラスが取得できること
		CrawlerFatalException sut4 = new CrawlerFatalException(DETASTORETYPE_IS_NOT_SUPPORT, "test", new NullPointerException());
		assertThat(sut4.getMessageObj(), is((MessageInterface)DETASTORETYPE_IS_NOT_SUPPORT));
		assertThat(sut4.getEmbeddedStrList().size(), is(1));
		assertThat(sut4.getEmbeddedStrList().get(0), is("test"));
		assertThat(sut4.getThrowable(), instanceOf(NullPointerException.class));
		
		// メッセージオブジェクト、単一文字のみ指定して実行した場合
		// 指定の埋め込み文字列が取得できること
		// throwクラスが取得できること
		List<String> strList = new ArrayList<String>();
		strList.add("test");
		CrawlerFatalException sut5 = new CrawlerFatalException(DETASTORETYPE_IS_NOT_SUPPORT, strList, new NullPointerException());
		assertThat(sut5.getMessageObj(), is((MessageInterface)DETASTORETYPE_IS_NOT_SUPPORT));
		assertThat(sut5.getEmbeddedStrList().size(), is(1));
		assertThat(sut5.getEmbeddedStrList().get(0), is("test"));
		assertThat(sut5.getThrowable(), instanceOf(NullPointerException.class));
		
		
		// メッセージオブジェクト、単一文字のみ指定して実行した場合
		// 指定の埋め込み文字列が取得できること
		// throwクラスが取得できること
		String[] strArray = {"test"};
		CrawlerFatalException sut6 = new CrawlerFatalException(DETASTORETYPE_IS_NOT_SUPPORT, strArray, new NullPointerException());
		assertThat(sut6.getMessageObj(), is((MessageInterface)DETASTORETYPE_IS_NOT_SUPPORT));
		assertThat(sut6.getEmbeddedStrList().size(), is(1));
		assertThat(sut6.getEmbeddedStrList().get(0), is("test"));
		assertThat(sut6.getThrowable(), instanceOf(NullPointerException.class));
	}
	
}
