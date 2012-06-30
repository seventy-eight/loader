package org.seventyeight.loader;

import java.lang.annotation.Annotation;

public abstract class Handler {
	public abstract void handle( Annotation a, Class<?> clazz ) throws Exception;
}
