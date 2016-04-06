package control;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.spi.IIORegistry;

import metadata.Dataset;
import model.ConnPg;
import model.FingerPrintManager;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import shell.Util;
import weka.clusterers.XMeans;
import weka.core.Instances;
import weka.core.Attribute;
import weka.core.DenseInstance;
 
public class Clustering {
	// 23 categories following the kawase article
	public static String path 								=  "/Users/AlexanderMera/Dropbox/Approach/WebContent/dataset/" ;
	public static HashMap<String, Integer > topLevelCat23 	= new HashMap<String , Integer>( ) ;
	public HashMap< String, Set<String>> resultXmeans ;
	public Clustering( String path ){
		chargeTopLevelCategories( ) ;
		this.path = path ;
	}

	public boolean executeClustering( HashMap < String, Dataset >  datasets, int nminc, int nmaxc, int seed, boolean normalized ){
		resultXmeans = new HashMap< String, Set< String > >( ) ;
		//Algorith of clustering XMeans select the suitable number of Clusters X
		XMeans Xmeans = new XMeans( ) ;
		// The euclidean distance was change for the Cosine distance.
		CosineDistance cosineDistance = new CosineDistance( ) ;
		Xmeans.setDistanceF( cosineDistance) ;
		Xmeans.setSeed( seed ) ;
		Xmeans.setMinNumClusters( nminc ) ; 
		Xmeans.setMaxNumClusters( nmaxc ) ;
		// Class to retrieve the fingerprint from the databases
		FingerPrintManager mfp = new FingerPrintManager( ) ;
		//datasets included in the clustering
		//important parameter to set: preserver order, number of cluster.
		HashMap< Integer, String > inds = new HashMap <Integer, String> ( ) ;
		//Number of attributes-> number of top level categories
		ArrayList< Attribute > atts = new ArrayList< Attribute >( 23 ) ;
    	String[ ] sortTopLevelCategories = new String[ 23 ] ;
		for(String c:topLevelCat23.keySet( ) ){
			int index = topLevelCat23.get( c ) - 1 ;
			sortTopLevelCategories[ index ] = c ;
		}
		for (int i = 0; i < sortTopLevelCategories.length; i++) {
			atts.add( new Attribute( sortTopLevelCategories[ i ]  ) ) ; 
		}
		Instances data = new Instances( "LODInstances" , atts , 0 ) ;
		int c = 0 ;
		for( String ds: datasets.keySet( ) ){ 
			if ( mfp.existDataset( ds ) ){
				HashMap<String, Double > fp = mfp.readFingerPrintDataset( ds ) ;
				if( !fp.isEmpty( ) ){
					double[ ] instanceValue = new double[ data.numAttributes( ) ] ;
					for( int i = 0 ; i < instanceValue.length ; i++){
						instanceValue[ i ] = 0 ;
					}
					// Normalized the dimension by the size of the vector.
					if ( normalized )
						fp = normalizeVector( fp ) ;
					for (String cat : fp.keySet( ) ) {
			        	int index = topLevelCat23.get( cat ) - 1 ; 
			        	instanceValue[ index ] = fp.get( cat ) ;
					}
					data.add( new DenseInstance( 1.0, instanceValue ) ) ;
					inds.put( c, ds ) ;
					c++ ;
				}
			}
		}
        try {
			Xmeans.buildClusterer( data ) ;
			for( int i = 0 ; i < data.numInstances( ) ; i++ ){
				String cluster = "" + Xmeans.clusterInstance( data.instance( i ) ) ;
				String ds = inds.get( i ) ;
				if ( resultXmeans.containsKey( cluster ) ){
					resultXmeans.get( cluster ).add( ds ) ;
				}else{
					HashSet<String> hs = new HashSet<String>( ) ;
					hs.add( ds ) ; 
					resultXmeans.put( cluster, hs ) ;
				}
			}
		} catch (Exception e) {
			System.out.println("\t Problem with the XMean Clustering:... " +  e.getMessage( ) ) ;
		}
        Util util = new Util( path ) ;
        String clusteringFileName	= nminc + "-" + nmaxc + "-" + seed + ".json" ;
		JSONObject obj = new JSONObject ( ) ;
		JSONObject objs ;
		obj.put("name", "0.0");
		obj.put("idNode", "0.0" ) ;
		JSONArray list = new JSONArray( ) ;
		for ( String cluster : resultXmeans.keySet( ) )  {
			JSONObject objtemp = new JSONObject ( ) ;
			objtemp.put("name",cluster ) ;
			objtemp.put("idNode", cluster ) ;
			JSONArray l = new JSONArray();
			for( String dsname: resultXmeans.get( cluster ) ){
				objs = new JSONObject( ) ;
				objs.put("name", dsname ) ;
				objs.put("size", 100) ;
				l.add( objs ) ;
			}
			objtemp.put( "children", l ) ;
			list.add( objtemp ) ;
		}
		obj.put( "children", list ) ;
		if( util.JSONTofile ( obj , path + "trees/" + clusteringFileName ) )
			return true ;
		else
			return false ;
	}
	public boolean executeClustering( int nminc, int nmaxc, int seed, boolean normalized ){
		resultXmeans = new HashMap< String, Set< String > >( ) ;
		//Algorith of clustering XMeans select the suitable number of Clusters X
		XMeans Xmeans = new XMeans( ) ;
		// The euclidean distance was change for the Cosine distance.
		CosineDistance cosineDistance = new CosineDistance( ) ;
		Xmeans.setDistanceF( cosineDistance) ;
		Xmeans.setSeed( seed ) ;
		Xmeans.setMinNumClusters( nminc ) ; 
		Xmeans.setMaxNumClusters( nmaxc ) ;
		// Class to retrieve the fingerprint from the databases
		FingerPrintManager mfp = new FingerPrintManager( ) ;
		//datasets included in the clustering
		//important parameter to set: preserver order, number of cluster.
		HashMap< Integer, String > inds = new HashMap <Integer, String> ( ) ;
		//Number of attributes-> number of top level categories
		ArrayList< Attribute > atts = new ArrayList< Attribute >( 23 ) ;
		String[ ] sortTopLevelCategories = new String[ 23 ] ;
		for(String c:topLevelCat23.keySet( ) ){
			int index = topLevelCat23.get( c ) - 1 ;
			sortTopLevelCategories[ index ] = c ;
		}
		for (int i = 0; i < sortTopLevelCategories.length; i++) {
			atts.add( new Attribute( sortTopLevelCategories[ i ]  ) ) ; 
		}
		Instances data = new Instances( "LODInstances" , atts , 0 ) ;
		int c = 0 ;
		for( String ds:mfp.listFingerPrintDataset( )  ){ 
			HashMap<String, Double > fp = mfp.readFingerPrintDataset( ds ) ;
			if( !fp.isEmpty( ) ){
				double[ ] instanceValue = new double[ data.numAttributes( ) ] ;
				for( int i = 0 ; i < instanceValue.length ; i++){
					instanceValue[ i ] = 0 ;
				}
				// Normalized the dimension by the size of the vector.
				if ( normalized )
					fp = normalizeVector( fp ) ;
				for (String cat : fp.keySet( ) ) {
					int index = topLevelCat23.get( cat ) - 1 ; 
					instanceValue[ index ] = fp.get( cat ) ;
				}
				data.add( new DenseInstance( 1.0, instanceValue ) ) ;
				inds.put( c, ds ) ;
				c++ ;
			}
		}
		try {
			Xmeans.buildClusterer( data ) ;
			for( int i = 0 ; i < data.numInstances( ) ; i++ ){
				String cluster = "" + Xmeans.clusterInstance( data.instance( i ) ) ;
				String ds = inds.get( i ) ;
				if ( resultXmeans.containsKey( cluster ) ){
					resultXmeans.get( cluster ).add( ds ) ;
				}else{
					HashSet<String> hs = new HashSet<String>( ) ;
					hs.add( ds ) ; 
					resultXmeans.put( cluster, hs ) ;
				}
			}
		} catch (Exception e) {
			System.out.println("\t Problem with the XMean Clustering:... " +  e.getMessage( ) ) ;
		}
		Util util = new Util( path ) ;
		String clusteringFileName	= nminc + "-" + nmaxc + "-" + seed + ".json" ;
		JSONObject obj = new JSONObject ( ) ;
		JSONObject objs ;
		obj.put("name", "0.0");
		obj.put("idNode", "0.0" ) ;
		JSONArray list = new JSONArray( ) ;
		for ( String cluster : resultXmeans.keySet( ) )  {
			JSONObject objtemp = new JSONObject ( ) ;
			objtemp.put("name",cluster ) ;
			objtemp.put("idNode", cluster ) ;
			JSONArray l = new JSONArray();
			for( String dsname: resultXmeans.get( cluster ) ){
				objs = new JSONObject( ) ;
				objs.put("name", dsname ) ;
				objs.put("size", 100) ;
				l.add( objs ) ;
			}
			objtemp.put( "children", l ) ;
			list.add( objtemp ) ;
		}
		obj.put( "children", list ) ;
		if( util.JSONTofile ( obj , path + "trees/" + clusteringFileName ) )
			return true ;
		else
			return false ;
	}
	public static void main(String[] args) throws Exception {
		ConnPg conn = new ConnPg( );
		Util util= new Util( path ) ;
		HashMap < String, Dataset >  datasets = util.unSerializeDatasets( "datasetsBizer.v1" ) ;
		Clustering cl = new Clustering( path  ) ;
		cl.executeClustering( datasets, 2, 10, 10, true ) ;
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

	public HashMap< String, HashMap< String, Double > > getFingerPrints( Set< String > datasets ) {
			// fingerprints of all datasets
			HashMap <String, HashMap< String, Double > >fps = new HashMap< String, HashMap< String, Double > >( ) ;
			// full fingerprint
			HashMap< String, Double > ffp;
			FingerPrintManager mfp = new FingerPrintManager( ) ;
			for( String ds: datasets ){ 
				if ( mfp.existDataset( ds ) ){
					//partial fingerprint
					HashMap<String, Double > fp = mfp.readFingerPrintDataset( ds ) ;
					if( !fp.isEmpty( ) ){
						ffp = new HashMap< String, Double >( ) ;
						for( String c: topLevelCat23.keySet( ) ){
							ffp.put( c , 0.0 ) ;
						}
						for ( String cat : fp.keySet( ) ) {
				        	ffp.put( cat , fp.get( cat ) ) ; 
						}
						ffp = normalizeVector( ffp ) ;
						fps.put( ds, ffp  ) ;
					}
				}
			}
			createCSV( fps ) ;
			return fps ;
	}

	private void createCSV(HashMap<String, HashMap<String, Double> > fps) {
		try
		{	FileWriter writer = new FileWriter( path + "trees/dataParallel.csv" ) ;
			String[ ] topLevelCat23 = {"Agriculture", "Applied science", "Arts", "Belief", "Business", "Chronology", "Culture",
		                     "Education", "Environment", "Geography", "Health", "History", "Humanities", "Language",
		                     "Law", "Life", "Mathematics", "Nature", "People", "Politics", "Science", "Society", "Technology" } ;
			String[ ] topLevelCat23Short = {"Agr", "Asc", "Art", "Bel", "Bus", "Chr", "Cul",
					"Edu", "Env", "Geo", "Hea", "His", "Hum", "Lan",
					"Law", "Lif", "Mat", "Nat", "Peo", "Pol", "Sci", "Soc", "Tec" } ;
			writer.append( "name" ) ;
			writer.append( ',' ) ;
			for( int i = 0 ; i <  topLevelCat23Short.length ; i++ ){
				writer.append( topLevelCat23Short[ i ] ) ; 
				if( i < topLevelCat23Short.length -1 )
					writer.append( ',' ) ;
			}
		    writer.append( '\n' ) ;
		    for( String ds: fps.keySet( ) ){
		    	HashMap<String, Double> hmds = fps.get( ds ) ;
		    	writer.append( ds ) ;
		    	writer.append( ',' ) ;
				for( int i = 0 ; i <  topLevelCat23.length ; i++ ){
		    		writer.append( "" + hmds.get( topLevelCat23[ i ] ) ) ;
					if( i < topLevelCat23.length -1 )
						writer.append( ',' ) ;
		    	}
		    	writer.append('\n');
		    }
		    writer.flush();
		    writer.close();
		}catch(IOException e){
		   System.out.println( "Problem writing the dataParallel file: " + e.getMessage( ) ) ;
		} 
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
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    long factor = (long) Math.pow(10, places);
	    value = value * factor;
	    long tmp = Math.round(value);
	    return (double) tmp / factor;
	}

	/**
	 * @return the resultXmeans
	 */
	public HashMap<String, Set<String>> getResultXmeans() {
		return resultXmeans;
	}

	/**
	 * @param resultXmeans the resultXmeans to set
	 */
	public void setResultXmeans(HashMap<String, Set<String>> resultXmeans) {
		this.resultXmeans = resultXmeans;
	}
}