package edu.mines.csci498.ybakos.lunchlist;

import java.util.Calendar;

public class Restaurant {

	private String name = "";
	private String address = "";
	private String type = "";
	private Calendar lastVisitDate;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public Calendar getLastVisitDate() {
		return lastVisitDate;
	}
	
	public void setLastVisitDate(Calendar date) {
		this.lastVisitDate = date;
	}
	
	public String toString() {
		return getName();
	}

	
}
