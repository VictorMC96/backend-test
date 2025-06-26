package com.daniel_lopez_rivera.backend_test.util;

import java.util.Calendar;

public class TimeUtils {
    public static int difEnMinutos(Calendar inicio, Calendar fin) {
        long diffMillis = fin.getTimeInMillis() - inicio.getTimeInMillis();
        return (int) (diffMillis / (1000 * 60));
    }
}
