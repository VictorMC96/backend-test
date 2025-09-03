package com.prueba.estacionamiento.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.prueba.estacionamiento.model.TarifaRequest;
import com.prueba.estacionamiento.model.TipoVehiculo;
import com.prueba.estacionamiento.strategy.TarifaNoResidente;
import com.prueba.estacionamiento.strategy.TarifaOficial;
import com.prueba.estacionamiento.strategy.TarifaResidente;
import com.prueba.estacionamiento.strategy.TarifaStrategy;

@Service
public class TarifaService {
	private final Map<TipoVehiculo, TarifaStrategy> estrategias = new HashMap<>();

    public TarifaService() {
        estrategias.put(TipoVehiculo.OFICIAL, new TarifaOficial());
        estrategias.put(TipoVehiculo.RESIDENTE, new TarifaResidente());
        estrategias.put(TipoVehiculo.NO_RESIDENTE, new TarifaNoResidente());
    }

    public BigDecimal calcularTarifa(TarifaRequest request) {
        TarifaStrategy estrategia = estrategias.get(request.getTipo());
        if (estrategia == null) {
            throw new UnsupportedOperationException("Tipo de vehículo no soportado: " + request.getTipo());
        }
        return estrategia.calcular(request.getEntrada(), request.getSalida());
    }
}
