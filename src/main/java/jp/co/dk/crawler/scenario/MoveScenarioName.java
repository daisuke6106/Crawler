package jp.co.dk.crawler.scenario;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface MoveScenarioName {
	String name();
}
