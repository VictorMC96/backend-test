package com.example.abrahamtest.Parking_management.util;

import java.util.Calendar;

public class DateTool {


    public static int difEnMinutos(Calendar inicial, Calendar fin) {
        long diffInMillis = fin.getTimeInMillis() - inicial.getTimeInMillis();
        return (int) (diffInMillis / (60 * 1000));
    }
}
