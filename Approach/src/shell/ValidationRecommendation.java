package shell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import control.Clustering;
import control.DistanceMeasure;
import metadata.Dataset;
import model.FingerPrintManager;

public class ValidationRecommendation {
	public static String path 				=  "/Users/AlexanderMera/Dropbox/Approach/WebContent/dataset/" ;
	public  boolean ASC = true;
    public  boolean DESC = false;

	
	private void datasetsValidationFingerPrintWithOutClusters(HashMap<String, Dataset> datasets2, DistanceMeasure dm ) {
		
		double k ;
		double preK ;
		double linksfounded	 ;
		double totalRelevant ;
		double tmplinksfounded ;
		FingerPrintManager mfp = new FingerPrintManager( ) ;
		Util u =  new Util( path ) ;
		HashMap < String, Double > results = new HashMap<String, Double>( ) ;
		HashSet < String > interval1 = new HashSet<String>( ) ;
		HashSet < String > interval2 = new HashSet<String>( ) ;
		HashSet < String > interval3 = new HashSet<String>( ) ;
		
		results.put("0-10", 0.000 ) ;
		results.put("11-20", 0.000 ) ;
		results.put("21-30", 0.000 ) ;
		results.put("31-40", 0.000 ) ;
		results.put("41-50", 0.000 ) ;
		results.put("51-60", 0.000 ) ;
		results.put("61-70", 0.000 ) ;
		results.put("71-80", 0.000 ) ;
		results.put("81-90", 0.000 ) ;
		results.put("91-100", 0.000 ) ;
		
		for( String member: datasets2.keySet( ) ){
			HashMap<String, Double> rankingList = new HashMap<String, Double>( ) ;
			HashMap< String, Double > fp = mfp.readFingerPrintDataset( member ) ;
			//Calculate the distance from member to the others dataset in the cluster
			for( String otherMember:datasets2.keySet( ) ){
				if( member != otherMember){
					HashMap< String, Double > fp2 = mfp.readFingerPrintDataset( otherMember ) ;
					if ( dm == DistanceMeasure.Euclidean)
						rankingList.put( otherMember, u.getEuclideanDistance( fp, fp2 ) ) ;
					else
						rankingList.put( otherMember, u.getCosineDistance( fp, fp2 ) ) ;
				}
			}
			
			if( !rankingList.isEmpty( ) ){
				// ground truth relationships
				totalRelevant = 0 ;
				HashMap<String, String> relations = datasets2.get( member ).getRelations( ) ;
				HashMap<String, String> gt = new HashMap<String, String>( ) ;
				for( String r : relations.keySet( ) ){
						if ( datasets2.containsKey( r ) ) {
							totalRelevant ++ ;
							gt.put(r, r) ;
						}
				}
				// At least one relevant to find, totalRelevant can be 0, when connection of member has no
				// fingerprint in the repository of FPs
				if( totalRelevant > 0 ){
					k 				= 0 ; // ranking position
					preK 			= 0 ; // precision k
					linksfounded 	= 0 ; // relevant connection till k
					tmplinksfounded = 0 ;
					rankingList = u.sortByComparator( rankingList, ASC ) ;

					for( String ds : gt.keySet( ) ){
						if( rankingList.containsKey( ds ) )
							tmplinksfounded++ ;
					}
					
					if( tmplinksfounded > 0 ){
							for( String ds : rankingList.keySet( ) ){
								k++ ;
								if( gt.containsKey( ds ) ){	
									linksfounded++ ;
									preK += (linksfounded / k ) ;
								}
								if (tmplinksfounded == linksfounded )
									break;
							}
					}
					//MAP
					double result = ( preK / totalRelevant ) ;
					//Set the MAP to the corresponding interval
					if ( 0 >= result && result <= 0.1 ){
						results.put("0-10", results.get( "0-10") + 1 ) ;
						interval1.add( member ) ;
					}else if ( 0.1 > result && result <= 0.2 ){
						results.put("11-20", results.get( "11-20" ) + 1 ) ;
						interval1.add( member ) ;
					}else if ( 0.2 > result && result <= 0.3 ){
						results.put("21-30", results.get( "21-30" ) + 1 ) ;
						interval2.add( member ) ;
					}else if ( 0.3 > result && result <= 0.4 ){
						results.put("31-40", results.get( "31-40" ) + 1 ) ;
						interval2.add( member ) ;
					}else if ( 0.4 > result && result <= 0.5 ){
						results.put("41-50", results.get( "41-50" )  + 1 ) ;
						interval2.add( member ) ;
					}else if ( 0.5 > result && result <= 0.6 ){
						results.put("51-60", results.get("51-60") + 1 ) ;
						interval3.add( member ) ;
					}else if ( 0.6 > result && result <= 0.7 ){
						results.put("61-70", results.get("61-70") + 1 ) ;
						interval3.add( member ) ;
					}else if ( 0.7 > result && result <= 0.8 ){
						results.put("71-80", results.get( "71-80" ) + 1 ) ;
						interval3.add( member ) ;
					}else if ( 0.8 > result && result <= 0.9 ){
						results.put("81-90", results.get( "81-90" ) + 1 ) ;
						interval3.add( member ) ;
					}else if ( 0.9 > result && result <= 1 ){
						results.put("91-100", results.get( "91-100" ) + 1 ) ;
						interval3.add( member ) ;
					}
				}else{
					interval1.add( member ) ;
					results.put("0-10", results.get( "0-10") + 1 ) ;
				}
			}				
		}
		double totalDatasets = 0 ;
		for(String interval : results.keySet(  ) ) {
			totalDatasets += results.get( interval ) ;
		}
		
		for(String interval : results.keySet( )) {
			System.out.println( interval + "%," + ( results.get( interval ) / totalDatasets ) ) ;
		}
		
		Iterator<String> it2 = interval2.iterator();
		Iterator<String> it3 = interval3.iterator();

		for( String ds:interval1){
			System.out.print("<tr>");
			System.out.print("<td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/" + ds + "'>" + ds +" </a></td>"  ) ;
			if(it2.hasNext())
				System.out.print("<td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/" + it2.next() + "'>" + it2.next() +" </a></td>"  ) ;
			else
				System.out.print("<td></td>" ) ;
			if(it3.hasNext())
				System.out.print( "<td><a style='color:#000;' target='_blank' href='http://linkeddatacatalog.dws.informatik.uni-mannheim.de/dataset/" + it3.next() + "'>" + it3.next() +" </a></td>"  ) ;
			else
				System.out.print("<td></td>" ) ;
			System.out.print("</tr>");
		}

	}

