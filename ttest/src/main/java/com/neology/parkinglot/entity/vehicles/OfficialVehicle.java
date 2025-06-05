package com.neology.parkinglot.entity.vehicles;

import lombok.Data;

@Data
public class OfficialVehicle extends Vehicle{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7996902504185904580L;
	private Double finalCheckOutAmount;
}
