package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class EntityCategoryManager
{
	public EntityCategoryManager( ) 
	{
	}
	
	public void insertEntityCategory( String entity, HashSet<String> parentCategories )
	{	if( !parentCategories.isEmpty( )  ){
			insertEntity ( entity) ;
			String insertSQL = "INSERT INTO entitycategory (entity, category) VALUES (?,?)";
			for ( String category : parentCategories ) {
				PreparedStatement preparedStatement = null;
				try {
					preparedStatement = ConnPg.connection.prepareStatement(insertSQL) ;
					preparedStatement.setString( 1, entity ) ;
					preparedStatement.setString( 2, category ) ;
					preparedStatement.executeUpdate( ) ;
				}catch (SQLException e){
					System.out.println("error inserting: " + category ) ;
					e.printStackTrace();
				}finally {
					DbUtil.close( preparedStatement ) ;
				}
			}			
		}
	}
	
	public void insertEntity (String entity){
		String insertSQL = "INSERT INTO entity (name) VALUES (?)";
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = ConnPg.connection.prepareStatement(insertSQL) ;
			preparedStatement.setString( 1, entity ) ;
			preparedStatement.executeUpdate( ) ;
		}catch (SQLException e){
			System.out.println("error inserting: " + entity ) ;
			e.printStackTrace();
		}finally {
            DbUtil.close( preparedStatement ) ;
        }
	}
	public HashSet<String> readEntityCategory( String entity )
	{
		HashSet<String > categories = new HashSet<String>( ) ;
		String  readTopCategories 				= "SELECT category FROM entitycategory where entity=?" ;
		ResultSet rs 							= null ;
		PreparedStatement statement 			= null ;
		try{
			statement = ConnPg.connection.prepareStatement( readTopCategories ) ;
			statement.setString( 1 , entity ) ;
            rs = statement.executeQuery(  ) ;
            while ( rs.next( ) ){
            	categories.add( rs.getString( "category") ) ;
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
            DbUtil.close(rs) ;
            DbUtil.close( statement ) ;
        }
		return categories ;
	}
	public boolean existEntity( String entity){
		boolean exist	= false ;
		String  readEntity 				= "SELECT * FROM entity where name=?" ;
		ResultSet rs 						= null ;
		PreparedStatement statement 		= null ;
		try{
			statement = ConnPg.connection.prepareStatement( readEntity ) ;
			statement.setString( 1 , entity ) ;
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
