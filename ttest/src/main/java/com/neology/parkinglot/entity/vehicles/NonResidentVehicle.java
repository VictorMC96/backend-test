package com.neology.parkinglot.entity.vehicles;

import lombok.Data;

@Data
public class NonResidentVehicle extends Vehicle{
		/**
	 * 
	 */
	private static final long serialVersionUID = 4634474757677546288L;
	private Integer checkout;
}
