package com.neology.estacionamiento.domain.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * Implementación concreta para vehículos no residentes.
 * Los no residentes pagan a la salida a razón de $0.50 por minuto.
 */
@Entity
@DiscriminatorValue("NO_RESIDENTE")
public class VehiculoNoResidente extends Vehiculo {

    private static final BigDecimal TARIFA_POR_MINUTO = new BigDecimal("0.50");

    // Constructor sin argumentos requerido por JPA
    protected VehiculoNoResidente() {
        super();
    }

    public VehiculoNoResidente(String placa) {
        super(placa, TipoVehiculo.NO_RESIDENTE);
    }

    /**
     * Calcula el importe basado en la tarifa de no residentes: $0.50 por minuto
     */
    @Override
    public BigDecimal calcularImporte(int minutos) {
        return TARIFA_POR_MINUTO.multiply(new BigDecimal(minutos));
    }

    public static BigDecimal getTarifaPorMinuto() {
        return TARIFA_POR_MINUTO;
    }

    @Override
    public String toString() {
        return "VehiculoNoResidente{" +
                "placa='" + getPlaca() + '\'' +
                ", fechaAlta=" + getFechaAlta() +
                '}';
    }
} 