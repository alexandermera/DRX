package shell;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.poi.util.SystemOutLogger;
import org.apache.xmlbeans.impl.xb.xsdschema.TopLevelAttribute;

import java.util.Map.Entry;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import control.CosineSimilarity;
import metadata.Dataset;
import model.*;
import control.*;


public class Util 
{
	public  boolean ASC = true;
    public  boolean DESC = false;
	public static String path 				=  "/Users/AlexanderMera/Dropbox/Approach/WebContent/dataset/" ;
	public static  HashMap < String, metadata.Dataset > datasets = new HashMap<String, metadata.Dataset>( ) ;
	public static  HashMap<String, Integer > topLevelCat23 	= new HashMap<String , Integer>( ) ;
	public static  HashMap< String, Set< String > > communitiesBizer ;
	
	public Util(String path) {
		this.path = path ;
	}

	public static void main( String[ ] args )
	{
		ConnPg conn = new ConnPg( );
		Util util= new Util( path ) ;
		datasets = util.unSerializeDatasets( "datasetsBizer.v1" ) ;
		MetadataCatalog mCatalog = new MetadataCatalog( ) ;
		communitiesBizer = mCatalog.communitiesSVG( path + "lod-cloud_colored.svg" ) ;
		util.chargeTopLevelCategories();
		SortedSet<String> keys = new TreeSet<String>(topLevelCat23.keySet());
		for (String key : keys) { 
		   System.out.print( key + ", ");
		   // do something
		}
		// Consider only datasets from the catalog listed in the LOD
		HashMap < String, Dataset > datasetsFiltered = getDatasetLODFingerprint( datasets, communitiesBizer ) ;
		// Consider only datasets from the catalog
		//HashMap < String, Dataset > datasetsFiltered = getDatasetFingerprint( datasets ) ;
		
		//util.datasetsValidationFingerPrintGoldenStandard( datasetsFiltered, DistanceMeasure.Euclidean ) ;
		
		//util.datasetsValidationFingerPrintWithOutClusters( datasetsFiltered, DistanceMeasure.Cosine ) ;
		
	//	util.calculateFingerPrintDataset( datasets ) ;

		//Catalog cat = new Catalog( ) ;
		//cat.getContentDasets( datasets ) ;
	}

	// dataset belongin to the LOD diagram with fingerprint
	private static HashMap<String, Dataset> getDatasetLODFingerprint(HashMap<String, Dataset> datasets2,
			HashMap<String, Set<String>> communitiesBizer2) {
		
		FingerPrintManager mfp = new FingerPrintManager( ) ;
		HashMap<String, Dataset> filteredDataset = new HashMap<String, Dataset>( ) ;
		
		for(String  c: communitiesBizer2.keySet( ) ) {
			Set<String> community = communitiesBizer2.get( c ) ; 
			for( String dsName:community ){
					if ( !mfp.readFingerPrintDataset( dsName ).isEmpty( ) )
						filteredDataset.put( dsName , datasets2.get( dsName ) ) ;
			}
		}
		
		return filteredDataset ;
	}
	// datasets with fingerprint
	public HashMap<String, Dataset> getDatasetFingerprint(HashMap<String, Dataset> datasets2) {
		
		FingerPrintManager mfp = new FingerPrintManager( ) ;
		HashMap<String, Dataset> filteredDataset = new HashMap<String, Dataset>( ) ;
		
		for(String  dsName: datasets2.keySet( ) ) {
				if ( !mfp.readFingerPrintDataset( dsName ).isEmpty( ) )
					filteredDataset.put( dsName , datasets2.get( dsName ) ) ;
		}
		
		return filteredDataset ;
	}
	
