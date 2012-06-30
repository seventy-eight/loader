package org.seventyeight.loader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Extracter {
	
	private Type type;
	private List<File> files = new ArrayList<File>();
	private List<File> paths = new ArrayList<File>();
	private File output;
	
	public enum Type {
		jar,
		zip
	}
	
	public Extracter setType( Type type ) {
		this.type = type;
		return this;
	}
	
	public Extracter addFile( File file ) {
		this.files.add( file );
		return this;
	}
	
	public Extracter addPath( File path ) {
		this.paths.add( path );
		return this;
	}
	
	public Extracter setOutputPath( File path ) {
		this.output = path;
		return this;
	}
	
	public List<File> extract() throws IOException {
		
		List<File> unpacked = new ArrayList<File>();
		
		/* Extract path */
		for( File path : paths ) {
			File[] files = path.listFiles( new OnlyExtension( type.name() ) );
			
			for( File packageFile : files ) {
				String packageName = packageFile.getName();
				packageName = packageName.substring( 0, ( packageName.length() - 4 ) );
				File folderName = new File( ( output != null ? output : path ), packageName );
				FileUtils.deleteDirectory( folderName );
				
				extractArchive( packageFile, folderName );
				unpacked.add( folderName );
			}
		}
		
		for( File packageFile : files ) {
			String packageName = packageFile.getName();
			packageName = packageName.substring( 0, ( packageName.length() - 4 ) );
			File folderName = new File( ( output != null ? output : packageFile.getParentFile() ), packageName );
			extractArchive( packageFile, folderName );
			unpacked.add( folderName );
		}
		
		return unpacked;
	}
	
	public static void extractArchive( File archive, File outputDir ) {
		extractArchive( archive, outputDir, null );
	}
	
	public static void extractArchive( File archive, File outputDir, String subdir ) {
		try {
			ZipFile zipfile = new ZipFile( archive );
			for( Enumeration<? extends ZipEntry> e = zipfile.entries(); e.hasMoreElements(); ) {
				ZipEntry entry = (ZipEntry) e.nextElement();
				if( subdir == null || ( subdir != null && entry.toString().startsWith( subdir ) ) ) {
					unzipEntry( zipfile, entry, outputDir );
				}
			}
		} catch( Exception e ) {
			System.err.println( e.getMessage() );
		}
	}

	private static void unzipEntry( ZipFile zipfile, ZipEntry entry, File outputDir ) throws IOException {

		if( entry.isDirectory() ) {
			new File( outputDir, entry.getName() ).mkdirs();
			return;
		}

		File outputFile = new File( outputDir, entry.getName() );
		if( !outputFile.getParentFile().exists() ) {
			outputFile.getParentFile().mkdirs();
		}

		BufferedInputStream inputStream = new BufferedInputStream( zipfile.getInputStream( entry ) );
		BufferedOutputStream outputStream = new BufferedOutputStream( new FileOutputStream( outputFile ) );

		try {
			IOUtils.copy( inputStream, outputStream );
		} finally {
			outputStream.close();
			inputStream.close();
		}
	}

	private class OnlyExtension implements FilenameFilter {
		String ext;

		public OnlyExtension( String ext ) {
			this.ext = "." + ext;
		}

		public boolean accept( File dir, String name ) {
			return name.endsWith( ext );
		}
	}
}
