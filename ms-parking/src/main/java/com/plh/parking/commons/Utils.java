package com.plh.parking.commons;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.TimeZone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {

    public static int diffMinute(Calendar start, Calendar exit) {
        long ms = exit.getTimeInMillis() - start.getTimeInMillis();
        return (int) (ms / (60 * 1000));
    }

    public static Calendar dateNow(){
        return Calendar.getInstance(TimeZone.getTimeZone("America/Mexico_City"));
    }

}
