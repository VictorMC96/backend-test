package com.neology.parkinglot.entity.vehicles;

import java.io.Serializable;
import java.util.Date;

public class Vehicle implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2028007581524946842L;
	private String vpNumber;
	private Date checkIn;
	private Date checkOut;
	private Double total;
	private Integer totalTime;
	private String type;
	
	public String getVpNumber() {
		return vpNumber;
	}
	public void setVpNumber(String vpNumber) {
		this.vpNumber = vpNumber;
	}
	public Date getCheckIn() {
		return checkIn;
	}
	public void setCheckIn(Date checkIn) {
		this.checkIn = checkIn;
	}
	public Date getCheckOut() {
		return checkOut;
	}
	public void setCheckOut(Date checkOut) {
		this.checkOut = checkOut;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public Integer getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(Integer totalTime) {
		this.totalTime = totalTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Vehicle [vpNumber=" + vpNumber + ", checkIn=" + checkIn + ", checkOut=" + checkOut + ", total=" + total
				+ ", totalTime=" + totalTime + ", type=" + type + "]";
	}
	
}
