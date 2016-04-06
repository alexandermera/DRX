package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;

public class FingerPrintManager {
	public FingerPrintManager( ) {
	}
	public void insertFingerPrintDataset( String dataset, HashMap<String, Double > score )
	{	insertDataset ( dataset) ;
		String insertSQL = "INSERT INTO fingerprint (dataset, scategory, value) VALUES (?,?,?)" ;
		for ( String scategory : score.keySet( ) ) {
			PreparedStatement preparedStatement = null ;
			try {
				preparedStatement = ConnPg.connection.prepareStatement( insertSQL ) ;
				preparedStatement.setString( 1, dataset ) ;
				preparedStatement.setString( 2, scategory ) ;
				preparedStatement.setDouble( 3, score.get( scategory ) ) ;
				preparedStatement.executeUpdate( ) ;
			}catch (SQLException e){
				System.out.println("\t error inserting: " + scategory + " for " + dataset ) ;
			}finally {
	            DbUtil.close( preparedStatement ) ;
	        }
		}			
	}
	
	public void insertDataset (String dataset){
		String insertSQL = "INSERT INTO dataset (name) VALUES (?)" ;
		PreparedStatement preparedStatement = null ;
		try {
			preparedStatement = ConnPg.connection.prepareStatement( insertSQL ) ;
			preparedStatement.setString( 1, dataset ) ;
			preparedStatement.executeUpdate( ) ;
		}catch (SQLException e){
			System.out.println("\t error inserting: " + dataset ) ;
		}finally {
            DbUtil.close( preparedStatement ) ;
        }
	}
	
	public HashMap<String, Double > readFingerPrintDataset( String dataset )
	{
		HashMap<String, Double > score = new HashMap<String, Double >( ) ;
		String  readTopCategories 				= "SELECT scategory,value FROM fingerprint where dataset=?" ;
		ResultSet rs 							= null ;
		PreparedStatement statement 			= null ;
		try{
			statement = ConnPg.connection.prepareStatement( readTopCategories ) ;
			statement.setString( 1 , dataset ) ;
            rs = statement.executeQuery(  ) ;
            while ( rs.next( ) ){
            	score.put( rs.getString( "scategory"), rs.getDouble("value") ) ;
            }
        } catch (SQLException e) {
        	System.out.println("\t error reading fingerprint of: " + dataset ) ;
		}
		finally {
            DbUtil.close(rs) ;
            DbUtil.close( statement ) ;
        }
		return score ;
	}
	
	public HashSet<String> listFingerPrintDataset( )
	{
		HashSet<String> list = new HashSet<String>( ) ;
		String  readDatasets 				= "SELECT distinct dataset FROM fingerprint" ;
		ResultSet rs 							= null ;
		PreparedStatement statement 			= null ;
		try{
			statement = ConnPg.connection.prepareStatement( readDatasets ) ;
			rs = statement.executeQuery(  ) ;
			while ( rs.next( ) ){
				list.add( rs.getString( "dataset") ) ;
			}
		} catch (SQLException e) {
			System.out.println("\t error reading fingerprints" ) ;
		}
		finally {
			DbUtil.close(rs) ;
			DbUtil.close( statement ) ;
		}
		return list ;
	}
	
	public boolean existDataset( String dataset){
		boolean exist	= false ;
		String  readDataset 				= "SELECT * FROM dataset where name=?" ;
		ResultSet rs 						= null ;
		PreparedStatement statement 		= null ;
		try{
			statement = ConnPg.connection.prepareStatement( readDataset ) ;
			statement.setString( 1 , dataset ) ;
            rs = statement.executeQuery(  ) ;
            exist = rs.first( ) ;
        } catch (SQLException e) {
			System.out.println("\t error cheking dataset: " + dataset ) ;
		}
		finally {
            DbUtil.close(rs) ;
            DbUtil.close( statement ) ;
        }
		return exist ;
	}	
}
