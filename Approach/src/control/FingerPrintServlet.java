package control;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.ConnPg;
import net.sf.json.JSONObject;
import shell.Util;

/**
 * Servlet implementation class ActionServlet
 */

public class FingerPrintServlet extends HttpServlet 
{
 private static final long serialVersionUID = 1L ;
    
 public FingerPrintServlet( ) 
 {
 }

  protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
  {
	  int option 		= 0 ;
	  String result		= "" ;
	  Gson gson 		= new Gson();
      JSONObject output ;  
	  option 			= Integer.parseInt( request.getParameter("request") ) ;
	  String path = request.getServletContext().getInitParameter("DIR_FILES") ;
	  Util u= new Util( path ) ;
	  Clustering cl ;
	  ConnPg conn = new ConnPg( );

	switch( option )
	  {
	  	case 0: 
	  			int nminc, nmaxc, seed ;
	  			nminc						= Integer.parseInt( request.getParameter("nminc") ) ;
	  			nmaxc						= Integer.parseInt( request.getParameter("nmaxc") ) ;
	  			nmaxc						= Integer.parseInt( request.getParameter("nmaxc") ) ;
	  			seed						= Integer.parseInt( request.getParameter("seed") ) ;
	  			boolean normalized 			= true ;
	  			boolean success 			= false ;
	  			String clusteringFileName	= nminc + "-" + nmaxc + "-" + seed + ".json" ;
  				cl = new Clustering( path ) ;
  				success = cl.executeClustering( nminc, nmaxc, seed, normalized ) ;
	  			output = new JSONObject( ) ;
				output.put("fileName", clusteringFileName ) ;  
				output.put("status", success ) ;  
	  			result = gson.toJson( output ) ;
	  		break;
	  			
	  	case 1:
	  			HashMap <String, Double> ranking 				= new HashMap< String, Double>( ) ;
	  			HashMap <String, String> dsClusterIds 			= new HashMap< String, String>( ) ;
	  			HashMap <String, HashMap< String, Double > >fps = new HashMap< String, HashMap< String, Double > >( ) ;
	  			String fileName = request.getParameter("fileName") ;
	  			String idNode = request.getParameter("idNode") ;
	  			cl = new Clustering( path  ) ;
	  			ranking = u.getRankingbyNode( fileName , idNode ) ;
	  			dsClusterIds = u.getIdClusters( fileName ) ;
	  			fps = cl.getFingerPrints( ranking.keySet( ) ) ;
	  			output = new JSONObject( ) ;
	  			
	  			output.put( "ranking" , ranking ) ;
	  			output.put( "idclusters" , dsClusterIds ) ;
	  			output.put( "fingerprint" , fps ) ;
	  			result = gson.toJson( output ) ;
	  		break;
	  	case 2:
				fileName 	= request.getParameter("fileName") ;
				idNode 		= request.getParameter("idNode") ;
				String dsName 	= request.getParameter("dsName") ;
				u		= new Util( path ) ;
				cl 			= new Clustering( path ) ;
				Set<String> dsList;
	  			HashMap<String, Integer> gs = new HashMap< String, Integer>( ) ;
	  			dsClusterIds = new HashMap< String, String>( ) ;
	  			dsClusterIds = u.getIdClusters( fileName ) ;
				dsList = u.getDsbyCluster( fileName , idNode ) ;
				fps = cl.getFingerPrints( dsList ) ;
				ranking = u.rankingCluster( fps, dsName, DistanceMeasure.Cosine ) ;
				gs = u.getGoldStandard( dsName, dsList ) ;
				output = new JSONObject( ) ;
				output.put( "ranking" , ranking ) ;
				output.put( "idclusters" , dsClusterIds ) ;
				output.put( "gs" , gs ) ;
				result = gson.toJson( output ) ;
				break;
	  		
	  	case 3:
	  			HashSet<String> entities;
	  			HashSet<String> categories;
	  			output = new JSONObject( ) ;
	  			u	= new Util( path ) ;
		  		dsName 	= request.getParameter("dsName") ;
		  		entities = u.getEntitiesByDataset( dsName ) ;
		  		categories = u.getCategoriesByDataset( dsName ) ;
		  		output.put( "rankingCategories" , categories ) ;
		  		output.put( "rankingEntities" , entities ) ;
		  		result = gson.toJson( output ) ;
		  		break;
	  }
	  
	  response.setContentType( "text/plain" ) ;  
	  response.setCharacterEncoding( "UTF-8" ) ; 
	  response.getWriter( ).write( result ) ; 
  }
  

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
 {
  
 }

}