package org.seventyeight.loader;

import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoader extends URLClassLoader {

	public ClassLoader() {
		super( new URL[]{});
	}
	
	public ClassLoader( java.lang.ClassLoader classLoader ) {
		super( new URL[]{}, classLoader);
	}
	
	public ClassLoader(URL[] urls) {
		super(urls);
	}
	
	public void addUrls( URL[] urls ) {
		//System.out.println( "Adding urls" );
		for( URL url : urls ) {
			addURL( url );
		}
	}
	
	public void addJar() {
		
	}

}
