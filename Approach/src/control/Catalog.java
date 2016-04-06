package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.jena.assembler.assemblers.DefaultModelAssembler;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.riot.system.stream.StreamManager;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.util.FileManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jsoup.Jsoup;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.google.gson.Gson;

import metadata.Dataset;
import metadata.Resource;
import net.sf.json.JSONObject;

public class Catalog {
	public static String path 		=  "/Users/AlexanderMera/Dropbox/Approach/WebContent/dataset/textRepository/" ;
	public static String pathTemp	=  "/Users/AlexanderMera/Dropbox/Approach/WebContent/dataset/temp/" ;
	private int numMaxResouces 		= 10000 ;
	// max size file read from a URL 10M
	private int maxFileSise			= 100000000 ;
	public Set<String> suitableFormats = new HashSet<String>( ) ;
	int dsAvailable = 0 ;
	int sparql1 = 0 ;
	int sparql11 = 0 ;
	//Max time of connection
	int timeOut = 15000 ;
	
	public Catalog(){
		// -- Formats that the app can deal
		suitableFormats.add( "html" ) ;	suitableFormats.add( "HTML" ) ; suitableFormats.add( "xhtml" ) ;
		// RDF files
		suitableFormats.add( "rdf" ) ;	suitableFormats.add( "application/rdf+xml" ) ;suitableFormats.add( "application/rdf+xml" ) ; 
		suitableFormats.add( "rdf/xml" ) ; suitableFormats.add( "RDF" ) ; suitableFormats.add( "application/rdf xml" ) ;
		suitableFormats.add( "example/rdf xml" ) ; suitableFormats.add( "example/rdf+xml" ) ;	
		// void
		// turtle
		suitableFormats.add( "application/x-turtle" ) ; suitableFormats.add( "application/turtle" ) ; suitableFormats.add( "text/rdf+ttl" ) ;	
		suitableFormats.add( "rdf/turtle" ) ; suitableFormats.add( "rdf-turtle" ) ;suitableFormats.add( "turtle" ) ;suitableFormats.add( "text/turtle" ) ;			
		suitableFormats.add( "example/turtle" ) ;
		// n3
		suitableFormats.add( "text/rdf+n3" ) ;suitableFormats.add( "example/n3" ) ;	suitableFormats.add( "rdf/n3" ) ;
		suitableFormats.add( "rdf-n3" ) ; suitableFormats.add( "n3" ) ; suitableFormats.add( "text/n3" ) ; suitableFormats.add( "example/n3" ) ;
		//n-triples
		suitableFormats.add( "text/ntriples" ) ;suitableFormats.add( "application/n-triples" ) ; suitableFormats.add( "n-triples" ) ;
		
		suitableFormats.add( "meta/void" ) ;  suitableFormats.add( "rdf/void" ) ;
		// Plain text files
		suitableFormats.add( "TXT" ) ;
		// SparqlendPoints
		suitableFormats.add( "api/sparql" ) ;	suitableFormats.add( "sparql" ) ;
		// json files
		suitableFormats.add( "json" ) ;	suitableFormats.add( "JSON" ) ;
		// CSV files
		suitableFormats.add( "CSV" ) ;	suitableFormats.add( "csv" ) ; //suitableFormats.add( "zip:csv" ) ;
		
		suitableFormats.add( "XML" ) ;	suitableFormats.add( "xml" ) ; 
		
		suitableFormats.add( "XLS" ) ;	
	}
	
	public static void main(String [] args)
	{	
		/*String id = "teste-teste" ;
		Catalog ca = new Catalog( ) ;
		System.out.println("".isEmpty( ) ) ;
		try{
			URL url = new URL("http://linkeddata.ge.imati.cnr.it:8890/sparql" ) ;
			url = new URL( "http://webscience.rkbexplorer.com/sparql/" ) ;
			//ca.processDataset( url ) ;
			//url = new URL("http://linked.opendata.cz/sparql?query=DESCRIBE+%3Chttp%3A%2F%2Flinked.opendata.cz%2Fresource%2Fdataset%2Fic%2Fdistribution%3E&output=text%2Fturtle" ) ;
			//ca.processDataset( url ) ;
			//url = new URL("https://ckannet-storage.commondatastorage.googleapis.com/2015-04-22T22:54:11.872Z/fy2011-pos-gt-2500dol.xlsx" ) ;
			//ca.processDataset( url ) ;
		//	url = new URL("https://ckannet-storage.commondatastorage.googleapis.com/2015-05-11T09:30:32.598Z/datatoupload.csv" ) ;
			
		} catch (MalformedURLException e) {
			e.printStackTrace() ;
		}*/ 
	}
	
