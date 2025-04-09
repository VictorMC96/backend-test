package com.neology.assessment.java_backend.util;

import java.time.Duration;
import java.util.Calendar;

public class DateUtil {

    /** Obtiene la diferencia en minutos entre dos fechas
     * @param fechaInicial fecha inicial
     * @param fechaFinal fecha final
     * @return diferencia final-inicial en minutos
     */
    public static int difEnMinutos(Calendar fechaInicial, Calendar fechaFinal) {
        Duration duration = Duration.between(fechaInicial.toInstant(), fechaFinal.toInstant());

        // Get the difference in minutes
        return (int) duration.toMinutes();
    }

}
