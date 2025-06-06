package com.prueba.estacionamiento.strategy;

import java.math.BigDecimal;
import java.util.Calendar;

public class TarifaNoResidente implements TarifaStrategy {
    private static final BigDecimal PRECIO_MINUTO = new BigDecimal("0.5");

    @Override
    public BigDecimal calcular(Calendar entrada, Calendar salida) {
        long diffMillis = salida.getTimeInMillis() - entrada.getTimeInMillis();
        long minutos = diffMillis / (60 * 1000);
        return PRECIO_MINUTO.multiply(BigDecimal.valueOf(minutos));
    }
}
