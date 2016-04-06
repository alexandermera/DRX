package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.apache.jena.atlas.json.io.parser.JSONParser;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import metadata.Dataset;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import shell.Util;

public class Fingerprint {
	HashMap< String, Double >	fingerPrint ;
	// put in xml app
	public static String 		path 	=  "/Users/AlexanderMera/Dropbox/Approach/WebContent/dataset/textRepository/" ;

	public Fingerprint( ){
	}
	  public static void main(String[] args) throws Exception {
		  Fingerprint fp = new Fingerprint( ) ;
		  Util u = new Util( path ) ;
		  HashMap < String, Dataset > datasets = u.unSerializeDatasets( "datasetsBizer.v1") ;
		  fp.calculateFingerPrint( datasets.get( "zitgist-musicbrainz" ) ) ;
	  }
	public HashMap< String, Double > calculateFingerPrint ( Dataset ds ){
		String content = "" ;
		fingerPrint = null ;
		if( existFile( ds.idDataHub ) ){ 
			content = getTextContent( ds.idDataHub ) ;
			// delete end string @xxx
			content = preProcessingText( content ) ;
			Annotator annotator = new Annotator( ) ;
			fingerPrint = annotator.annotateText( content, ds.idDataHub ) ;
		}
		return fingerPrint ;
	}
	
	private String preProcessingText(String content) {
		String tmpContent = "" ;
		StringTokenizer tokenizer = new StringTokenizer( content ) ;
		while ( tokenizer.hasMoreTokens( ) ) {
			String s = tokenizer.nextToken( ).toLowerCase( ) ;
			s = s.replaceAll( "[-+.^:,\")(/“”?*©«»�!@\\}\\]\\[\\{]","" ) ;
			s = s.replaceAll("\\s","") ;
			if( !s.trim( ).isEmpty( ) ){
				if( !weka.core.Stopwords.isStopword( s ) ){
					if( !s.contains("http") && !s.contains("\\") && !s.contains("\\/")&& !s.contains("«")&& !s.contains("»")&& !s.contains("   ") ){
						tmpContent += " " + s ;
					}
				}
			}
		}
		return tmpContent;
	}

	public boolean existFile( String nameFile ){
		File f = new File( path + nameFile + ".txt" ) ;
		if( f.exists( ) && !f.isDirectory( ) && f.length( ) > 300 ) 
			return 	  true ;
		else
			return false ;
	}
	public String getTextContent (String fileName){
		String content 	= "" ;
        String fullPath = path + fileName + ".txt"  ;
        System.out.println( fullPath ) ;
        try {
        	 	content = readFile( fullPath, Charset.defaultCharset( ) ) ; 
        } catch (FileNotFoundException e) {
           System.out.println( "problem file not found: " + e.getMessage( ) ) ;
        } catch (IOException ioe){
        	System.out.println( "problem reading-file: " + ioe.getMessage( )  ) ;
        }
        return content ;
    }
	@Override
	public String toString( ){
		String text = "" ;
		for (String cat : fingerPrint.keySet( ) ) {
			text += "\n " + cat + ":" + fingerPrint.get( cat ) ;
		}
		return text ;
	}
	
	public String readFile(String path, Charset encoding) 
			  throws IOException {
			  byte[ ] encoded = Files.readAllBytes( Paths.get( path ) ) ;
			  return new String(encoded, encoding);
	}
	public static boolean isUTF8MisInterpreted( String input ) {
        //convenience overload for the most common UTF-8 misinterpretation
		return isUTF8MisInterpreted( input, "Windows-1252" ) ;  
	}

	public static boolean isUTF8MisInterpreted( String input, String encoding ) {
	  CharsetDecoder decoder = Charset.forName( "UTF-8" ).newDecoder( ) ;
	  CharsetEncoder encoder = Charset.forName( encoding ).newEncoder( ) ;
	  ByteBuffer tmp;
	  try {
	      	tmp = encoder.encode( CharBuffer.wrap( input ) ) ;
	  }catch( CharacterCodingException e ) {
	      return false;
	  }
	  try {
		  	decoder.decode(	tmp	) ;
		  	return true ;
	  }catch( CharacterCodingException e ){
	      	return false;
	  }       
	}

}
