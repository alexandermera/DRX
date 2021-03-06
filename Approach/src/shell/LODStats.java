package shell;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;

import metadata.Dataset;
import model.FingerPrintManager;

public class LODStats {
	public   String labels[ ] ;
	public  int lsize[ ] ;
	public static  HashMap < String, Dataset > datasets = new HashMap<String, Dataset>( ) ;
	public static String path 	=  "/Users/AlexanderMera/Dropbox/Approach/WebContent/dataset/" ;
	public static  HashMap< String, Set< String > > communitiesBizer ;

	
	private void getDeegreDistribution(HashMap<String, Set<String>> cloudCommunities) {
		Util u = new  Util(path) ;
		HashMap<Integer, Integer> outlinks , inlinks  ;
		HashMap<Integer, Integer> outCommunitylinks , inCommunitylinks  ;
		HashSet<String>  linkset = new HashSet<String> ( ) ; 
		HashSet<String>  dsIsolated = new HashSet<String> ( ) ; 
		HashMap< String,Integer>  max = null ;
		int  nc ,ndc, m ;
		nc = ndc = m =  0 ;
		outlinks = new HashMap<Integer, Integer>( ) ;
		int tout = 0, tin = 0 ;
		
		for (String community : cloudCommunities.keySet(  ) ) {
			outCommunitylinks = new HashMap<Integer, Integer>( ) ;
			m = 0 ;
			tout = 0 ;
			for(String ds: cloudCommunities.get( community) ) {
				if ( datasets.containsKey( ds ) ) {
					Dataset dataset = datasets.get( ds ) ; 
					for (String r: dataset.getRelations().keySet( ) ){
						if( datasets.containsKey( r ) ){
							if ( u.isBizerDataset(r) ){
								tout++;
								if( !(linkset.contains(ds+"-"+r) || linkset.contains(r+"-"+ds))){
									nc ++  ;
									linkset.add(ds+"-"+r) ;
								}else{
									ndc ++ ;
								}
								
							} 
						}
					}
					
					int nrelation = dataset.getRelations().size( ) ;
					if ( nrelation > m ){
						max = new HashMap<String, Integer> ( ) ;
						max.put(ds, nrelation) ;
						m = nrelation ;
					}
						
					if ( outlinks.containsKey( nrelation ) )
						outlinks.put( nrelation, outlinks.get(nrelation) + 1 ) ;
					else
						outlinks.put(nrelation, 1 ) ;
					
					if ( outCommunitylinks.containsKey( nrelation ) )
						outCommunitylinks.put( nrelation, outCommunitylinks.get(nrelation) + 1 ) ;
					else
						outCommunitylinks.put(nrelation, 1 ) ;
					
					if( nrelation == 0 )
						dsIsolated.add ( ds ) ;
				}
			}
			//total out linksets per domain
			System.out.println(community + ": "+ tout ) ;
			/*System.out.println("outlinks " + community ) ;
			for (Integer out : outCommunitylinks.keySet( ) ) {
				System.out.println( out + " " + outCommunitylinks.get( out ) ) ;
			}
			nc = 0 ;*/
		}
		
		inlinks = new HashMap<Integer, Integer>( ) ;
		System.out.println("-------------------------------------------------");
		for (String community : cloudCommunities.keySet(  ) ) {
			m = 0 ;
			tin= 0;
			for(String ds:cloudCommunities.get( community ) ){
				int in = 0 ;
				if ( datasets.containsKey( ds ) ){
					for (String c : cloudCommunities.keySet(  ) ) {
						for(String db:cloudCommunities.get( c ) ){
							if ( datasets.containsKey( db ) ){
								if(u.isBizerDataset(db)){
								if( datasets.get( db ).getRelations().containsKey( ds ) )
									tin ++;
									in ++ ;
								}
							}
						}
					}
				}
				if ( in > m ){
					max = new HashMap<String, Integer> ( ) ;
					max.put(ds, in ) ;
					m = in ;
				}
				
				if ( inlinks.containsKey( in ) ){
					inlinks.put( in, inlinks.get( in ) + 1 ) ;
				}else{
					inlinks.put( in , 1 ) ;
				}
				
				if( dsIsolated.contains( ds ) ){
					if ( in > 0 )
						dsIsolated.remove( ds ) ;	
				}
			}
			//System.out.println(community + ": " + max.toString( ) ) ;
			System.out.println(community + ": "+ tin ) ;

		}
		
		System.out.println("outlinks");
		for (Integer out : outlinks.keySet( ) ) {
			System.out.println( out + " " + outlinks.get( out ) ) ;
		}
		
		System.out.println("inlinks");
		for (Integer in : inlinks.keySet( ) ) {
			System.out.println( in + " " + inlinks.get( in ) ) ;
		}
		
		System.out.println("dataset isolados: #" + dsIsolated.size( ) + " {" + dsIsolated.toString( ) ) ;
		System.out.println( "all outlinks: " + outlinks.toString( ) ) ; 
				
	}
	
