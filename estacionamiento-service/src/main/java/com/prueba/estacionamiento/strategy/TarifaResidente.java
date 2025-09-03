package com.prueba.estacionamiento.strategy;

import java.math.BigDecimal;
import java.util.Calendar;

public class TarifaResidente implements TarifaStrategy {
    private static final BigDecimal PRECIO_MINUTO = new BigDecimal("0.05");

    @Override
    public BigDecimal calcular(Calendar entrada, Calendar salida) {
        long diffMillis = salida.getTimeInMillis() - entrada.getTimeInMillis();
        long minutos = diffMillis / (60 * 1000);
        return PRECIO_MINUTO.multiply(BigDecimal.valueOf(minutos));
    }
}
