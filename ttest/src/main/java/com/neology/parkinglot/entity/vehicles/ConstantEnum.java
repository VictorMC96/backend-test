package com.neology.parkinglot.entity.vehicles;

import java.text.DecimalFormat;

public enum ConstantEnum{

	MONTAMOUNT("0.05"),
	CHECKOUTAMOUNT("0.5"),
	FORMAT("#0.00");
	
	private String value;
	 
	ConstantEnum(String value) {
	   this.value = value;
	}
	 
	public String getValue() {
	   return this.value;
	}

}
