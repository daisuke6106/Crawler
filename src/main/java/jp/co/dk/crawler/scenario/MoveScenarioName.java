package jp.co.dk.crawler.scenario;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MoveScenarioName {
	String name();
	String manualTitle();
	String manualText();
	String[] manualExample();
	String[] manualArgument();
}
