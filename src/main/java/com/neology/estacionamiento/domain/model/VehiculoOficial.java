package com.neology.estacionamiento.domain.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * Implementación concreta para vehículos oficiales.
 * Los vehículos oficiales no pagan estacionamiento pero se registran sus estancias.
 */
@Entity
@DiscriminatorValue("OFICIAL")
public class VehiculoOficial extends Vehiculo {

    // Constructor sin argumentos requerido por JPA
    protected VehiculoOficial() {
        super();
    }

    public VehiculoOficial(String placa) {
        super(placa, TipoVehiculo.OFICIAL);
    }

    /**
     * Los vehículos oficiales no pagan estacionamiento
     */
    @Override
    public BigDecimal calcularImporte(int minutos) {
        return BigDecimal.ZERO;
    }

    @Override
    public String toString() {
        return "VehiculoOficial{" +
                "placa='" + getPlaca() + '\'' +
                ", fechaAlta=" + getFechaAlta() +
                '}';
    }
} 