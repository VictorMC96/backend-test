package com.neology.estacionamiento.application.dto;

import com.neology.estacionamiento.domain.model.Vehiculo;
import com.neology.estacionamiento.domain.model.VehiculoResidente;
import java.util.Date;

/**
 * DTO para información de vehículos
 * Contiene la lógica de transformación de entidades a DTOs
 */
public class VehiculoInfo {
    private String placa;
    private String tipo;
    private String descripcionTipo;
    private Date fechaAlta;
    private Integer tiempoAcumuladoMes; // Solo para residentes

    public VehiculoInfo(Vehiculo vehiculo) {
        this.placa = vehiculo.getPlaca();
        this.tipo = vehiculo.getTipo().name();
        this.descripcionTipo = vehiculo.getTipo().getDescripcion();
        this.fechaAlta = vehiculo.getFechaAlta() != null ? vehiculo.getFechaAlta().getTime() : null;
        
        if (vehiculo instanceof VehiculoResidente) {
            this.tiempoAcumuladoMes = ((VehiculoResidente) vehiculo).getTiempoAcumuladoMes();
        }
    }

    // Getters
    public String getPlaca() { return placa; }
    public String getTipo() { return tipo; }
    public String getDescripcionTipo() { return descripcionTipo; }
    public Date getFechaAlta() { return fechaAlta; }
    public Integer getTiempoAcumuladoMes() { return tiempoAcumuladoMes; }
} 