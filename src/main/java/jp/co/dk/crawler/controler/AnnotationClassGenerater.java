package jp.co.dk.crawler.controler;

import static jp.co.dk.crawler.message.CrawlerMessage.FAILE_TO_CLASS_GENERATION;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import jp.co.dk.browzer.exception.MoveActionFatalException;

import org.reflections.Reflections;

@SuppressWarnings("all")
public abstract class AnnotationClassGenerater extends ClassGenerater {

	protected String targetPackage;
	
	protected Class searchAnnotation;
	
	public AnnotationClassGenerater(String targetPackage, Class searchAnnotation, String classesStr) {
		super(classesStr);
		this.targetPackage    = targetPackage;
		this.searchAnnotation = searchAnnotation;
	}

	@Override
	protected Object createByClassName(String className, String[] arguments) throws MoveActionFatalException {
		Set<Class<?>> annotationClasses = getClassesByAnnotation(this.targetPackage, this.searchAnnotation);
		for (Class<?> annotationClass : annotationClasses) {
			String annotationName = this.getName(annotationClass.getAnnotation(searchAnnotation));
			if (annotationName.equals(className)) {
				String classStr = annotationClass.getName();
				return super.createByClassName(classStr, arguments);
			}
		}
		throw new MoveActionFatalException(FAILE_TO_CLASS_GENERATION, new String[]{"", className});
	}
	
	protected abstract <A extends Annotation> String getName(A annotationClass) ;
	
	public static Set<Class<?>> getClassesByAnnotation(String targetPackage, Class searchAnnotation) {
		Reflections reflections = new Reflections(targetPackage);
		Set<Class<?>> classesWithEntity = reflections.getTypesAnnotatedWith(searchAnnotation);
		return classesWithEntity;
	}

}
