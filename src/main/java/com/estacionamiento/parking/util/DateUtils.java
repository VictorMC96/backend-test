package com.estacionamiento.parking.util;

import java.util.Calendar;

public class DateUtils {

    public static int difEnMinutos(Calendar inicial, Calendar fin) {
        long diffMillis = fin.getTimeInMillis() - inicial.getTimeInMillis();
        long minutes = diffMillis / (1000L * 60L);
        return (int) minutes;
    }

    public static Calendar now() {
        return Calendar.getInstance();
    }
}