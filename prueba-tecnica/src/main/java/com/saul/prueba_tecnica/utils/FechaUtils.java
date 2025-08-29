package com.saul.prueba_tecnica.utils;

import java.util.Calendar;

public class FechaUtils {

    /**
     * Obtiene la diferencia en minutos entre dos fechas
     *
     * @param inicial fecha inicial
     * @param fin fecha final
     * @return diferencia fin - inicial en minutos
     */
    public static int difEnMinutos(Calendar inicial, Calendar fin) {
        long diffMillis = fin.getTimeInMillis() - inicial.getTimeInMillis();
        return (int) (diffMillis / (1000 * 60));
    }
}
