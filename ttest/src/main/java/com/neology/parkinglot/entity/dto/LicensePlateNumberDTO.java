package com.neology.parkinglot.entity.dto;

import java.io.Serializable;

import jakarta.persistence.Entity;
import lombok.Data;

public class LicensePlateNumberDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3793123905497700523L;
	private String vpNumber;
	
	public String getVpNumber() {
		return vpNumber;
	}
	
	public void setVpNumber(String vpNumber) {
		this.vpNumber = vpNumber;
	}
	@Override
	public String toString() {
		return "LicensePlateNumberDTO [vpNumber=" + vpNumber + "]";
	}
	
	
}
