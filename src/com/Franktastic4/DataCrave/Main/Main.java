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
	static HashMap mHashMap;

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
		    
		    //dropTable(databaseConnection);
		    
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
		System.out.println("Dropping Table " + NutritionTableDbHelper.TABLE_NAME);
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
	      System.out.println("Created Table");
	       
	   }catch(Exception se){
		   se.printStackTrace();
	   }
   }
    
    public static void fillTable(Connection databaseConnection){
       String sql = "temp";
       // import USDA info into database
       int counter = 0;
 	   try{
 		  
 		   System.out.println("Opening CSV...");
 		   Statement statement = databaseConnection.createStatement();  
 		   CSVReader reader = new CSVReader(new FileReader("USDA_DATA1.csv"), ',', '"', 1);
 		  
 		   String prevString = "";
 		   String[] nextLine = reader.readNext();
 	       while (nextLine != null) {
 	    	   
 	    	   // Check for duplicates first before doing work
 	    	  if(prevString.equals(nextLine[1].replace("'",""))){
 	    		  nextLine = reader.readNext();
 	    		  continue;
 	    	  }
 	    	   
 	    	  // If not a duplicate

 	          // Check for empty entries  	   
 	    	  for(int index = 3; index < 10; index++){
 	    		  
 	    		  if(nextLine[index].isEmpty()){
 	    			 nextLine[index] = "0";
 	    		  }
 	    		  
 	    	  }
 	    	  
 	    	  
 	    	  
 	    	  // Remove apostrophe, messes with parsing
 	    	 prevString = nextLine[1].replace("'","");
 	    	  
 	    	  // Build statement
 	    	  sql =   "INSERT INTO " + NutritionTableDbHelper.TABLE_NAME + 
 	    			  " VALUES ('"  + prevString + "', "	+
 	    			  Double.parseDouble(nextLine[3]) + ", " + 
 	    			  Double.parseDouble(nextLine[4]) + ", " + 		
 	    			  Double.parseDouble(nextLine[5]) + ", " + 	
 	    			  Double.parseDouble(nextLine[7]) + ", " + 			
 	    			  Double.parseDouble(nextLine[9]) +  ") ";
 	    	  
 	    	  // Execute
 	    	  statement.executeUpdate(sql);
 	    	  counter++;
 	    	  
 	    	  if(counter%100 == 1){
 	    		  System.out.println("Inserted: " + counter);
 	    	  }
 	    	  
 	    	  // Next Iteration
 	    	  nextLine = reader.readNext();
 	       }
 
 	      System.out.println("Database has been loaded");
 	 	  reader.close();
 	 	  statement.close();
 	 	   
 	   }catch(FileNotFoundException e){
 		   e.printStackTrace();
 	   }catch(IOException e){
 		   e.printStackTrace();
 	   }catch(SQLSyntaxErrorException e){
 		   e.printStackTrace(); 		   
 	   }catch(SQLIntegrityConstraintViolationException e){
 		   e.printStackTrace();
 		   System.out.print(sql);
 	   }catch(Exception e){
 		   e.printStackTrace();		   
 	   }  
 	   
 	  System.out.println("Counter: " + counter);
	   
    }
            
    public static HashMap<Integer, Food> buildHashmap(){
	   	
    	int anotherCounter = 0;
    	
    	try{
    		
    		System.out.println("Connecting to database to build HashMap.");
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
    		
    		
    		// We add the <Double, Food> for type safety. It will know we use a key of type double to store Food objects
    		int hashMapCapacity = 70001; // is Prime, won't have to rehash because of capacity, default load is 75%
    		HashMap<Integer, Food> hashmapUSDA = new HashMap<Integer, Food>(hashMapCapacity);
    		
    		// Create an object and store it in the hashmap
    		while(results.next()){
    			
    			// Store in food object
    			Food food = new Food();	
    			food.setName(results.getString(NutritionTableDbHelper.NAME));
    			food.setCals(results.getDouble(NutritionTableDbHelper.CALORIES));
    			food.setProtein(results.getDouble(NutritionTableDbHelper.PROTEIN));
    			food.setFat(results.getDouble(NutritionTableDbHelper.FAT));
    			food.setCarbs(results.getDouble(NutritionTableDbHelper.CARBS));
    			food.setSugar(results.getDouble(NutritionTableDbHelper.SUGAR));
    		
    			// Store in hashmap, note if you have two matching keys
    			// It will replace the older one. That is not a collision
    			// A collision is when two unique keys hash to the same spot (bucket)
    			if(!hashmapUSDA.containsKey(food.returnName().hashCode())){
    				hashmapUSDA.put(food.returnName().hashCode(), food);
    				anotherCounter++;
    			}

    		 			
    		}
    	
    		
    		System.out.println("Hashmap buillt, counter is: " + anotherCounter);
    		System.out.println("Hashmap buillt, size is: " + hashmapUSDA.size());
    		return hashmapUSDA;
    		
    	}catch(SQLException e){
    		e.printStackTrace();
    	}
    	
    	return null;
    	
    	
    }
    
    public static void main(String[] args){
    	
		// The database closes the connection at the end of SelectDatabase? Doesn't seem to return.
		SelectDatabase();
			
		// Create Hashtable and Tree
		mHashMap = buildHashmap();
			
		SwingUtilities.invokeLater(new Runnable() {
			public void run(){
				// We made a MainFrame class, that we can create with 
				// the components already defined. Layout is defined in MainFrame
				
				// The MainFrame is the "controller" that tells each button what to do instead of the 
				// buttons/objects calling each other.
				JFrame frame = new MainFrame("ProteinPicks");	
				frame.setSize(400, 250);
				
				// Can I try to add a listener to the frame?
				
				// sets the x button to exit the program when the x is clicked
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
			
		});
		
		
	}
	
}