	//ranking for members of a cluster cluster by a distance measure
	public HashMap<String, Double> rankingCluster(HashMap<String, HashMap<String, Double>> fps, String dsReference , DistanceMeasure dm ){
		HashMap < String, Double > rankingList = new HashMap<String, Double>( ) ;
		HashMap<String, Double > fpReference = fps.get( dsReference ) ;
		HashMap<String, Double > fp ;
		for(String ds:fps.keySet( ) ){
			if( !ds.equalsIgnoreCase( dsReference ) ){
				fp = new HashMap<String, Double>( ) ;
				fp =  fps.get( ds ) ;
				if ( !fpReference.isEmpty( ) && !fp.isEmpty( ) ){
					if ( dm ==  DistanceMeasure.Euclidean)
						 rankingList.put( ds, getEuclideanDistance( fpReference, fp ) ) ;
					else
						rankingList.put( ds, getCosineDistance( fpReference, fp ) ) ;	
				}
			}
		}
		return sortByComparator( rankingList, ASC ) ;
	}

	public java.lang.Double getCosineDistance(HashMap<java.lang.String, java.lang.Double> fp,
			HashMap<java.lang.String, java.lang.Double> fp2) {
		chargeTopLevelCategories(  ) ;
		double distance = 0.00000 ;
		distance = 1 - CosineSimilarity.calculateCosineSimilarity( fp, fp2 ) ;
		return distance ;
	}
	
	public java.lang.Double getEuclideanDistance(HashMap<java.lang.String, java.lang.Double> fp,
			HashMap<java.lang.String, java.lang.Double> fp2) {
		chargeTopLevelCategories(  ) ;
		double distance = 0.00000 ;
		try{
			//fp = normalizeVector( fp ) ;
			//fp2 = normalizeVector( fp2 ) ;
			
			for( String category:topLevelCat23.keySet( ) ){
				double v 	= 0.00000 ;
				double v2 	= 0.00000 ;
				if(fp.containsKey( category )  ) 
					v = fp.get ( category ) ;
				if(fp2.containsKey( category ) ) 
					v2 = fp2.get ( category ) ;
				distance = distance + ( Math.pow( v  - v2 , 2 ) ) ;
			}
		}
		catch( Exception e ){
			System.out.println("\t Problem calculating the Euclidean Measure") ;
		}
		return Math.sqrt( distance ) ;
	}
	

	
	public void calculateFingerPrintDataset(HashMap<String, Dataset> datasets2 ){
		Fingerprint fp ;
		FingerPrintManager managerFingerprint = new FingerPrintManager( ) ;
		int cont = 0 ;
		for( String ds:datasets.keySet(  ) ){
			if( !managerFingerprint.existDataset( ds ) ){
				cont ++ ;
				fp = new Fingerprint( ) ;
				HashMap< String, Double > fpds = fp.calculateFingerPrint( (metadata.Dataset) datasets.get( ds ) ) ;
				if( fpds != null){
					if( !managerFingerprint.existDataset( ds ) ){
						managerFingerprint.insertFingerPrintDataset( ds, fpds ) ;
						System.out.println( cont + ". " + ds + ":" + fpds ) ;
					}
				}
				System.out.println("ds processados: " + cont ) ;
			}
		}
	}
	
