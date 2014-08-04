package com.rhsquashclub.arhscbook.model;

public enum RHSCCourtSelection {
	All ("All",0),
	Singles("Singles",1),
	Doubles("Doubles",2),
	Front("Front",3),
	Back("Back",4);
	
	private final String text;
	private int pos;
	
	RHSCCourtSelection(String text, int pos) {
		this.text = text;
		this.pos = pos;
	}
	
	public String getText() {
		return text;
	}
	
	public static RHSCCourtSelection find(int pos) {
		for (RHSCCourtSelection cs : RHSCCourtSelection.values()) {
			if (pos == cs.pos) return cs;
		}
		return All;
	}
}
