package com.neology.app.context.shaed;

import java.util.Calendar;

public class DateUtils {
    public static int difEnMinutes(Calendar entry, Calendar exit) {
        long diffMillis = exit.getTimeInMillis() - entry.getTimeInMillis();
        long minutes = diffMillis / (1000L * 60L);
        return (int) minutes;
    }
}
