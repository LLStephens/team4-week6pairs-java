package com.techelevator.model;

import java.sql.Date;
import java.time.LocalDate;

public class Park {
	private int parkId;
	private String name;
	private String location;
	private Date establishDate;
	private int area;
	private int visitors;
	private String description;
	
	public int getParkId() {
		return parkId;
	}
	public void setParkId(int parkId) {
		this.parkId = parkId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getEstablishDate() {
		return establishDate;
	}
	public void setEstablishDate(Date date) {
		this.establishDate = date;
	}
	public int getArea() {
		return area;
	}
	public void setArea(int area) {
		this.area = area;
	}
	public int getVisitors() {
		return visitors;
	}
	public void setVisitors(int visitors) {
		this.visitors = visitors;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return name;
	}
	@Override
	public boolean equals(Object other) {
		if(other == null) {
			return false;
		} else if(!(other instanceof Park)) {
			return false;
		} else {
			Park otherPark = (Park)other;
			return this.parkId==(otherPark.parkId) ;
		}
	}
	
}
