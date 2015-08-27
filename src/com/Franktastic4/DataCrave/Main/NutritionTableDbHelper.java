package com.Franktastic4.DataCrave.Main;

public class NutritionTableDbHelper{
	
	public NutritionTableDbHelper(){
		
	}

	public static final String TABLE_NAME = "NUTRITION_TABLE";
	public static final String DATABASE_NAME = "USDA_DATABASE";
	
	public static final String FOR_NAME = "org.apache.derby.jdbc.EmbeddedDriver";  
	public static final String DB_URL = "jdbc:derby:" + DATABASE_NAME + ";create=true";
	
	public static final String USER = "username";
    public static final String PASS = "password";
	
	public static final String CREATE_DATABASE = "CREATE DATABASE " + DATABASE_NAME;
	
	// Column B (1)
	public static final String NAME = "NAME";
	
	// Column D (3) 
	public static final String CALORIES = "CALORIES";
	
	// Column E (4)
	public static final String PROTEIN = "PROTEIN";
	
	// Column F (5)
	public static final String FAT = "FAT";
	
	// Column H (7)
	public static final String CARBS = "CARBOHYDRATES";
	
	// Column J (9)
	public static final String SUGAR = "SUGAR";

	public static final String INT = " INTEGER, ";
	
	public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" + 
											  NAME + " VARCHAR(255),  " + 
											  CALORIES + INT +
											  PROTEIN + INT + 
											  FAT + INT + 
											  CARBS + INT +
											  SUGAR + INT + 
											  "PRIMARY KEY " + "( " + NAME + ")" +  
											  ")";
	


}
