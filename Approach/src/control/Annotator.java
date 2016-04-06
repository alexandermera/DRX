package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.poi.util.SystemOutLogger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import model.CategoryManager;
import model.DatasetEntityManager;
import model.EntityCategoryManager;
import shell.Topic;

public class Annotator {
	
	public HashMap < String, Topic > topics ;
	private static final String vCatDataBase 				= "enwiki" ; // The wikipedia metadata databaseis not working, but it does can be used "enwiki"
	public static CategoryManager categoryManager 		= new CategoryManager( );
	public static HashMap< String, Double > globalScore ;
	// 38 in the wikipedia super categories article
	public static HashMap<String, Integer > topLevelCat38 	= new HashMap<String, Integer >( ) ;
	// 23 categories following the kawase article
	public static HashMap<String, Integer > topLevelCat23 	= new HashMap<String , Integer>( ) ;
	
	public Annotator(){
		chargeTopLevelCategories( ) ;
	}
	
	public static void main(String [] args)
	{	
		String text2 = "At around the size of a domestic"
			+ " chicken, kiwi are by far the smallest living ratites and lay the largest egg in relation"
			+ " to their body size of any species of bird in the world." ;
		String text = "Consumer spending stalled in August after the surge in gasoline prices squeezed Americans’ paychecks, showing the biggest part of the economy is struggling to contribute to the economic recovery. Purchases rose 0.1 percent after adjusting for inflation following a 0.4 percent gain in July, according to data from the Commerce Department issued in Washington today. Other reports showed business activity unexpectedly contracted in September, while consumer sentiment improved. Households may find it difficult to boost spending as slack employment generates income gains that scarcely cover rising food and fuel bills. At the same time, a real estate market that shows signs of stabilizing and higher stock prices are giving Americans reason to be less pessimistic about the future, which may prevent demand from deteriorating further. “We are going to get more of the same, very slow growth in consumer spending,” said Joshua Shapiro, chief U.S. economist at Maria Fiorini Ramirez Inc. in New York, who correctly projected the increase in purchases. That is “hardly surprising given the state of income growth and what is going on in the labor market.” Stocks dropped after the reports, capping the biggest weekly slump since June for the Standard & Poor’s 500 Index. The S&P 500 fell 0.5 percent to 1,440.67 at the close in New York. The index retreated 1.3 percent this week. Treasury securities rose, sending the yield on the benchmark 10-year note down to 1.63 percent from 1.66 percent late yesterday. Spending Totals The value of all goods and services bought last month rose 0.5 percent to $11.2 trillion at an annual pace, matching the median estimate of economists surveyed by Bloomberg and the biggest gain since February, according to the Commerce Department’s report. The gain mainly reflected a 0.4 percent jump in prices, the biggest since March 2011, leaving so-called real spending up 0.1 percent. The cost of fuel continues to be a drag on buying power. The pump price for a gallon of regular unleaded gasoline averaged $3.83 through Sept. 27 compared with $3.70 in August and $3.42 the prior month, according to data from AAA, the largest U.S. auto group. Bed Bath & Beyond Inc., the operator of more than 1,000 home-furnishing stores, this month reported second-quarter profit that trailed analysts’ estimates. Discount coupons to drive traffic cut into profit margins, Steve Temares, the Union, New Jersey-based retailer ‘s chief executive officer, said on a Sept. 19 conference call. Europe Inflation Prices are also picking up overseas as euro-area inflation unexpectedly accelerated in September after the Spanish government increased the sales tax to help plug its budget gap, driving prices up the most in 17 months, the European Union’s statistics office in Luxembourg said in a flash estimate today. In Asia, Japanese and South Korean industrial production fell more than economists estimated last month as slowdowns in China and Europe weighed on exports. Business activity in the U.S. unexpectedly contracted in September for the first time in three years, adding to signs manufacturing will contribute less to the economic recovery. The Institute for Supply Management-Chicago Inc. said today its business barometer fell to 49.7 this month from 53 in August. A reading of 50 is the dividing line between expansion and contraction. The median estimate of 57 economists surveyed by Bloomberg forecast the gauge would fall to 52.8. Projections ranged from 50 to 54.5. The group’s measures of orders, production and employment all declined. Fiscal Policy Uncertainties surrounding domestic fiscal policy and weakening economies in Europe and China may prevent companies from adding to headcount and ramping up production, according to economists such as Ward McCarthy. Slow growth prospects prompted the Federal Reserve to announce more accommodation measures earlier this month in a bid to help spur the three-year-old expansion. “The chain that links all this stuff together is just a loss of confidence as we head toward the end of the year in fiscal policy,” said McCarthy, chief financial economist at Jefferies & Co. Inc. in New York, whose forecast of 50 for the Chicago index was the closest in the Bloomberg survey. Businesses “have been cutting back on their investment spending.” Consumer confidence climbed in September to a four-month high as Americans became less pessimistic about the outlook for the economy, another report showed today. The Thomson Reuters/University of Michigan final sentiment index rose to 78.3 this month from 74.3 in August. Economists projected a reading of 79, according to the Bloomberg survey median. Sentiment Averages The index averaged 64.2 during the 18-month recession that ended in June 2009. In the five years leading up to the economic slump, the gauge averaged 89. While sentiment has been improving, it is “still well below what you would normally consider to be good numbers,” McCarthy said. “The buoyancy can be traced to the fact that the stock market continues to do well. Were the labor market to begin to show more signs of normalcy, I think consumer confidence would rise quite substantially.” Employers added 96,000 workers to payrolls last month, less than the 130,000 projected, and the unemployment rate fell to 8.1 percent after 368,000 people left the workforce. The jobless rate has exceeded 8 percent for 43 months, the longest stretch since monthly records began in 1948. Incomes rose 0.1 percent in August, matching the previous month’s gain after the Commerce Department revised down those figures, today’s report showed. Wages and salaries also increased 0.1 percent. Because incomes grew less than spending, the saving rate dropped to 3.7 percent, the lowest since April, from 4.1 percent. Disposable income, or the money left over after taxes, dropped 0.3 percent after adjusting for inflation, the weakest reading since November.";
		long date1 =  System.currentTimeMillis( ) ;
		Annotator annotator = new Annotator( ) ;
		annotator.annotateText( text, "test" ) ;
		long date2 = System.currentTimeMillis( ) ;
		System.out.println( globalScore.toString( ) ) ;
	}
	// Given a text the wikiminer service recognizes entities.
	public HashMap< String, Double > annotateText( String content, String datasetName ) {
			InputStream xml ;
			Document document ;
			NodeList topics = null ;
			String uriAnnotate 	= "" ;
			String uriBase 		= "http://drx.inf.puc-rio.br:8181/wikipediaminer/services/wikify?source=?" ;
			globalScore 	= new HashMap<String,Double>( ) ;
			ArrayList< String > aContent   = new ArrayList< String >( ) ;
			ArrayList< String > categories = new ArrayList< String >( )  ;
			
			if ( content.length( ) > 1800 ){
				StringTokenizer tokenizer = new StringTokenizer( content ) ;
				String s = "" ;
				while ( tokenizer.hasMoreTokens( ) ) {
					s += " " + tokenizer.nextToken( ) ;
					if( s.length( ) > 1800 ){
						aContent.add( s ) ;
						s = "" ;
					}
				}
			}else{
				aContent.add( content ) ;
			}
			
			ArrayList< String > entities = new ArrayList <String > ( ) ;
			for (String text : aContent) {
				try{
					uriAnnotate = uriBase + URLEncoder.encode( text, "UTF-8") ;
					URL	url = new URL( uriAnnotate ) ;
					HttpURLConnection connection = ( HttpURLConnection ) url.openConnection( ) ;
					connection.setRequestMethod( "GET" ) ;
					connection.setRequestProperty( "Accept", "application/xml" ) ;
					xml = connection.getInputStream( ) ;
					document = newDocumentFromInputStream ( xml ) ;
					topics = document.getElementsByTagName( "DetectedTopic" ) ;
					//get all the parent categories of the annotated topics
					for( int j = 0; j < topics.getLength(); j++){
						Node cNode = topics.item( j ) ;
						String entityName = cNode.getAttributes( ).getNamedItem( "title" ).getNodeValue( ) ;
						entities.add( entityName ) ;
						getCategories( entityName , categories ) ;
					}
					
					//System.out.println( "categories size: " +  categories.size( ) ) ;
				}catch(Exception e){
					System.out.println("\t problem annotating: " + uriAnnotate ) ;
				}
				
				if( entities.size( ) >1000) break;
			}
			
			DatasetEntityManager dem = new DatasetEntityManager( ) ;
			dem.insertDatasetEntity( datasetName, entities ) ;
			//get the top level categories for the parent categories  
			for (String category : categories) {
				getSuperCategories( category ) ;
			}
			if( globalScore.isEmpty() )
				return null ;
			else
				return  globalScore ; 
		}
	// Given a category retrieve its underlying supercategories from vCat app.
	private void getSuperCategories(String category) 
	{
		if( categoryManager.existCategory( category, 1 ) ){
			for( String scategory: categoryManager.readFingerPrintCategory( category ).keySet( ) ){
				if( globalScore.containsKey( scategory ) )
					globalScore.put( scategory, globalScore.get( scategory ) + 1 ) ;
				else
					globalScore.put( scategory ,  (double) 1 ) ;
			}
		}else{
				HashMap<String, Double > localScore = new HashMap<String,Double>( ) ;
				try{
					InputStream inputStream ;
					HttpURLConnection connection ;
					String uriBaseCategory = "https://tools.wmflabs.org/vcat/render?wiki=" + vCatDataBase + "&format=dot&category=";
					String uriCategory = uriBaseCategory + category ; 
					URL	url1 = new URL( uriCategory ) ;
					connection = (HttpURLConnection) url1.openConnection( ) ;
					connection.setConnectTimeout( 5000 ) ;
					connection.setReadTimeout( 10000 ) ;
					connection.setRequestMethod("GET");
					connection.setRequestProperty("Accept", "application/xml");
					inputStream = connection.getInputStream( ) ;
					BufferedReader reader = new BufferedReader(new InputStreamReader( inputStream ) ) ;
			        String line;
			        while ((line = reader.readLine()) != null) {
			        	if (line.contains("->" ) ) {
			        		String[ ] categories = line.split("->");
			        		for (int i = 0; i < 1; i++) {
								String s = categories[ i ].replaceAll("\"", "").replace(";", "").trim( ) ;
								if( topLevelCat23.containsKey( s ) ){
									if( localScore.containsKey( s ) )
										localScore.put( s, localScore.get(s) + 1 ) ;
									else
										localScore.put( s,  (double) 1 ) ;
									if( globalScore.containsKey( s ) )
										globalScore.put( s, globalScore.get(s) + 1 ) ;
									else
										globalScore.put( s,  (double) 1 ) ;
								}
								else{
									System.out.println( "no considered Category: " + s ) ;
								}
							}
			        	}
			        }
			        categoryManager.insertFingerPrintCategory( category, localScore ) ;
				} catch (MalformedURLException e) {
					System.out.println("\t problem with the vcat service: " + e.getMessage( )  ) ;
				} catch (IOException e) {
					System.out.println("\t problem I/O: " + e.getMessage( ) + " for " + category ) ;
				}				
			}
	}

