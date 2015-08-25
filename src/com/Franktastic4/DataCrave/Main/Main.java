package com.Franktastic4.DataCrave.Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.sql.*;


public class Main {

	private ActionListener searchButtonListener;
	static final NutritionTableDbHelper myDbHelper = new NutritionTableDbHelper();

	public static void SelectDatabase(){
		
		// Load or Create table if needed
		Connection databaseConnection = null;
		   
		try{
			// Register JDBC driver
		    Class.forName(myDbHelper.FOR_NAME);

		    // Open a connection (Select database)
		    System.out.println("Connecting to database...");
		    databaseConnection = DriverManager.getConnection(myDbHelper.DB_URL);

		    // If no database is selected, create it and load in from CSV file.
		    if(!databaseExists(databaseConnection)){
		       System.out.println("Database does not exist");
		       createDatabase(databaseConnection); 
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
		   
    public static Boolean databaseExists(Connection databaseConnection){
	   
	   try{
		   ResultSet resultSet = databaseConnection.getMetaData().getCatalogs();
		   		while (resultSet.next()) {		        
		   			String databaseName = resultSet.getString(1);
		   			if(databaseName.equals(myDbHelper.TABLE_NAME)){
		   				return true;
		   			}
		   		}
		   		resultSet.close();
	   }catch(SQLException se){
		   se.printStackTrace();
	   }
	   
	   return false;
   }
   
    public static void createDatabase(Connection databaseConnection){
	   
	   // Create database
	   
	   try{
	      Statement statement = databaseConnection.createStatement();
	      statement.executeUpdate(myDbHelper.CREATE_DATABASE);
	      System.out.println("Created database");
	      
	   }catch(Exception se){
		   se.printStackTrace();
	   }
	   
	   // import USDA info into database
	   
	   CSVReader reader = new CSVReader(new FileReader("data.csv"), ',' , '"' , 1);
	      
	   String[] nextLine;
	   while ((nextLine = reader.readNext()) != null) {
	      if (nextLine != null) {
	         
	    	  //Verifying the read data here
	         System.out.println(Arrays.toString(nextLine));
	      }
	    }
	   
   }
	
	public static void main(String[] args){
		
		SelectDatabase();
		
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