	public void getContentDasets (HashMap < String, Dataset > datasets ){
 		System.out.println("Total ds: " + datasets.size() );
 		int b = 0 ;
		for(String ds: datasets.keySet( ) ){ 
			if( !ds.equalsIgnoreCase( "allie-abbreviation-and-long-form-database-in-life-science"  ) ){
				if ( !existFile( ds + ".txt" ) ){
					System.out.println("------------------------------------------------------------------------");
					System.out.println( "Processing: " + ds ) ;
					serializeContent ( ds + ".txt"  , processDataset( datasets.get( ds ) ) );
				}
				b++ ;
				if( b%10 == 0 )
					System.out.println( "Processed " + b + "of " + datasets.size( ) ) ;
			}
		}
		System.out.println( "endlich!" ) ;
	}
	// Creates a file for each dataset, plain text is extracted from different sources such as Web pages, tags, sparqlenpoint, VOID, CSV or XLS. 
	public String processDataset( Dataset ds ){
		URL url;
		String result 		= "" ;
		String content 		= "" ;
		String tmpContent 	= "" ;
		Resource tmprs = null	;
		Gson gson 			= new Gson();
	    JSONObject output 	= new JSONObject( ) ;  
		for( Resource rs:ds.getResources( ) ){
			if( rs.format != null ){
				if( suitableFormats.contains( rs.format ) ){
					System.out.println( "\t R-id:" + rs.id ) ;
					try {	url = new URL ( rs.getUrl( ) );
							if( ( rs.format.equalsIgnoreCase("api/sparql") || rs.format.equalsIgnoreCase("sparql") ) ){
								tmpContent = getSparqlEndpointContent( url ) ;
								if( !tmpContent.isEmpty( ) ){
									content += tmpContent ;
									tmprs = rs ;
								}
							}else{
								tmpContent = readFile( url , rs.format ) ;
								if( !tmpContent.isEmpty( ) ){
									content += tmpContent ;
									tmprs = rs ;
								}
							}
					} catch (MalformedURLException e) {
						System.out.println("\t Problem resource: " + rs.id + "of ds: " + ds ) ;
					}
					
					if( content.length( ) > 1000000 ){
						System.out.println("Dataset with more than 1MB: " +  ds.getName( ) ) ;
						break ;
					}
				}
			}
		}
		// if there is no resources try with the web page of the dataset
		if( content.isEmpty( ) && ds.getURL() != null){
			if( !ds.getURL( ).isEmpty( ) ){
				try {
					tmprs = new Resource( ) ;
					tmprs.setFormat( "HTML");
					tmprs.setName("Web Page");
					tmprs.setUrl( ds.getURL ( ) ) ; 
					tmprs.setId( ds.getURL ( ) ) ; 
					content = readFile( new URL ( ds.getURL( ) ) , tmprs.format ) ;
				} catch (MalformedURLException e) {
					System.out.println("\t Problem loading web page ds: " + ds.idDataHub ) ;
				}
			}
		}
		if( content.isEmpty( ) ){
			System.out.println("No resources for: " + ds.idDataHub ) ;
		}
		output.put("content", content ) ;  
		result = gson.toJson( output ) ;
		System.out.println( result ) ;
		return result ;
	}

