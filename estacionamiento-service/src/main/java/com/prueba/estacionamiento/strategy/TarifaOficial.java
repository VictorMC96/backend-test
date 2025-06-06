package com.prueba.estacionamiento.strategy;

import java.math.BigDecimal;
import java.util.Calendar;

public class TarifaOficial implements TarifaStrategy {
    @Override
    public BigDecimal calcular(Calendar entrada, Calendar salida) {
        return BigDecimal.ZERO;
    }
}