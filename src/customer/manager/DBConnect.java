/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer.manager;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnect {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  
	static final String DB_URL = "jdbc:mysql://173.194.241.17:3306/customers?&useSSL=false"; //google cloud database
	static Properties loginDetails = new Properties();
	static String USER;
	static String PASS;
	static Connection conn;
	static Statement stmt;
	static String sql;
	
	public DBConnect() {
		System.out.println("Ready To Connect!");
	}
	
	public static void regDriver(){
	   	//Register JDBC driver
	      try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void setConnection(){
      //Open a connection
	      System.out.println("Connecting to database...");
	      try {
			conn = DriverManager.getConnection(DB_URL,USER,PASS);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static Connection getConnection(){
		return conn;
	}
	
	public static void setStatement(){
		System.out.println("Creating Statement...");
		try {
			stmt=conn.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Statement getStatement(){
		return stmt;
	}
	
    public void CloseConnections(){
    	System.out.println("Closing Connections...");
	    try {
			stmt.close();
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	    try {
			conn.close();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    public static void loginDB(){
    	final Properties loginDetails = new Properties();
    	{
    	try (FileReader in = new FileReader("login.properties")) 
    		{
    		System.out.println("prop file completed");
    	    loginDetails.load(in);
    	    } 
    	catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
    	catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	}	      
    	USER = loginDetails.getProperty("username");
    	PASS = loginDetails.getProperty("password");
    }
    
    public static Connection connect(){
		loginDB();
	    regDriver();
	    setConnection();
	    setStatement();
	    return conn;
	}
}
