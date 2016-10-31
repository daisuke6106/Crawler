package jp.co.dk.crawler.scenario.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MoveActionName {
	String name();
	String manualTitle();
	String manualText();
	String[] manualExample();
	String[] manualArgument();
}