	private String getSparqlEndpointContent( URL url) {
		String content = "" ;
		//query sparql version 1.1 
		String querystr = 
				  " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
				+ " PREFIX skos: <http://www.w3.org/2004/02/skos/core#> " 
				+ " SELECT ?l "
							+ " WHERE {"
							+ " 		{ ?s	rdfs:label	     ?l  } "
							+ " UNION 	{ ?s	skos:altLabel	 ?l  } "
							+ " UNION 	{ ?s	skos:prefLabel	 ?l  } "
							+ " UNION 	{ ?s	skos:hiddenLabel ?l } "
							+ "} "
							+ " LIMIT " + numMaxResouces  ;
		// query sparql version 1.0
        String querystr2 = 
        		" PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
        				+ " SELECT ?l "
        				+ " WHERE {"
        				+ " 		 ?s	rdfs:label	     ?l   "
        				+ "} "
        				+ " LIMIT " + numMaxResouces  ;
        ResultSet results ;
        QueryEngineHTTP qt = new QueryEngineHTTP( url.toString( ), querystr ) ;
        qt.setTimeout( timeOut ) ; 
        try{
        	results = qt.execSelect( ) ;
        	while ( results.hasNext( ) ) {
        		QuerySolution qs = results.next( ) ;
        		Literal literal = qs.getLiteral( "?l" ) ;
        		content+= " " + literal.getString( ) ;
        	}
        }catch( Exception e){
        	System.out.println( "\t Problem with query sparql v1.1: "+ url.toString( ) ) ;
        }
        qt = new QueryEngineHTTP( url.toString( ), querystr2 ) ;
        qt.setTimeout( timeOut ) ;
        if ( content.isEmpty( ) ) {
	        try{
	        	results = qt.execSelect( ) ;
	        	while ( results.hasNext( ) ) {
	        		QuerySolution qs = results.next( ) ;
	        		Literal literal = qs.getLiteral( "?l" ) ;
	        		content+= " " + literal.getString( ) ;
	        	}
	        	sparql1 ++ ;
	        }catch( Exception e){
	        	System.out.println( "\t Problem with query sparql v1.0: "+ url.toString( ) ) ;
	        }
        }else{
        	sparql11 ++ ;
        }
		return content ;
	}
	// Reads file content
	public String readFile( URL url, String format){
		String content = "" ;
		try { // get URL content
				URLConnection urlc ;
				HttpURLConnection htmlurl ;
				if ( ( format.equalsIgnoreCase("html") || format.equalsIgnoreCase("HTML") || format.equalsIgnoreCase("xhtml") ) ){
					htmlurl = (HttpURLConnection) url.openConnection( ) ; htmlurl.setReadTimeout( timeOut ) ; 
					htmlurl.setConnectTimeout( timeOut ) ; htmlurl.setAllowUserInteraction( false ) ;
					htmlurl.setDoInput( true ) ; htmlurl.setDoOutput( false ) ; htmlurl.setUseCaches( true ) ;
					htmlurl.connect( ) ;
				    System.out.println( "\t ctype: " +  htmlurl.getContentType( ) + " code: " +  htmlurl.getResponseCode( ) ) ;
					if( containsIgnoreCase( htmlurl.getContentType( ) , "html") && htmlurl.getResponseCode() == HttpURLConnection.HTTP_OK )
						content = getHTMLContent( htmlurl ) ;
				}else{	// get from file
						int fileSize = getFileSize( url ) ;
						if( -1 > fileSize  && fileSize < maxFileSise   ) {
							urlc = url.openConnection( ) ; urlc.setConnectTimeout( timeOut) ;
							urlc.setReadTimeout( timeOut ) ; urlc.setAllowUserInteraction( false ) ;
							urlc.setDoInput( true ) ; urlc.setDoOutput( false ) ; urlc.setUseCaches( true ) ;
							if( format.equalsIgnoreCase("CSV") || format.equalsIgnoreCase("csv") ){
								if( containsIgnoreCase ( urlc.getContentType( ) , "csv") )
								content = getCSVContent( urlc ) ;
							}else if( format.equalsIgnoreCase("XML") || format.equalsIgnoreCase("xml") ){
								if ( containsIgnoreCase ( urlc.getContentType( ) , "xml") )
									content = getXMLContent( urlc ) ;
							}else if( ( format.equalsIgnoreCase("meta/void") || format.equalsIgnoreCase("rdf/void") ) && isVOID( urlc ) ){
								content = getVOIDContent( urlc ) ;
							}else if( format.equalsIgnoreCase("TXT") ){
								content = getTXTContent( urlc ) ;
							}else if( format.equalsIgnoreCase("XLS")  ){
								content = geXLSContent( urlc ) ;
							}else if( !containsIgnoreCase( urlc.getContentType( ), "html" ) && urlc.getContentType() != null ) {
									content = getModelContent( urlc , format ) ;
							}
						}
				}
		} catch (Exception e) {
			System.out.println( "\t Problem URL open file: "+ url.toString( ) ) ;
		}
		return content ;
	}

