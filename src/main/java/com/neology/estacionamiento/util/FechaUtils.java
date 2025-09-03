package com.neology.estacionamiento.util;

import java.time.Duration;
import java.time.LocalDateTime;

public class FechaUtils {

    /**
     * Obtiene la diferencia en minutos entre dos fechas tipo LocalDateTime.
     * @param inicial Fecha inicial
     * @param fin Fecha final
     * @return diferencia en minutos
     */
    public static int difEnMinutos(LocalDateTime inicial, LocalDateTime fin) {
        return (int) Duration.between(inicial, fin).toMinutes();
    }
}
