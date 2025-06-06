package com.prueba.estacionamiento.strategy;

import java.math.BigDecimal;
import java.util.Calendar;

public interface TarifaStrategy {
	BigDecimal calcular(Calendar entrada, Calendar salida);
}