	private String getModelContent(URLConnection urlc, String format) {
		Model model ;
		String file = "";
		String 	content = "" ;
		Boolean zipped = false;
		QueryExecution qexec = null ;
		Query query ;
		try{
			String extension = urlc.getURL( ).toString( ) ;
			if (extension.substring( extension.lastIndexOf('.')).equals(".gz") ) 
			{   file = unZipFile( urlc, "gz" ) ;
			    zipped = true;
			}else if (extension.substring( extension.lastIndexOf('.')).equals(".zip") ){
				file = unZipFile( urlc, "zip" ) ;
				zipped = true;
			}
			if( zipped )
					model = RDFDataMgr.loadModel( file ) ;
				else
					model= FileManager.get().loadModel( urlc.getURL( ).toString( ) ) ;
				String 	querystr = 
					  " PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> "
					+ " PREFIX skos: <http://www.w3.org/2004/02/skos/core#> " 
					+ " SELECT ?l "
								+ " WHERE {"
								+ " 		{ ?s	rdfs:label	     ?l  } "
								+ " UNION 	{ ?s	skos:altLabel	 ?l  } "
								+ " UNION 	{ ?s	skos:prefLabel	 ?l  } "
								+ " UNION 	{ ?s	skos:hiddenLabel ?l  } "
								+ "} "
								+ " LIMIT " + numMaxResouces  ;
				query = QueryFactory.create( querystr ) ;
				qexec = QueryExecutionFactory.create( query ,  model ) ;
				ResultSet  results = qexec.execSelect( ) ;
				while( results.hasNext( ) ){
					QuerySolution soln = results.nextSolution( ) ;
					Literal l = soln.getLiteral( "?l" ) ;
					content += " " + l ;
				}
		}catch( Exception e){
			System.out.println( "\t Problem parsing model jena: " +  urlc.toString( ) ) ;
		}finally{
			if( qexec!= null)
				qexec.close( ) ; 
		}
		return content ;
	}

	private String getTXTContent(URLConnection urlc) {
		String content = "" ;
		BufferedReader in;
		String inputLine;
		try {
			in = new BufferedReader( new InputStreamReader( urlc.getInputStream( ) ) );
			while (( inputLine = in.readLine()) != null)	
				content += inputLine ;
			in.close();
		} catch (IOException e) {
			System.out.println( "\t Problem URL parsing TXT: " +  urlc.toString( ) ) ;
		}
		return content ;        
	}

	private String getXMLContent(URLConnection urlc ) {
		String content = "" ;
		DocumentBuilder db;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance( ) ;
		try {
			db = dbf.newDocumentBuilder( ) ;
			Document doc = db.parse( urlc.getInputStream( ) ) ;
			NodeList nl = doc.getDocumentElement().getChildNodes( ) ;
			for( int k = 0; k < nl.getLength( ) ; k++ ) {
				String tmp = getXMLValues( ( ( Node ) nl.item( k ) ) , " " ) ;
				tmp = tmp.replace("\n", "").replace("\r", "").trim( ) ;
				if( !tmp.isEmpty( ) )
					content += " " + tmp ;
			}		
		} catch (ParserConfigurationException e) {
			System.out.println( "\t Problem URL parsing xml: "+ urlc.toString( ) ) ;
		} catch (SAXException e) {
			System.out.println( "\t Problem URL parsing xml: "+ urlc.toString( ) ) ;
		} catch (IOException e) {
			System.out.println( "\t Problem URL getting xml: "+ urlc.toString( ) ) ;
		}
        return content;
	}

