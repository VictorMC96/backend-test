package com.mx.estacionamiento.estacinamiento.util;

import java.util.Calendar;

public class DateUtils {

    public static int diffInMinutes(Calendar start, Calendar end) {
        long diffMs = end.getTimeInMillis() - start.getTimeInMillis();
        return (int) (diffMs / (1000 * 60));
    }

}
