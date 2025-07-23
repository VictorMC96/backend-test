package com.neology.estacionamiento.domain.model;

/**
 * Enumeración para los estados de una estancia en el estacionamiento
 */
public enum EstadoEstancia {
    ACTIVA("Vehículo actualmente en estacionamiento"),
    FINALIZADA("Estancia completada con salida registrada"),
    CANCELADA("Estancia cancelada por error de registro");

    private final String descripcion;

    EstadoEstancia(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }
} 