	private String geXLSContent(URLConnection urlc) {
		String content = "" ;
	    try{
	    /** Create a POIFSFileSystem object**/
	    POIFSFileSystem myFileSystem = new POIFSFileSystem( urlc.getInputStream( ) ) ;
	    /** Create a workbook using the File System**/
	     HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
	     for (int i = 0; i < myWorkBook.getNumberOfSheets(); i++) {
	         HSSFSheet mySheet = myWorkBook.getSheetAt(i);  
	         Iterator rowIter = mySheet.rowIterator();
	         while(rowIter.hasNext()){
	        	 HSSFRow myRow = (HSSFRow) rowIter.next();
	        	 if (myRow.getCell(0) != null){
	        		 Iterator cellIter = myRow.cellIterator();
	        		 while(cellIter.hasNext()){
	        			 HSSFCell myCell = (HSSFCell) cellIter.next( );
	        			 content += " " + myCell.getStringCellValue( ) ;
	        		 }
	        	 }
	         }
	         myWorkBook.close(  ) ;
	     }	 
	    /** We now need something to iterate through the cells.**/
	    }catch (Exception e){
	    	System.out.println( "\t Problem URL getting XLS: "+ urlc.toString( ) ) ;
	    }
	    return content;		
	}

	private String getVOIDContent(URLConnection urlc) {
		String content = "" ;
		try{
			Model m = FileManager.get( ).loadModel( urlc.getURL( ).toExternalForm( ) ) ; 
			//String VOID = "http://rdfs.org/ns/void#" ;
			String dcterms = "http://purl.org/dc/terms/" ;
			Resource description = (Resource) m.getResource( dcterms + "description" ) ;
			Resource title = (Resource) m.getResource( dcterms + "title" ) ;
			content = title.toString( ) + " " +description.toString( ) ;
			//	dcterms:subject <http://dbpedia.org/resource/Computer_science>;
			//	void:dataDump <http://data.nytimes.com/people.rdf>;
			// 	void:exampleResource <http://dbpedia.org/resource/Berlin> ; 
			}catch( Exception e ){
				System.out.println( "\t Problem URL getting VOID: "+ urlc.toString( ) ) ;
			}
			return content ;
	}
	

