package com.general.mbts4ma.erunner;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class MethodSearcher {

	Method methods[];

	public MethodSearcher(Method methods[]) {
		this.methods = methods;
	}

	public ArrayList<Method> getMethodsAnnotatedWith(Class<? extends Annotation> annotation) {
		ArrayList<Method> matchMethods = new ArrayList<Method>();

		for (Method method : this.methods) {
			if (method.isAnnotationPresent(annotation)) {
				matchMethods.add(method);
			}
		}

		return matchMethods;
	}

	public Method getMethodsEventAnnotatedWith(String label) {
		for (Method method : this.getMethodsAnnotatedWith(Event.class)) {
			if (method.getAnnotation(Event.class).label().equals(label)) {
				return method;
			}
		}

		return null;
	}

	public Method getMethodsEventAnnotatedWith(String eventLabel, String eventRule) {
		for (Method method : this.getMethodsAnnotatedWith(Event.class)) {
			if (method.getAnnotation(Event.class).label().equals(eventLabel) && method.getAnnotation(Event.class).rule().equals(eventRule)) {
				return method;
			}
		}

		return null;
	}
}