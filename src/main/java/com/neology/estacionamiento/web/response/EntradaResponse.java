package com.neology.estacionamiento.web.response;

import java.util.Calendar;
import java.util.Date;

/**
 * DTO para la respuesta de entrada con información de la estancia
 */
public class EntradaResponse {

    private String mensaje;
    private String placa;
    private String tipoVehiculo;
    private Date fechaEntrada; // Usar Date para JSON serialization

    public EntradaResponse() {}

    public EntradaResponse(String mensaje, String placa, String tipoVehiculo, Calendar fechaEntrada) {
        this.mensaje = mensaje;
        this.placa = placa;
        this.tipoVehiculo = tipoVehiculo;
        this.fechaEntrada = fechaEntrada != null ? fechaEntrada.getTime() : null;
    }

    // Getters y Setters
    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public Date getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(Date fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    @Override
    public String toString() {
        return "EntradaResponse{" +
                "mensaje='" + mensaje + '\'' +
                ", placa='" + placa + '\'' +
                ", tipoVehiculo='" + tipoVehiculo + '\'' +
                ", fechaEntrada=" + fechaEntrada +
                '}';
    }
} 