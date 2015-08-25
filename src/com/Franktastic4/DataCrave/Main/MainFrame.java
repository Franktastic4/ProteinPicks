package com.Franktastic4.DataCrave.Main;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextArea;

public class MainFrame extends JFrame {

	private MainPanel mainPanel;
	
	// Constructor
	public MainFrame(String title){
		super(title);
	
		// Set layout manager
		// BorderLayout is a simple standard layout
		setLayout(new BorderLayout());
	
		// Create components
		// mainPanel is the panel on the left where I append the buttons and text fields
		mainPanel = new MainPanel();
		
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
				
				// Send back to main to search?
				System.out.println(searchTextField);
			}
			
		});
	
		// Add the panel on the west/left side of the GUI
	   Container container = getContentPane();
	   container.add(mainPanel, BorderLayout.WEST);
	   
	   
	   
	}
	
	
	
}
