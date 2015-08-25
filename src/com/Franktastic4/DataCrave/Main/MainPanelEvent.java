package com.Franktastic4.DataCrave.Main;

import java.util.EventObject;

public class MainPanelEvent extends EventObject {

	private String outputText;
	public MainPanelEvent(Object myObject, String inputText){
		super(myObject);
		outputText = inputText;
	}
	
	public String getText(){
		return outputText;
	}
	
}
