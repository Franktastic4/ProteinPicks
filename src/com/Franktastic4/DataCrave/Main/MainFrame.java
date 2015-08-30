package com.Franktastic4.DataCrave.Main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JViewport;

public class MainFrame extends JFrame {

	private MainPanel mainPanel;
	private DisplayPanel displayPanel;
	private JScrollPane tableContainer;
	Container container = getContentPane();
	
	private Food hashSearch(String searchTextField){
		
		Food foodTemp = (Food) Main.mHashMap.get(searchTextField.toLowerCase().hashCode());
		//System.out.println("Searched for: "+ searchTextField);
		//System.out.println("Results: " +  foodTemp.returnName());
		return foodTemp;
	}
	
	private void offerSuggestions(ArrayList searchSuggestionsArrayList){
		
		JFrame frame = new SuggestionFrame("Suggestions", searchSuggestionsArrayList);
		frame.setSize(200, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		frame.setVisible(true);
		
	}
	
	// Constructor
	public MainFrame(String title){
		super(title);
	
		// Set layout manager
		// BorderLayout is a simple standard layout
		setLayout(new BorderLayout());
	
		// Create components
		// mainPanel is the panel on the left where I append the buttons and text fields
		// displayPanel is the panel we view, inside a scroll-able pane
		mainPanel = new MainPanel();
		displayPanel = new DisplayPanel(null,null);
		tableContainer = new JScrollPane(displayPanel);
		
		// We're creating a listener to detect events in our panel.
		// Only have one button so I don't check for which event
		mainPanel.addMainPanelListener(new MainPanelListener() {
			
			/* HOW IT WORKS
			 In the main panel class that we created, we used a generic eventListener to detect when the 
			 Search Button was pressed, and we saved the search field, made an event, passed the event to 
			 fireEvent, Which calls back this method
			*/
			
			public void mainPanelEventOccured(MainPanelEvent event){
				
				// Get the text that comes back
				String searchTextField = event.getText();
				
				// Remove old view
				container.remove(tableContainer);
				
				// If Clearing, make a clean displayPanel
				if(event.getText().equals("CLEAR")){
					displayPanel = new DisplayPanel(null,null);
	
				}else{
				// Else we're updating a screen
					
					// Check for existing table
					JViewport viewport = tableContainer.getViewport(); 
					JTable oldTable = (JTable) ((DisplayPanel) viewport.getView()).returnJTable();
							
					// Call a method that access Main's public hashmap, and Tree
					
					// SEARCH SUGGESTION
					if(hashSearch(searchTextField) == null){
						// Offer search suggestions
						System.out.println("No results for " + searchTextField);
						ArrayList searchSuggestions = (ArrayList<String>) Main.mSearchSugguestions.get( Main.returnKeyWord(searchTextField).toLowerCase().hashCode());
						if(searchSuggestions != null){
							// There exists search sugguestions
							offerSuggestions(searchSuggestions);

							//System.out.println("Did you mean: ");
							//for(int index = 0; index < searchSuggestions.size(); index++){
							//	System.out.println(searchSuggestions.get(index));
							//}
							
						}else{
							System.out.println("No suggestions");
							
						}
		
					}
					// Set a new value for searchTextField if there are suggestions
					displayPanel = new DisplayPanel(hashSearch(searchTextField), oldTable);
				}
				
				
				tableContainer = new JScrollPane(displayPanel);	
				container.add(tableContainer, BorderLayout.EAST);
				
				// Refresh the FRAME
				revalidate();
				repaint();		
			}
			
		});
	
		// Add the panel on the west/left side of the GUI		
	    container.add(mainPanel, BorderLayout.WEST);
	   
	    //tableContainer (ScrollPane Object) should provide the header. We added it when we put the table in it.
	    container.add(tableContainer, BorderLayout.EAST);
	   
	   
	   
	}
	
	
	
}
