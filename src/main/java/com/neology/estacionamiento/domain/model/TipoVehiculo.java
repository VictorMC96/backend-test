package com.neology.estacionamiento.domain.model;

/**
 * Enumeración para los diferentes tipos de vehículos en el estacionamiento
 */
public enum TipoVehiculo {
    OFICIAL("Vehículo oficial - No paga estacionamiento"),
    RESIDENTE("Vehículo de residente - Paga a fin de mes"),
    NO_RESIDENTE("Vehículo no residente - Paga a la salida");

    private final String descripcion;

    TipoVehiculo(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
} 