	private void getCategories(String entityName, ArrayList<String> categories) {
		try{
			InputStream xml1 ;
			Document document1 ;
			HttpURLConnection connection1 ;
			String uriBaseCategory = "http://drx.inf.puc-rio.br:8181/wikipediaminer/services/exploreArticle?parentCategories=true" ;
			String uriCategory = uriBaseCategory + "&title=" + URLEncoder.encode( entityName, "UTF-8" ) ;
			URL	url1 = new URL( uriCategory ) ;
			connection1 = (HttpURLConnection) url1.openConnection();
			connection1.setConnectTimeout( 5000 ) ;
			connection1.setReadTimeout( 10000 ) ;
			connection1.setRequestMethod("GET");
			connection1.setRequestProperty("Accept", "application/xml");
			xml1 = connection1.getInputStream( ) ;
			document1 = newDocumentFromInputStream ( xml1 ) ;
			NodeList parentCategories = document1.getElementsByTagName( "ParentCategory" ) ;
			HashSet<String> tempCategories = new HashSet<String>( ) ;
			for( int i = 0; i < parentCategories.getLength(); i++){
				Node cNode1 = parentCategories.item( i ) ;
				categories.add( cNode1.getAttributes( ).getNamedItem( "title" ).getNodeValue( ).replaceAll("\\s+", "_" )  ) ;
				tempCategories.add( cNode1.getAttributes( ).getNamedItem( "title" ).getNodeValue( ).replaceAll("\\s+", "_" )  ) ;
			}	
			EntityCategoryManager ecm = new EntityCategoryManager( ) ;
			if ( !ecm.existEntity( entityName ) ){
					ecm.insertEntityCategory( entityName , tempCategories ) ;
			}
		} catch (MalformedURLException e) {
			System.out.println("\t problem with the exploreArticle service: " + e.getMessage( )  );

		} catch (IOException e) {
			System.out.println("\t problem of I/O: " + e.getMessage( )  ) ;
		}
	}
	// super categories base on the kawase's paper "topLevelCat23" and also super categories base on the Main_topic_classifications category "topLevelCat38"
	private void chargeTopLevelCategories() {
		// top level categories base on https://en.wikipedia.org/wiki/Category:Main_topic_classifications
		topLevelCat38.put("Agriculture", 1 ) ; 	topLevelCat38.put("Architecture‎", 2 ) ; topLevelCat38.put("Arts‎", 3 ) ;
		topLevelCat38.put("Behavior‎", 4 ); 		topLevelCat38.put("Chronology", 5 ) ; 	topLevelCat38.put("Creativity‎", 6 ) ;
		topLevelCat38.put("Culture", 7 ) ; 		topLevelCat38.put("Education", 8 ) ; 	topLevelCat38.put("Employment‎", 9  ) ;
		topLevelCat38.put("Energy‎", 10 ) ; 		topLevelCat38.put("Environment", 11) ; 	topLevelCat38.put("Geography‎", 12 ) ;
		topLevelCat38.put("Goods‎", 13 ) ; 		topLevelCat38.put("Government", 14 ) ; 	topLevelCat38.put("Health", 15 ) ;
		topLevelCat38.put("History", 16 ) ; 	topLevelCat38.put("Humanities", 17 ) ; 	topLevelCat38.put("Humans‎", 18 ) ;
		topLevelCat38.put("Industry" , 19 ) ; 	topLevelCat38.put("Information", 20 ); 	topLevelCat38.put("Knowledge‎", 21 ) ;
		topLevelCat38.put("Language‎", 22 ) ; 	topLevelCat38.put("Law", 23 );  		topLevelCat38.put("Mathematics", 24 ) ; 
		topLevelCat38.put("Medicine‎", 25 ) ;  	topLevelCat38.put("Mind‎", 26 ) ; 		topLevelCat38.put("Nature", 27 ); 
		topLevelCat38.put("Objects‎", 28 ) ;  	topLevelCat38.put("People", 29 ) ; 		topLevelCat38.put("Politics", 30 ) ;
		topLevelCat38.put("Science‎", 31 ) ;		topLevelCat38.put("Sports‎", 32 ) ;		topLevelCat38.put("Structure", 33 ) ;
		topLevelCat38.put("Systems‎", 34 ) ;		topLevelCat38.put("Technology", 35 ) ;	topLevelCat38.put("Telecommunications", 36 ) ;
		topLevelCat38.put("Universe‎", 37 ) ;	topLevelCat38.put("World‎", 38 ); 
		
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
	public Document newDocumentFromInputStream(InputStream in) 
	{
		Document ret = null ;
		DocumentBuilder builder = null ;
		DocumentBuilderFactory factory = null ;
		try{
			factory = DocumentBuilderFactory.newInstance( ) ;
	  		builder = factory.newDocumentBuilder( ) ;
	  		ret = builder.parse(new InputSource(in));
		} catch (ParserConfigurationException e) {
			System.out.println("Problem parsing Document: " + e.getMessage( ) );
		}catch (SAXException e) {
			System.out.println("Problem parsing Document: " + e.getMessage( ) );
		} catch (IOException e) {
			System.out.println("\t problem of I/O: " + e.getMessage( )  ) ;
		}
	    return ret;
	}
}
