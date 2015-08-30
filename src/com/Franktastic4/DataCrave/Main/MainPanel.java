package com.Franktastic4.DataCrave.Main;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.EventListenerList;

public class MainPanel extends JPanel {

	private EventListenerList listenerList = new EventListenerList();

	public MainPanel(){
		
		Dimension size = getPreferredSize();
		size.width = 200; // Window is 400
		setPreferredSize(size);
		
		// BorderFactory generates the borders
		setBorder(BorderFactory.createTitledBorder("Commands"));
		
		JLabel searchLabel = new JLabel("Search");
		
		// The 10 is telling it how big to make it.
		// 10 divisions in the grid
		final JTextField searchFieldText = new JTextField(10);
		
		JButton searchButton = new JButton("Go");
		searchButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				// Now I want to pass some information along
				String searchFieldTextFinal = searchFieldText.getText();
				fireMainPanelEvent(new MainPanelEvent(this,searchFieldTextFinal));
				
			}
			
		});
		
		JButton clearButton = new JButton("Clear History");
		clearButton.addActionListener(new ActionListener(){
			
			public void actionPerformed(ActionEvent e){
				
				// Now I want to pass some information along
				String searchFieldTextFinal = "CLEAR";
				fireMainPanelEvent(new MainPanelEvent(this,searchFieldTextFinal));
				
			}
			
		});
		
		
		setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		// Splits my grid into sections.
		
		// First columns of controls
		// This anchor lets the label stick to the left
		gc.weightx = 0.5;
		gc.weighty = 0.5;
		
		gc.anchor = GridBagConstraints.LINE_START;
		gc.gridx = 0;
		gc.gridy = 0;
		add(searchLabel, gc);
		
		gc.anchor = GridBagConstraints.LINE_END;
		gc.gridx = 0;
		gc.gridy = 0;
		add(searchFieldText, gc);
		
		gc.anchor = GridBagConstraints.FIRST_LINE_START;
		gc.weighty = 10;
		gc.gridx = 0;
		gc.gridy = 1;
		add(searchButton, gc);
		
		gc.anchor = GridBagConstraints.FIRST_LINE_END;
		gc.weighty = 10;
		gc.gridx = 0;
		gc.gridy = 1;
		add(clearButton, gc);
		
		
		
	}
	
	public void fireMainPanelEvent(MainPanelEvent event){
		Object[] listeners = listenerList.getListenerList();
		
		// to iterate through the list we go by two because the first of each pair
		// is the name of the class. We then verify the class is the of the correct type
		for(int listenerItor = 0; listenerItor < listeners.length; listenerItor += 2){
			if(listeners[listenerItor] == MainPanelListener.class){
				((MainPanelListener) listeners[listenerItor + 1]).mainPanelEventOccured(event);
			}
		}
		
	}

	public void addMainPanelListener(MainPanelListener listener){
		listenerList.add(MainPanelListener.class, listener);
	} 
	
	//good habit to add a remove listener
	public void removeMainPanelListener(MainPanelListener listener){
		listenerList.remove(MainPanelListener.class, listener);
	} 
}

