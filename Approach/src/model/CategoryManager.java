package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;


public class CategoryManager
{
	public CategoryManager( ) 
	{
	}
	
	public void insertFingerPrintCategory( String category, HashMap<String, Double > score )
	{	if( !score.isEmpty( )  ){
			insertCategory ( category , 1) ;
			String insertSQL = "INSERT INTO topcategories (category, scategory, value) VALUES (?,?,?)";
			for ( String scategory : score.keySet( ) ) {
				PreparedStatement preparedStatement = null;
				try {
					preparedStatement = ConnPg.connection.prepareStatement(insertSQL) ;
					preparedStatement.setString( 1, category ) ;
					preparedStatement.setString( 2, scategory ) ;
					preparedStatement.setDouble( 3, score.get( scategory ) ) ;
					preparedStatement.executeUpdate( ) ;
				}catch (SQLException e){
					System.out.println("error inserting: " + scategory ) ;
					e.printStackTrace();
				}finally {
					DbUtil.close( preparedStatement ) ;
				}
			}			
		}
	}
	
	public void insertCategory (String category, int active){
		String insertSQL = "INSERT INTO category (name, active) VALUES (?,?)";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = ConnPg.connection.prepareStatement(insertSQL) ;
			preparedStatement.setString( 1, category ) ;
			preparedStatement.setInt( 2, active ) ;
			preparedStatement.executeUpdate( ) ;
		}catch (SQLException e){
			System.out.println("error inserting: " + category ) ;
			e.printStackTrace();
		}finally {
            DbUtil.close( preparedStatement ) ;
        }
	}
	public HashMap<String, Double > readFingerPrintCategory( String category )
	{
		HashMap<String, Double > score = new HashMap<String, Double >( ) ;
		String  readTopCategories 				= "SELECT scategory,value FROM topcategories where category=?" ;
		ResultSet rs 							= null ;
		PreparedStatement statement 			= null ;
		try{
			statement = ConnPg.connection.prepareStatement( readTopCategories ) ;
			statement.setString( 1 , category ) ;
            rs = statement.executeQuery(  ) ;
            while ( rs.next( ) ){
            	score.put( rs.getString( "scategory"), rs.getDouble("value") ) ;
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
            DbUtil.close(rs) ;
            DbUtil.close( statement ) ;
        }
		return score ;
	}
	public boolean existCategory( String category , int active){
		boolean exist	= false ;
		String  readCategory 				= "SELECT * FROM category where name=? and active=?" ;
		ResultSet rs 						= null ;
		PreparedStatement statement 		= null ;
		try{
			statement = ConnPg.connection.prepareStatement( readCategory ) ;
			statement.setString( 1 , category ) ;
			statement.setInt( 2 , active ) ;
            rs = statement.executeQuery(  ) ;
            exist = rs.first( ) ;

        } catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
            DbUtil.close(rs) ;
            DbUtil.close( statement ) ;
        }
		return exist ;
	}
}
