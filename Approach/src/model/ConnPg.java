package model;

import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnPg 
{
	//desarrollo
	public String password 							= "xxxx" ;
	public String hostname 							= "localhost" ;
	public String dbname 							= "profile" ;
	public String username 							= "xxxx" ;
	public static java.sql.Connection connection 	= null;
	
	public ConnPg( )
	{
		try{
				Class.forName("com.mysql.jdbc.Driver");
		}catch (ClassNotFoundException e)
		{
			System.out.println( "driver problem: " + e.getMessage( ) ) ;
		}
		
		try{
			String constr =  "jdbc:mysql://"+ hostname +":3306/"+ dbname ;
			connection = DriverManager.getConnection( constr, username , password  ) ;
			System.out.println("connection stablished!") ;
		}catch (SQLException e)
		{
			System.out.println("connection problem" + e.getErrorCode( ) )  ;
		}
	}

	/**
	 * @return the connection
	 */
	public java.sql.Connection getConnection( ) {
		return connection ;
	}

	/**
	 * @param connection the connection to set
	 */
	public void setConnection(java.sql.Connection connection) {
		this.connection = connection ;
	}
}
