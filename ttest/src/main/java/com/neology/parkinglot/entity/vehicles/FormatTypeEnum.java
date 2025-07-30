package com.neology.parkinglot.entity.vehicles;

public enum FormatTypeEnum {
	OFICIAL,
	RESIDENT,
	NORESIDENT;
	
	public static FormatTypeEnum getFormatTypeEnumFromString(String format) {
		for (FormatTypeEnum formatTypeEnum : FormatTypeEnum.values()) {
			if (formatTypeEnum.name().equals(format))
				return formatTypeEnum;
		}
		return null;
	}
}