	 String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes( Paths.get( path) ) ;
			  return new String(encoded, encoding);
	}
	
	public HashMap<String, Double> getRankingbyNode(String fileName, String idNode) {
		 HashMap <String, Double> ranking = new HashMap<String, Double>( ) ;
		 ArrayList<String> dsName = new ArrayList< String > ( ) ;
		 boolean add = false ;
		 try{
			 	String  content =  readFile( path + "trees/" + fileName , StandardCharsets.UTF_8 ) ;
			 	JSONObject jsonObject=  JSONObject.fromObject( content ) ;
	            dsName = getJSONds( idNode, jsonObject , dsName, add ) ;
	    		datasets = unSerializeDatasets( "datasetsBizer.v1" ) ;
	    		for (String dsa : dsName) {
					ranking.put( dsa , getDegreeCentrality( datasets , dsa , dsName ) );
				}

		 }catch( Exception e){
			 e.printStackTrace( ) ;
		 }
		return sortByComparator( ranking, DESC ) ;
	}
	
	// get all datasets  from a cluster 
	public Set<String> getDsbyCluster( String fileName, String idNode){
		ArrayList<String> dsName = new ArrayList< String > ( ) ;
	 	String content = "" ;
		try {
			content = readFile( path + "trees/" + fileName , StandardCharsets.UTF_8 );
		} catch (IOException e) {
			System.out.println( "\tProblem getting members cluster.");
		}
	 	JSONObject jsonObject=  JSONObject.fromObject( content ) ;
	 	boolean add = false ;
        dsName = getJSONds( idNode, jsonObject , dsName, add );
        Set<String> set = new HashSet<String>(dsName);
        return set ;
	}
	
	public HashMap<String, String> getIdClusters(String fileName ){
		String idNode = "0.0" ; //get the id for all nodes from the root node
		HashMap<String, String> dsClusterIds = new HashMap<String, String>  ( ) ;
		 try{
			 	String  content =  readFile( path + "trees/" + fileName , StandardCharsets.UTF_8 ) ;
			 	JSONObject jsonObject=  JSONObject.fromObject( content ) ;
			 	dsClusterIds = getJSONdsClusterIds( idNode, jsonObject , dsClusterIds ) ;
		 }catch( Exception e){
			 e.printStackTrace( ) ;
		 }
		return dsClusterIds ;
	}
	
	private HashMap<String, String>  getJSONdsClusterIds(String idNode, JSONObject jsonObject, HashMap<String, String> dsClusterIds) {
		JSONArray children = (JSONArray) jsonObject.get( "children" ) ;
		
		if( children != null ){
			for ( int i = 0 ; i< children.size( ) ; i++ ) {
					getJSONdsClusterIds( jsonObject.getString("idNode"), (JSONObject) children.get( i ) , dsClusterIds ) ;
				}	
		}else{
			dsClusterIds.put( jsonObject.getString( "name"), idNode ) ;
		}
		return dsClusterIds ;
	}
	
	private Double getDegreeCentrality(HashMap<String, Dataset> datasets, String dsa, ArrayList<String> dsName) {
		Double in, out ;
		in = out = 0.0 ;
		for (String dsq: dsName){
			if ( dsa != dsq ){
				Dataset ds = datasets.get( dsq ) ;
				if ( ds.getRelations().containsKey( dsa ) )
					in ++ ;
				ds = datasets.get( dsa ) ;
				if ( ds.getRelations().containsKey( dsq ) )
					out ++ ;
			}
		}
		return in + out ;
	}

	private ArrayList<String> getJSONds(String idNode, JSONObject jsonObject, ArrayList<String> dsName, boolean add) {
		
			 if(jsonObject.get("idNode") !=  null){
				 if ( jsonObject.get("idNode").equals( idNode ) )
					 add = true ;
			 }
			
				JSONArray children = (JSONArray) jsonObject.get( "children" ) ;
				if( children != null ){
					for ( int i = 0 ; i< children.size( ) ; i++ ) {
						if ( add ){
							getJSONds( idNode, (JSONObject) children.get( i ) , dsName, true ) ;
						}
						else{
							getJSONds( idNode, (JSONObject) children.get( i ) , dsName, false ) ;
						}	
					}
				}else{
						if ( add ){
							dsName.add( jsonObject.getString( "name") ) ;
					}
				}
		return dsName ;
	}

	public boolean JSONTofile(JSONObject obj, String fullfileName) {
		FileWriter file = null ;
		boolean ok		= false ;
        try {
        		file = new FileWriter( fullfileName  );
        		file.write( obj.toString( ) ) ;
        		System.out.println("Successfully Copied JSON Object to File...");
        		ok = true ;
        } catch (IOException e) {
            e.printStackTrace();
 
        } finally {
            try {
				file.flush();
				file.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }		
        return ok ;
	}

	public boolean isBizerDataset( String  ds ){
		if( ds!=null){
			for (String community : communitiesBizer.keySet( ) ) {
				if ( communitiesBizer.get(community).contains( ds ) )
					return  true ;
			}
		}
		return  false ;
	}
	
	//SORT SCORE LIST
	public	HashMap<String, Double> sortByComparator(HashMap<String, Double> unsortMap, final boolean order)
    {

		List< Entry<String, Double> > list = new LinkedList< Entry< String, Double > > (unsortMap.entrySet ( ) ) ;
		
		// Sorting the list based on values
		Collections.sort(list, new Comparator< Entry<String, Double> >( ){
		public int compare(Entry<String, Double> o1,Entry<String, Double> o2)
			{
				if (order)
					return o1.getValue().compareTo(o2.getValue( ) ) ;
				else
					return o2.getValue().compareTo(o1.getValue( ) ) ;
		}});
		
		// Maintaining insertion order with the help of LinkedList
		HashMap<String, Double> sortedMap = new LinkedHashMap<String, Double>( ) ;
		
		for (Entry<String, Double> entry : list)
		{ sortedMap.put( entry.getKey( ), entry.getValue( ) ) ;
		}
		
		return sortedMap;
    }

	// this method get all the datasets by category from the LOD-Cloud



	public HashMap<String, Set< String > > getCommunitiesBizer(String csvFile) 
	{
		HashMap< String, Set< String > > cloudCommunities = new HashMap<String, Set<String>>( ) ;
		
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "\t";
		
		try 
		{
	 		br = new BufferedReader( new FileReader( csvFile ) ) ;
	 		
			while ( ( line = br.readLine( ) ) != null ) 
			{
		       String[ ] dbRow = line.split( cvsSplitBy ) ;
		       String group    = dbRow[ 1 ].trim( ) ;
		       String db       = dbRow[ 0 ].trim( ) ;
		       
		       if ( cloudCommunities.containsKey( group ) )
		       {
		    	   Set <String> set = cloudCommunities.get( group ) ;
		    	   set.add( db ) ;
		    	   cloudCommunities.put( group, set ) ;
		       }else
		       {
		    	   Set <String> set = new HashSet<String>( ) ;
		    	   set.add( db ) ;
		    	   cloudCommunities.put( group, set ) ;
		       }
			}
	 
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace( ) ;
		} catch (IOException e) 
		{
			e.printStackTrace( ) ;
		} finally 
		{
			if (br != null) 
			{
				try 
				{
					br.close( ) ;
				} catch (IOException e) 
				{
					e.printStackTrace( ) ;
				}
			}
		}
		return cloudCommunities ;
	}
	
	/*
	 * serialized
	 * */
	public void serializeDatasets( HashMap< String , Dataset > datasets, String fileName )
	{
		try{
			FileOutputStream fout = new FileOutputStream( path + fileName );
			ObjectOutputStream oos = new ObjectOutputStream(fout);   
			oos.writeObject( datasets );
			oos.close();
			System.out.println("Done");
		}
		catch( IOException ex){
			System.out.println( "Error writting dataset file." + ex.toString( ) ) ;
		}
	}
	
	/*
	 * Unserialized
	 * */
	public  HashMap< String, Dataset > unSerializeDatasets( String nameFile )
	{
		HashMap< String, Dataset > datasets = new HashMap<String, Dataset>( ) ;
		
		try
		{
			FileInputStream fin = new FileInputStream( path + nameFile  ) ;
			ObjectInputStream ois = new ObjectInputStream(fin);
			datasets =  (HashMap<String, Dataset>) ois.readObject() ;
			ois.close();
		  
		}catch(IOException ex)
		{
			 System.out.println( "Error reading dataset file." + ex.toString( ) ) ;
		} catch (ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
		
		return datasets ;
	}
	
		public  String datasetToString( Dataset dataset ) {
		    return  String.format( " title: " + dataset.name +"\n idDataHub: " + dataset.idDataHub +"\n sparqlendpoint: " + dataset.sparqlEndpoint +
		    		"\n resources: " + dataset.resources.toString( ) + "\n relationships: " + dataset.relations.toString( ) ) ;
		}
		
		private HashMap<String, Double> normalizeVector( HashMap<String, Double> ffp) {
			double total = 0.00000 ;
			for (String cat:ffp.keySet( ) ) {
				total += Math.pow( ffp.get( cat ), 2 ) ;
				//System.out.println( "valor: " + ffp.get( cat ) +" / "+ Math.pow( ffp.get( cat ), 2 ) +" / total: " + total);
			}
			//System.out.println( "antes:" + ffp.toString( ) ) ;
			total = Math.sqrt( total ) ;
			for( String cat:ffp.keySet( ) ){
				ffp.put(cat, round( ( ffp.get( cat ) / total ) , 2 ) ) ;
			}
			//System.out.println( "despues:" + ffp.toString( ) ) ;
			return ffp ;
		}
		public  double round(double value, int places) {
		    if (places < 0) throw new IllegalArgumentException();

		    long factor = (long) Math.pow(10, places);
		    value = value * factor;
		    long tmp = Math.round(value);
		    return (double) tmp / factor;
		}
		private void chargeTopLevelCategories( ) {
			// top level categories base on the paper Kawase
			topLevelCat23.put("Agriculture", 1 ) ; 	topLevelCat23.put("Applied science", 2 ) ;	topLevelCat23.put("Arts", 3 ) ;
			topLevelCat23.put("Belief", 4 ) ; 		topLevelCat23.put("Business", 5 ) ;			topLevelCat23.put("Chronology", 6 ) ; 
			topLevelCat23.put("Culture", 7 ) ; 		topLevelCat23.put("Education", 8 ) ; 		topLevelCat23.put("Environment", 9 ) ;
			topLevelCat23.put("Geography", 10 ) ;	topLevelCat23.put("Health", 11 ) ; 			topLevelCat23.put("History", 12 ) ;
			topLevelCat23.put("Humanities", 13 ) ; 	topLevelCat23.put("Language", 14 ) ; 		topLevelCat23.put("Law", 15 ) ;
			topLevelCat23.put("Life", 16 ) ; 		topLevelCat23.put("Mathematics", 17 ) ; 	topLevelCat23.put("Nature", 18 ) ;
			topLevelCat23.put("People", 19) ; 		topLevelCat23.put("Politics", 20 ) ; 		topLevelCat23.put("Science", 21 ) ;
			topLevelCat23.put("Society", 22 ) ; 	topLevelCat23.put("Technology", 23 ) ; 
		}

		public HashSet<String> getEntitiesByDataset(String dsName) {
			HashSet<String>  entities ;
			DatasetEntityManager dem = new DatasetEntityManager( ) ;
			entities = dem.readDatasetEntity( dsName ) ;
			return entities ;
		}

		public HashSet<String> getCategoriesByDataset(String dsName) {
			HashSet<String>  entities ;
			HashSet<String>  categories = new HashSet<String>( ) ;
			DatasetEntityManager dem = new DatasetEntityManager( ) ;
			EntityCategoryManager ecm = new EntityCategoryManager( ) ;
			entities = dem.readDatasetEntity( dsName ) ;
			
			for(String e:entities){
				HashSet<String> catEntity = ecm.readEntityCategory( e ) ;
				for( String ce:catEntity){
					categories.add( ce ) ;
				}
			}
			return categories ;
		}

		public HashMap<String, Integer> getGoldStandard(String dsName, Set<String> dsList) {
			HashMap<String, Integer> gs = new HashMap<String, Integer>( ) ;
    		datasets = unSerializeDatasets( "datasetsBizer.v1" ) ;
    		HashMap<String, String> relations = datasets.get(dsName).getRelations( ) ;
			for (String r:dsList ){
				if( relations.containsKey( r ) ) {
					gs.put( r , 1 ) ;
				}else{
					gs.put( r , 0 ) ;
				}
			}
			return  gs ;
		}
}