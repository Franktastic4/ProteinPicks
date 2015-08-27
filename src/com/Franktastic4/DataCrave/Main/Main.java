package com.Franktastic4.DataCrave.Main;

import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;

public class Main {

	private ActionListener searchButtonListener;

	public static void SelectDatabase(){
		
		// Load or Create table if needed
		Connection databaseConnection = null;
		   
		try{
			// Register JDBC driver
		   Class.forName(NutritionTableDbHelper.FOR_NAME);

		    // Open a connection (Select database)
		    System.out.println("Connecting to database...");
		    databaseConnection = DriverManager.getConnection(NutritionTableDbHelper.DB_URL,
		    		NutritionTableDbHelper.USER, NutritionTableDbHelper.PASS);
		    
		    // Check if table exists
		    if(!tableExists(databaseConnection)){
		       System.out.println("Table does not exist");
		       createTable(databaseConnection); 
		       fillTable(databaseConnection);
		    }else{
		    	System.out.println("Table already exists");
		    }
		    	       
		   }catch(SQLException se){
			      se.printStackTrace();
			}catch(Exception e){
			      e.printStackTrace();
			}finally{
			      try{
			         if(databaseConnection!=null)
			        	 databaseConnection.close();
			      }catch(SQLException se){
			         se.printStackTrace();
			      }
			}

	}
		   
	public static void dropTable(Connection databaseConnection) throws SQLException{
		
		Statement statement = databaseConnection.createStatement();
		statement.executeUpdate("DROP TABLE " + NutritionTableDbHelper.TABLE_NAME);
		statement.close();
		
	}
	
    public static Boolean tableExists(Connection databaseConnection){
	   
	 try {
		 ResultSet tables = databaseConnection.getMetaData().getTables(null, null, NutritionTableDbHelper.TABLE_NAME, null);
		 if(tables.next()){
			 return true;
		 }
		
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 
	 
	 return false;
	 
		   
   }
   
    public static void createTable(Connection databaseConnection) throws IOException{

	      
	   try{
	      Statement statement = databaseConnection.createStatement();
	      statement.executeUpdate(NutritionTableDbHelper.CREATE_TABLE);
	       
	   }catch(Exception se){
		   se.printStackTrace();
	   }
   }
	
    public static void fillTable(Connection databaseConnection){
    	String sql = "temp";
    	// import USDA info into database
 	   try{
 		  
 		  Statement statement = databaseConnection.createStatement();  
 		  CSVReader reader = new CSVReader(new FileReader("USDA_DATA1.csv"), ',', '"', 1);
 	      
 		  String[] nextLine;
 	      while ((nextLine = reader.readNext()) != null) {
 	         if (nextLine != null) {
 	      	 
 	        	 // Check each line
 	        	 for(int index = 3; index < 10; index++){

 	        		 // fill the blanks
 	        		 if(nextLine[index].isEmpty()){
 	        			 nextLine[index] = "0";
 	        		 }        	
 	        		 
 	        	 }

 	        	 // add to database
 	        	  sql = 
 	        			"INSERT INTO " + NutritionTableDbHelper.TABLE_NAME + 
 	        			" VALUES ('"  + nextLine[1]	 + "', "	+
 	        			Double.parseDouble(nextLine[3]) + ", " + 
 	        			Double.parseDouble(nextLine[4]) + ", " + 		
 	        			Double.parseDouble(nextLine[5]) + ", " + 	
 	        			Double.parseDouble(nextLine[7]) + ", " + 			
 	        			Double.parseDouble(nextLine[9]) +  ") ";
	
 	        	 statement.executeUpdate(sql);
 	        	 
 	         }
 	       }
 	 	  
 		  
 		  
 	 	  reader.close();
 	 	  statement.close();
 	 	   
 	   }catch(FileNotFoundException e){
 		   e.printStackTrace();
 	   }catch(IOException e){
 		   e.printStackTrace();
 	   }catch(SQLSyntaxErrorException e){
 		  System.out.println(sql);
 	   }catch(Exception e){
 		   e.printStackTrace();
    }   
	   
    }
    
    public static void buildHashmap(){
   	
    	try{
    		
    		System.out.println("Connecting to database to print.");
		    Connection databaseConnection = DriverManager.getConnection(NutritionTableDbHelper.DB_URL,
		    		NutritionTableDbHelper.USER, NutritionTableDbHelper.PASS);
    		Statement statement = databaseConnection.createStatement();
    		
    		
    		
    		// Build Query
    		String query = "SELECT " + 
    		NutritionTableDbHelper.NAME + ", " + 
    		NutritionTableDbHelper.CALORIES + ", " +
    		NutritionTableDbHelper.PROTEIN + ", " +
    		NutritionTableDbHelper.FAT + ", " +
    		NutritionTableDbHelper.CARBS + ", " +
    		NutritionTableDbHelper.SUGAR + 
    		" FROM " +  NutritionTableDbHelper.TABLE_NAME; 
    		
    		// Submit query, store in ResultSet object
    		ResultSet results = statement.executeQuery(query);
    		
    		// Store result in hashmap to improve search time over DB
    		// hashmap over hashtable because a hashmap works faster
    		// because its unsychronized so it doesn't need to be checked
    		
    		HashMap hashmapUSDA = new HashMap();
    		
    		while(results.next()){
    			System.out.println(
    					"Name: " + results.getString(NutritionTableDbHelper.NAME) + 
    					" Protein: " + results.getInt(NutritionTableDbHelper.PROTEIN)
    					);
    			
    		}
    	
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	
    	
    }
    
	public static void main(String[] args){
		
		// The database closes the connection at the end of SelectDatabase? Doesn't seem to return.
		SelectDatabase();
		buildHashmap();
		
		// Then Make Hashmap by selecting Table in the DATABASE
		// Then build the Tree
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				// We made a MainFrame class, that we can create with 
				// the components already defined. Layout is defined in MainFrame
				
				// The MainFrame is the "controller" that tells each button what to do instead of the 
				// buttons/objects calling each other.
				JFrame frame = new MainFrame("DataCrave");
				frame.setSize(400, 250);
				
				// Can I try to add a listener to the frame?
				
				// sets the x button to exit the program when the x is clicked
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
			
		});
		
		
	}
	
	
	
}
