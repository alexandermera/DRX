package model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class DatasetEntityManager
{
	public DatasetEntityManager( ) 
	{
	}
	
	public void insertDatasetEntity( String dataset, ArrayList<String> entities )
	{	if( !entities.isEmpty( )  ){
			String insertSQL = "INSERT INTO datasetentity (dataset, entity) VALUES (?,?)";
			for ( String entity : entities ) {
				PreparedStatement preparedStatement = null;
				try {
					preparedStatement = ConnPg.connection.prepareStatement(insertSQL) ;
					preparedStatement.setString( 1, dataset ) ;
					preparedStatement.setString( 2, entity ) ;
					preparedStatement.executeUpdate( ) ;
				}catch (SQLException e){
					System.out.println("error inserting: " + entity ) ;
					e.printStackTrace();
				}finally {
					DbUtil.close( preparedStatement ) ;
				}
			}			
		}
	}
	public HashSet<String> readDatasetEntity( String dataset )
	{
		HashSet<String > entities = new HashSet<String>( ) ;
		String  readEntities 				= "SELECT entity FROM datasetentity where dataset=?" ;
		ResultSet rs 							= null ;
		PreparedStatement statement 			= null ;
		try{
			statement = ConnPg.connection.prepareStatement( readEntities ) ;
			statement.setString( 1 , dataset ) ;
            rs = statement.executeQuery(  ) ;
            while ( rs.next( ) ){
            	entities.add( rs.getString( "entity") ) ;
            }
        } catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
            DbUtil.close(rs) ;
            DbUtil.close( statement ) ;
        }
		return entities ;
	}
	
	public boolean existDatasetEntity( String dataset){
		boolean exist	= false ;
		String  readCategory 				= "SELECT * FROM datasetentity where dataset=?" ;
		ResultSet rs 						= null ;
		PreparedStatement statement 		= null ;
		try{
			statement = ConnPg.connection.prepareStatement( readCategory ) ;
			statement.setString( 1 , dataset ) ;
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
