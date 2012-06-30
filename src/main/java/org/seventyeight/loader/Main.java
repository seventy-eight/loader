package org.seventyeight.loader;

import java.io.File;
import java.util.List;

public class Main {

	/**
	 * @param args
	 */
	public static void main( String[] args ) {
		ClassLoader classLoader = new ClassLoader( Thread.currentThread().getContextClassLoader() );
		
		Loader loader = new Loader( classLoader );
		
		File path = new File( "target/classes" );
		System.out.println( "----> " + new File( "target/classes" ).getAbsolutePath() );
		
		List<String> s = loader.getClasses( path, "" );
		
		System.out.println( "S: " + s );
	}

}
