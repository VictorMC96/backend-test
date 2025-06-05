package com.neology.parkinglot.entity.vehicles;

import java.io.Serializable;

public class Amount implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6278551939184918401L;
	private Amount() {
	}
	private static final String MONTAMOUNT ="0.05";
	private static final String CHECKOUTAMOUNT="0.5";
	
	public static String getMontAmount() {
		return MONTAMOUNT;
	}

	public static String getCheckoutAmount() {
		return CHECKOUTAMOUNT;
	}
}
