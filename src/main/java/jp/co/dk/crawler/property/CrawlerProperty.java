package jp.co.dk.crawler.property;

import java.io.File;

import jp.co.dk.property.AbstractProperty;
import jp.co.dk.property.exception.PropertyException;

/**
 * クローラに関するプロパティを定義するクラスです。
 * 
 * @version 1.0
 * @author D.Kanno
 */
public class CrawlerProperty extends AbstractProperty {
	
	public static CrawlerProperty SLEEP_TIME = new CrawlerProperty("sleep.time");
	
	/**
	 * コンストラクタ<p>
	 * 
	 * 指定されたプロパティキーをもとにプロパティ定数クラスを生成します。
	 * 
	 * @param key プロパティキー
	 */
	protected CrawlerProperty (String key) throws PropertyException {
		super(new File("properties/CrawlerProperty.properties"), key);
	}
	
}
