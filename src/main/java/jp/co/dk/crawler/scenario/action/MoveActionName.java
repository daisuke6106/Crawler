package jp.co.dk.crawler.scenario.action;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface MoveActionName {
	String name();
}