	private String getCSVContent(URLConnection urlc) {
		String content = "" ;
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy( urlc.getInputStream( ) , writer);
			String theString = writer.toString();
			content = theString.toString().replace(";", " ") ;
		} catch (IOException e) {
			System.out.println( "\t Problem URL getting CSV: "+ urlc.toString( ) ) ;
		}
		return content ;
	}

	private String getHTMLContent(URLConnection urlc) {
		String content = "" ;
		String html = "" ;
		StringWriter writer = new StringWriter();
		try {

				IOUtils.copy( urlc.getInputStream( ) , writer);
				html = writer.toString();
				org.jsoup.nodes.Document doc = Jsoup.parse( html ) ;
				content = ( ( org.jsoup.nodes.Document ) doc ).body( ).text( ) ; 
		} catch ( IOException e ) {
			System.out.println( "\t Problem URL getting HTML: "+ urlc.toString( ) ) ;
		}
		return content ;
	}

	private boolean isVOID(URLConnection urlc) {
		try{
		Model m = FileManager.get().loadModel( urlc.getURL( ).toExternalForm( ) ) ; 
		String VOID = "http://rdfs.org/ns/void#" ;
		Resource dataset = (Resource) m.getResource( VOID + "Dataset" ) ;
		if( dataset != null )
			return true ;
		}catch( Exception e ){
			System.out.println( "\t Problem URL checking VOID: "+ urlc.toString( ) ) ;
		}
		return false ;
	}

	//Verifies if an URL is an SPARQLEndpoint service
	private boolean isSparqlEndpointOnline(URL url){
		String querystr 	= "SELECT ?s WHERE { ?s ?p ?o . } LIMIT 1";
        QueryEngineHTTP qt 	= new QueryEngineHTTP( url.toString( ), querystr ) ;
        try{	ResultSet results = qt.execSelect( ) ;
		        if ( results.hasNext( ) ) {
		            QuerySolution qs = results.next( ) ;
		            Resource  resource = ( Resource ) qs.get( "?s" ) ;
		            if ( resource != null )
		            	return true ;
        }
        }catch( Exception e){
        	System.out.println( "\t Problem URL checking sparqlendpoint: "+ url.toString( ) ) ;
        }
        return false ;
	}
	/*
	 * json file serialized
	 * */
	public void serializeContent( String nameFile, String contentObj )
	{
		try{	FileOutputStream fout = new FileOutputStream( path + nameFile );
				ObjectOutputStream oos = new ObjectOutputStream(fout);   
				oos.writeObject( contentObj );
				oos.close();
		}catch( IOException ex){
			System.out.println( "Error writting dataset file." + ex.toString( ) ) ;
		}
	}
	
	/*
	 * Unserialized
	 * */
	public  JSONObject unSerializeContent( String nameFile )
	{
		JSONObject contentObj = new JSONObject( ) ;
		try{	FileInputStream fin = new FileInputStream( path + nameFile  ) ;
				ObjectInputStream ois = new ObjectInputStream(fin);
				contentObj =  (JSONObject) ois.readObject() ;
				ois.close();
		}catch(IOException ex){
			 System.out.println( "Error reading dataset file." + ex.toString( ) ) ;
		} catch (ClassNotFoundException e){
			e.printStackTrace();
		}
		return contentObj ;
	}
	
	public static String getXMLValues(Node nodes, String content)
	{
		if(nodes.hasChildNodes()  || nodes.getNodeType()!=3){
			NodeList nl=nodes.getChildNodes();
			for( int j=0; j < nl.getLength();j++){
				String tmp = getXMLValues( ( ( Node ) nl.item( j ) ) , " " ) ;
				tmp = tmp.replace("\n", "").replace("\r", "").trim( ) ;
				if( !tmp.isEmpty( ) )
					content += " " + tmp ;
			}
		}else{
			//return nodes.getTextContent( ) ;
		}
		return content ;
	}
	
	public boolean containsIgnoreCase( String s1, String s2)
	{
		return org.apache.commons.lang3.StringUtils.containsIgnoreCase( s1, s2 ) ;
	}
	
	public boolean existFile( String nameFile )
	{
		File f = new File( path + nameFile ) ;
		if( f.exists( ) && !f.isDirectory( ) ) 
			return 	  true ;
		else
			return false ;
	}
	
	private int getFileSize( URL url ) 
	{
	    HttpURLConnection conn = null ;
	    try {	conn = (HttpURLConnection) url.openConnection( ) ;
	    		conn.setConnectTimeout( timeOut ) ;
	    		conn.setReadTimeout( timeOut) ; 
		        conn.setRequestMethod( "HEAD" ) ;
		        conn.getInputStream( ) ;
		        return conn.getContentLength( ) ;
	    } catch ( IOException e) {
	        return -1 ;
	    } finally {
	        conn.disconnect( ) ;
	    }
	}
	public String  unZipFile( URLConnection URLc, String extension ){
		String newFile = " " ;
		String fileName = "http://zbw.eu/beta/external_identifiers/jel/download/jel.ttl.zip" ;
		if( extension.equalsIgnoreCase("zip")){
			try {	InputStream input = new URL( fileName ).openStream( ) ;
					ZipInputStream zin = new ZipInputStream( input ) ;
					ZipEntry ze = null ;
					while ( ( ze = zin.getNextEntry( ) ) != null) {
						FileOutputStream fout = new FileOutputStream( pathTemp + "triple.temp" ) ; 
						for (int c = zin.read( ) ; c != -1; c = zin.read( ) ) {
							fout.write( c ) ;
						}
						fout.close( ) ;
						zin.close( ) ; 
						return pathTemp + "triple.temp" ;
					}
				} catch (FileNotFoundException e) {
					System.out.println( "\t problem reading zip file: " + e.toString( ) ) ;
				} catch (IOException e) {
					System.out.println( "\tproblem reading zip file: " + e.toString( ) ) ;
				}
			}else{	
				try{	ZipEntry ze 			= null;    int sChunk = 8192 ;
						byte[ ] buffer 			= new byte[ sChunk ] ;
						InputStream input 		= new URL( fileName ).openStream( ) ; 
					    GZIPInputStream zipin 	= new GZIPInputStream( input )  ;
					    FileOutputStream out 	= new FileOutputStream( pathTemp + "triple.temp" ) ;
					    int length ;
					    while ( (length = zipin.read( buffer, 0, sChunk ) ) != -1 )
					    	out.write( buffer, 0, length ) ;
					    out.close( ) ;
					    zipin.close( ) ;
					    return pathTemp + "triple.temp" ; 
					} catch (FileNotFoundException e) {
						System.out.println( "\t problem reading gzip file: " + e.toString( ) ) ;
					} catch (IOException e) {
						System.out.println( "\t problem reading gzip file: " + e.toString( ) ) ;
					}
			}
			return newFile ;
	}
}