	private void getCommunitiesBizerInfo(String csvFile) 
	{
		HashMap< String, Set< String > > cloudCommunities = new HashMap<String, Set<String>>( ) ;
		
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "\t";
		boolean firstLine = true ;
		try 
		{
	 		br = new BufferedReader( new FileReader( csvFile ) ) ;
	 		
			while ( ( line = br.readLine( ) ) != null ) 
			{
				if ( !firstLine )
				{
					String[ ] dbRow = line.split( cvsSplitBy ) ;
					String group    = dbRow[ 1 ].trim( ) ;
					String db       = dbRow[ 0 ].trim( ) ;
					int out			= Integer.parseInt( dbRow[ 2 ] .trim( ) ) ;
					int in			= Integer.parseInt( dbRow[ 3 ] .trim( ) ) ;
					
					if( in > 0 || out > 0)
					{
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
				}
				
				firstLine = false ;
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
		
		System.out.println("Linked Data by Domain with at least one linked datasets ");
		Util u = new Util( path );
		System.out.println("----------------------------------");
	}
	
	private int[ ][ ] contingencyTable(HashMap<String, Set< String > > rawCommunities,
			
			HashMap<String, Set< String > > cloudCommunities) 
	{
		int[ ][ ] mCoocorrencia = new int [cloudCommunities.size( ) + 1 ] [ rawCommunities.size( ) + 1 ] ;
		labels = new String[ cloudCommunities.size( ) ] ;
		lsize = new int[ cloudCommunities.size( ) ] ;
		int i, j = 0 ;
	//	System.out.println("--------------------------------------------------------------------------------------------------");
		String line = "content/community" ;
		
		for ( i = 0 ; i < rawCommunities.size( ) ; i++){
			line = line + "\t"+ i ;
		}
		//System.out.println( line ) ;
		i= 0 ;
		
		for( String content: cloudCommunities.keySet( ) ){
			Set<String> community = cloudCommunities.get( content ) ;
			content = String.format("%-20s", content);
			line = "" + content ;
			labels [ i ] = content ;
			lsize [ i ] = community.size( ) ;
			
			for ( String c: rawCommunities.keySet() ) {
				Set<String> rawCommunity = rawCommunities.get( c ) ;
				int inter = Sets.intersection( community, rawCommunity ).size( )  ;
				if( inter > 80 ){
					System.out.println( Sets.difference( community, rawCommunity ).toString( ) ) ;
				}
				j = Integer.parseInt( c ) ;
				mCoocorrencia[ i ][ j ] = inter ;
			}
			
			for( j = 0 ; j < mCoocorrencia[ 0 ].length-1; j++ ){
				line = line + "\t" + mCoocorrencia[ i ] [ j ] ; 
			}
		//	System.out.println( line ) ;
			i++ ;
		}
		return mCoocorrencia ;
	}
	
	private void stats( HashMap<String, Dataset> datasets2 ) {
		// Manager to query the fingerprints in the database
		FingerPrintManager mfp = new FingerPrintManager( ) ;
		//datasets with fingerprint
		ArrayList< String > dsFingerPrint = new ArrayList< String > () ;
		ArrayList< String > dsRelations = new ArrayList< String > () ;
		//datasets with fingerprint and relationships > 0
		ArrayList< String > dsFingerPrintRelation = new ArrayList< String > ( ) ;
		HashMap < String, String > relationships ;
		HashMap < String, String > totalRelationships =  new HashMap<String, String>( ) ; ;
		for(String dsname:datasets2.keySet( ) ){
			relationships = new HashMap<String, String>( ) ;
			relationships = datasets2.get( dsname ).getRelations( ) ;
			if( mfp.existDataset( dsname ) ){
				dsFingerPrint.add( dsname ) ;
				if(  relationships.size( ) > 0  ){
					dsFingerPrintRelation.add( dsname ) ;
					for( String r: relationships.keySet( ) ) {
						if ( !totalRelationships.containsKey( dsname + "-" + r ) && !totalRelationships.containsKey( r + "-" + dsname ) )
							totalRelationships.put( dsname + "-" + r, "" ) ;
					}
				}
			}
			
			if(  relationships.size( ) > 0  )
				dsRelations.add( dsname ) ;
		}
		System.out.println("Total dataset with: ") ;
		System.out.println("\t links: " + dsRelations.size( ) )  ;
		System.out.println("\t fps: " + dsFingerPrint.size( ) )  ;
		System.out.println("\t fp & links: " + dsFingerPrintRelation.size( ) )  ;	
	}
	private void linksetsProportion( ) {
		HashMap<String, String > linksets = new HashMap<String, String>( ) ;
		HashMap<String, Integer> contadores = new HashMap<String, Integer>( ) ;
		int totalLinks = 0 ;
		for( String dsname:datasets.keySet( ) ) {
			String ct = "" ;
			String cs = "" ; 
			Dataset ds = datasets.get( dsname ) ;
			for( String c:communitiesBizer.keySet( ) ) {
				if( communitiesBizer.get( c ) .contains( dsname ) ){
					cs = c ;
					break ;
				}
			}
			if(cs.isEmpty( ) ) continue ;
			
			for( String l:ds.getRelations( ).keySet( ) ){
				if( !linksets.containsKey( dsname + ":" + l ) && !linksets.containsKey( l + ":" +  dsname ) ){
					linksets.put( dsname + ":" + l, "") ;
					for( String c:communitiesBizer.keySet( ) ) {
						if( communitiesBizer.get( c ) .contains( l ) ){
							ct = c ;
							break ;
						}
					}
					if( ct.isEmpty( ) ) continue ;
					if( !contadores.containsKey( cs + ":" +ct ) && !contadores.containsKey( ct + ":" +cs ) ){
						contadores.put( cs + ":" + ct, 1 ) ;
						totalLinks ++ ;
					}else{
						Integer cont = contadores.get( cs + ":" +ct ) != null ? contadores.get( cs + ":" +ct ) : contadores.get( ct + ":" +cs ) ;
						String key = contadores.get( cs + ":" +ct ) != null ? cs + ":" +ct  :  ct + ":" +cs ;
						contadores.put( key , cont + 1 ) ;
						totalLinks ++ ;
					}
				}
			}
			
		}
		
		for(String cont:contadores.keySet( ) ) {
			System.out.println( cont + "," +contadores.get( cont ) ) ; 
		}
		
		System.out.println( "total Linksets: " + totalLinks ) ; 
 }
}
