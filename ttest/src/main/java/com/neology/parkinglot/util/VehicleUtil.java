package com.neology.parkinglot.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import com.neology.parkinglot.entity.vehicles.Amount;
import com.neology.parkinglot.entity.vehicles.Vehicle;

public class VehicleUtil {
	
	public static int difminutes(Calendar checkIn, Calendar checkOut) {
        long millisDiff = checkOut.getTimeInMillis() - checkIn.getTimeInMillis();
        return (int) (millisDiff / (1000 * 60));
    }

    public static LocalDateTime convertToLocalDateTime(Calendar calendar) {
        return LocalDateTime.ofInstant(calendar.toInstant(), ZoneId.systemDefault());
    }

    public static void processVehicle(String tipo, Calendar checkIn, Calendar checkOut, Vehicle vehicle) {
        switch (tipo.toLowerCase()) {
            case "OFICIAL":
            	vehicle.setCheckIn(Date.from(convertToLocalDateTime(checkIn).atZone(ZoneId.systemDefault()).toInstant()));
            	
            	vehicle.setCheckOut(Date.from(convertToLocalDateTime(checkOut).atZone(ZoneId.systemDefault()).toInstant()));
                break;

            case "RESIDENT":
            	Integer duration = difminutes(checkIn, checkOut);
                Integer acumulated = vehicle.getTotalTime() != null ? vehicle.getTotalTime() : 0;
                vehicle.setTotalTime(acumulated + duration);
                break;

            case "NORESIDENT":
            	Integer minutos = difminutes(checkIn, checkOut);
                double amountPerMinute = Double.valueOf(Amount.getCheckoutAmount());
                double amount = minutos * amountPerMinute;
                vehicle.setTotal(amount);
                break;

            default:
                System.out.println("NOT FOUND.");
                break;
        }
    }
}