	private void datasetsValidationFingerPrintGoldenStandard( HashMap< String, Dataset> datasets2, DistanceMeasure dm) {
		double MAP 	;
		double k ;
		double preK ;
		double Recall ;
		double contTotal ;
		double auxPosition ;
		double notConsidered ;
		double linksfounded	 ;
		double numClusters ;
		double totalRelevant ;
		double tmptotalRelevant ;
		double tmplinksfounded ;
		int seed = 14 ;
		Util u =  new Util( path ) ;
		ArrayList< String > results = new ArrayList< String > ( ) ;
		FingerPrintManager mfp = new FingerPrintManager();
		HashSet< String > dsFingerPrint = (HashSet<String>) u.getDatasetFingerprint( datasets2 ).keySet( ) ;
		HashMap< String, Set< String > > 	clusters = null ;
		HashMap < String, Double >			rankingList = null ; 
		Clustering  cl = new Clustering( path ) ;
		
		for( int nminc = 2 ; nminc<= 100 ; nminc = nminc + 1 ){
			// we pass the minimum and maximum number of clusters with the same value for the evaluation.
			cl.executeClustering( datasets2, nminc, nminc, seed, true ) ;
			clusters = cl.getResultXmeans( ) ;
			MAP = 0.0 ;
			Recall = 0.0 ;
			notConsidered   = 0.0 ;
			contTotal = 0.0 ;
			// evaluate each cluster
			for( String cluster:clusters.keySet( ) ){
				Set< String > evaluatedCluster = clusters.get( cluster ) ;
				for( String member:evaluatedCluster){
					rankingList = new HashMap<String, Double>( ) ;
					HashMap< String, Double > fp = mfp.readFingerPrintDataset( member ) ;
					//Calculate the distance from member to the others dataset in the cluster
					for( String otherMember:evaluatedCluster){
						if( member != otherMember){
							HashMap< String, Double > fp2 = mfp.readFingerPrintDataset( otherMember ) ;
							if( dm == DistanceMeasure.Euclidean )
								rankingList.put( otherMember, u.getEuclideanDistance( fp, fp2 ) ) ;
							else
								rankingList.put( otherMember, u.getCosineDistance( fp, fp2 ) ) ;
								
						}
					}
					//number of ds relevants to find in the ranking, empty rankingList happends when a cluster has 1 element
					if( !rankingList.isEmpty( ) ){
						// ground truth relationships
						totalRelevant = 0 ;
						tmptotalRelevant = 0 ;
						HashMap<String, String> relations = datasets2.get( member ).getRelations( ) ;
						HashSet<String> gt = new HashSet<String>( ) ;
						
						for( String relation : relations.keySet( ) ){
								if (  dsFingerPrint.contains( relation ) )  {
									totalRelevant ++ ;
									gt.add( relation ) ;
								}
						}
						
						if( gt.isEmpty( ) ) {
							notConsidered ++ ;
						}
						// At least one relevant to find, totalRelevant can be 0, when connection of member has no
						// fingerprint in the repository of FPs
						if( totalRelevant > 0 ){
							k 				= 0 ; // ranking position
							preK 			= 0 ; // precision k
							auxPosition 	= 0 ; 
							linksfounded 	= 0 ; // relevant connection till k
							tmplinksfounded = 0 ;
							rankingList = u.sortByComparator( rankingList, ASC ) ;
							for( String ds : gt ){
								if( rankingList.containsKey( ds ) )
									tmplinksfounded++ ;
							}
							//System.out.println( member + "-r: " +  totalRelevant + " " + tmplinksfounded );
							
							if( tmplinksfounded > 0 ){
									for( String ds : rankingList.keySet( ) ){
										k++ ;
										if( gt.contains( ds ) ){	
											linksfounded++ ;
											preK += (linksfounded / k ) ;
										}
										if (tmplinksfounded == linksfounded )
											break;
									}
									//CALCULATING AVERAGE PRECISION
									Recall += (linksfounded / totalRelevant) ;
									MAP += ( preK / totalRelevant ) ;
									//System.out.println( ( preK / totalRelevant ) );
							}
						}
					}
					contTotal ++ ; 
				}
			}
			numClusters = clusters.size( ) ;
			System.out.println("#Clusters: " + nminc + " #considered: " + numClusters ) ;
			//System.out.println("Recall: " + Recall / contTotal ) ;
			System.out.println("MAP: " + MAP / ( contTotal - notConsidered ) ) ;
			System.out.println("Considered: "+ contTotal + " Not Considered: " + notConsidered ) ;
			results.add(nminc + "," + MAP/ ( contTotal - notConsidered )  ) ;
		}
		for(String r : results){
			System.out.println( r ) ;
		}
//		 links: 1157
//		 fps: 762
//		 fp & links: 510
	}
}
