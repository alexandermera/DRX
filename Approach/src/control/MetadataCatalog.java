package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import shell.FindUrls;
import metadata.*;

public class MetadataCatalog {
	public  String catalogURL 	= "http://linkeddatacatalog.dws.informatik.uni-mannheim.de/api/1" ;
//	public  String catalogURL 	= "http://datahub.io/api/1" ;
	public  int numberDb 		= 0 ;
	public  int linked2014  	= 0 ;
	
	public Dataset getMetaDataExtras(  Dataset dataset  )
	{	
		  try {
				URL url = new URL(catalogURL + "/rest/dataset/"+ dataset.getIdDataHub( ) ) ;
				HttpURLConnection conn = (HttpURLConnection) url.openConnection( ) ;
				conn.setRequestProperty("Accept", "application/json" ) ;
				conn.setRequestMethod( "GET" ) ;
				if (conn.getResponseCode() != 200) 
					return null ;
		 
				BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream( ) ) ) ) ;
				String output	= "" ;
				String jsonTxt	= "" ;
				
				while ( (output = br.readLine( ) ) != null ){
					jsonTxt += output ;
				}
				JSONObject json 	= (JSONObject) JSONSerializer.toJSON( jsonTxt ) ;
				dataset.setName( json.getString("title") ) ; 
				String u 	= json.getString("url").trim( ) ;
				String rn 	= json.getString("notes_rendered") ;
				String n 	= json.getString("notes") ;
				List <String> r ;
				if ( u != "" && !u.contains( "null" ) ){	
					dataset.setURL( u ) ; 
				}else{
					 if( rn != null){
						 r = FindUrls.extractUrls( rn ) ;
						 if( r.size( ) >0)
							 dataset.setURL( r.get( 0 ) ) ;
					 }else{
						 if ( n!= null){
							 r = FindUrls.extractUrls( n ) ;
							 if( r.size( ) >0)
								 dataset.setURL( r.get( 0 ) ) ;
						 }
					 }
				}
				
				JSONObject array 	= json.getJSONObject( "extras") ;
			    for (  Object e: array.keySet() ){	
			    	if( e.toString( ).contains("link") ){
			    		String link = e.toString( ) ;
			    		link = link.substring( link.indexOf( ":") + 1 ) ;
						dataset.getRelations( ).put( link , "" ) ;
			    	}
			    }
			    JSONArray array1 = json.getJSONArray( "tags") ;
			    if( array1.contains( "LinkedDataCrawl2014" ) ){
			    		dataset.setLinkedDataCrawl2014( true ) ;
			    		linked2014 ++;
			    }
			    JSONArray jsonResouces = json.getJSONArray("resources") ;
			    String rdesc = "", urlSparqlEndpoint ="";
		        for (int i = 0; i < jsonResouces.size( ) ; i++) {
		            JSONObject jsonResource = jsonResouces.getJSONObject( i ) ;
		            Resource  rsrc = new Resource( ) ;
		            if ( jsonResource.get("name") != null)
		            	rsrc.setName( jsonResource.getString("name") ) ;
		            if ( jsonResource.get("id") != null)
		            	rsrc.setId( jsonResource.getString("id") ) ;
		            if ( jsonResource.get("url") != null)
		            	rsrc.setUrl( jsonResource.getString("url") ) ;
		            if ( jsonResource.get("description") != null)
		                rdesc = jsonResource.getString("description") ;
		            if ( jsonResource.get("format") != null )
		            	rsrc.setFormat( jsonResource.getString("format") ) ;
		            if (rsrc.getName( ).contains("SPARQL") || rdesc.contains("SPARQL") || rsrc.getFormat( ).contains("sparql"))
		            	urlSparqlEndpoint = rsrc.getUrl( ) ;
			        if( urlSparqlEndpoint != null && urlSparqlEndpoint.length( ) > 0 ){
			        	   dataset.setSparqlEndpoint( urlSparqlEndpoint ) ;
			        	   String querystr = "SELECT ?s WHERE { ?s ?p ?o . } LIMIT 1";
		                   QueryEngineHTTP qt = new QueryEngineHTTP(dataset.getSparqlEndpoint( ) , querystr);
		                   ResultSet results = qt.execSelect();
		                   if (results.hasNext()) {
		                	   QuerySolution qs = results.next();
		                	   Resource  resource = (Resource) qs.get("?s") ;
		                	   if (resource != null)
		                		   dataset.setOnline( true ) ;
		                   }
		            }
			        dataset.getResources().add( rsrc ) ;
		        }
		  }catch(ClassCastException fn)
		  {
			  fn.printStackTrace( ) ;
		  }
		  catch (MalformedURLException e) 
		  {
			e.printStackTrace();
		  }
	   	  catch (IOException e )
	   	  {		 
			e.printStackTrace( ) ;			
		  }catch (Exception e) {
			  e.printStackTrace( ) ;
		  }
		  return dataset ;
		}
	
		public HashMap< String , Dataset >  getDatasetsLODCloud( ) throws IOException 
		{
			HashMap< String , Dataset > datasets = new HashMap< String, Dataset >( ) ;
			
			try 
			{
				URL url = new URL(catalogURL + "/rest/group/lodcloud" ) ;
				HttpURLConnection conn = (HttpURLConnection) url.openConnection( ) ;
				conn.setRequestMethod( "GET" ) ;
				conn.setRequestProperty("Accept", "application/json" ) ;
		 
				if (conn.getResponseCode() != 200) 
					System.out.println( "eror" ) ;
		 
				BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream( ) ) ) ) ;
		 
				String output	= "" ;
				String jsonTxt	= "" ;
	
				while ( (output = br.readLine( ) ) != null ) 
				{
					jsonTxt += output ;
				}
				
				JSONObject json = (JSONObject) JSONSerializer.toJSON( jsonTxt ) ;
				JSONArray cloudDatasets = json.getJSONArray( "packages") ;
			    
				for ( int i = 0; i < cloudDatasets.size( ) ; i++ )  
			    {	
			    	String dataset = cloudDatasets.getString( i ) ;
			    	datasets.put( dataset, new Dataset(  ) ) ;
			    }
				
			}catch (MalformedURLException e) 
			{
				e.printStackTrace();
			}catch (IOException e )
		   	{		 
				e.printStackTrace( ) ;		 
		   	}
			return datasets ;
		}
		
		//add the databases which are point out for some database but do not have a entry in the catalog.	
		public HashMap<String, Dataset>  fixDatasets( HashMap<String, Dataset> datasets )
		{
			Dataset  dataset ;
			int dbWithoutLinks = 0 ;
			HashMap < String, String > relationships ;
			HashMap < String ,Dataset> hds = new HashMap<String,Dataset>( ) ;
				
			for ( String  dskey : datasets.keySet() ){
				dataset = datasets.get( dskey ) ;
				relationships = dataset.getRelations( ) ;
				if( relationships.size( ) == 0 )
					dbWithoutLinks ++ ;
				
				for (String key : relationships.keySet( ) ){
					key = key.trim( ) ;
					if( !datasets.containsKey( key ) ){	
						dataset   = new Dataset( ) ;
						dataset.setIdDataHub( key ) ;
						dataset.setName( key ) ;
						dataset.setIdNet( "" + numberDb ) ;
						hds.put( key, dataset ) ;
						numberDb ++ ;
					}
				}
			}
			for (String  dskey : hds.keySet( ) ){
				datasets.put( dskey, hds.get( dskey) ) ;
			}
			
			System.out.println( "Total ds: " + datasets.size( ) +"/ ds no links: " + dbWithoutLinks) ;
			return datasets ;
		}
		// Get the datasets from a catalog joinly with its associated metadata
		public HashMap< String , Dataset >  getDatasets( )
		{
			String output	= "" ;
			String jsonTxt	= "" ;
			HashMap< String , Dataset > datasets = new HashMap< String, Dataset >( ) ;
			try{
				URL url = new URL( catalogURL + "/rest/dataset" ) ;
				HttpURLConnection conn = ( HttpURLConnection ) url.openConnection( ) ;
				conn.setRequestProperty( "Accept" , "application/json" ) ;
				conn.setRequestMethod( "GET" ) ;
				if (conn.getResponseCode() != 200 ) 
					System.out.println( "it can't be obtained the datasets!" ) ;
				BufferedReader br = new BufferedReader(new InputStreamReader(( conn.getInputStream( ) ) ) ) ;
				while ( (output = br.readLine( ) ) != null ){
					jsonTxt += output ;
				}
				JSONArray json = (JSONArray) JSONSerializer.toJSON( jsonTxt ) ;
				Dataset dataset ;
				
				for ( int i = 0; i < json.size( ) ; i++ ){
					dataset = new Dataset( ) ;
					String dsid = ( ( String ) json.get( i ) ).trim( ) ;
					dataset.setIdNet( "" + i ) ;
					dataset.setIdDataHub( dsid ) ;
					dataset = getMetaDataExtras( dataset ) ;
					
					if( dataset != null ){
							datasets.put( dsid , dataset ) ;
							numberDb ++ ;
					}		
				}
				
				datasets = fixDatasets( datasets ) ; 
				br.close( ) ;
			}catch (MalformedURLException e) 
			{
				e.printStackTrace();
			}catch (IOException e )
		   	{		 
				e.printStackTrace( ) ;		 
		   	}
			return datasets ;
		}
		
		public  HashMap< String, Set< String > >  communitiesSVG( String SVGFile ) 
		{

			HashMap< String, Set< String > > cloudCommunities = new HashMap<String, Set<String>>( ) ;
			HashMap<String, String> hashColor = new HashMap<String, String>( ) ;
			hashColor.put( "#E8FED3", "publications" ) ;
			hashColor.put( "#F6DEE7", "lifesciences" ) ;
			hashColor.put( "#EAF7F6", "crossdomain" ) ;
			hashColor.put( "#A3FFFA", "socialweb" ) ;
			hashColor.put( "#FEF1B6", "geographic" ) ;
			hashColor.put( "#CEFFEB", "government" ) ;
			hashColor.put( "#E8EDFF", "media" ) ;
			hashColor.put( "#FEEDDF", "usergeneratedcontent" ) ;
			//hashColor.put( "#CCFFFF", "linguistics" ) ;
			
			try {
					File fXmlFile = new File( SVGFile ) ;
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(fXmlFile);
					doc.getDocumentElement().normalize();
					NodeList nList = doc.getElementsByTagName("a");
					
					for (int temp = 0; temp < nList.getLength(); temp++) 
					{
						Node nNode = nList.item(temp);
						
						if (nNode.getNodeType() == Node.ELEMENT_NODE) 
						{
							Element eElement = (Element) nNode;
							String db = eElement.getAttribute("xlink:href").substring( 26 ,eElement.getAttribute("xlink:href").length( ) )  ;
							String color = ((Element) nNode.getChildNodes().item(1).getChildNodes().item(1) ).getAttribute("fill")  ;
							if ( hashColor.containsKey( color ) ) 
							{
								String group = hashColor.get( color ) ;
								if ( cloudCommunities.containsKey( group ) )
								{
									Set <String> set = cloudCommunities.get( group ) ;
									set.add( db ) ;
									cloudCommunities.put( group , set ) ;
								}else
								{
									Set <String> set = new HashSet<String>( ) ;
									set.add( db ) ;
									cloudCommunities.put( group , set ) ;
								}
							}	
						}
					}
			} catch (Exception e) {
			e.printStackTrace();
			}
		
			return cloudCommunities;
			}
		
		
}
