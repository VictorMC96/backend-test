package com.neology.estacionamiento.application.dto;

import com.neology.estacionamiento.domain.model.Estancia;
import java.util.Date;

public class VehiculoActivoInfo {
    private String placa;
    private String tipoVehiculo;
    private Date fechaEntrada;
    private int minutosTranscurridos;

    public VehiculoActivoInfo(Estancia estancia) {
        this.placa = estancia.getVehiculo().getPlaca();
        this.tipoVehiculo = estancia.getVehiculo().getTipo().name();
        this.fechaEntrada = estancia.getFechaEntrada() != null ? estancia.getFechaEntrada().getTime() : null;
        this.minutosTranscurridos = estancia.calcularMinutosTranscurridos();
    }

    // Constructor manual para casos específicos
    public VehiculoActivoInfo(String placa, String tipoVehiculo, 
                             Date fechaEntrada, int minutosTranscurridos) {
        this.placa = placa;
        this.tipoVehiculo = tipoVehiculo;
        this.fechaEntrada = fechaEntrada;
        this.minutosTranscurridos = minutosTranscurridos;
    }

    // Getters
    public String getPlaca() { return placa; }
    public String getTipoVehiculo() { return tipoVehiculo; }
    public Date getFechaEntrada() { return fechaEntrada; }
    public int getMinutosTranscurridos() { return minutosTranscurridos; }
} 