package com.neology.estacionamiento.web.response;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * DTO para la respuesta de registro de salida con importe calculado
 */
public class SalidaResponse {

    private String mensaje;
    private String placa;
    private String tipoVehiculo;
    private Date fechaEntrada;
    private Date fechaSalida;
    private int minutosEstancia;
    private BigDecimal importeAPagar;

    public SalidaResponse() {}

    public SalidaResponse(String mensaje, String placa, String tipoVehiculo, 
                         Calendar fechaEntrada, Calendar fechaSalida, 
                         int minutosEstancia, BigDecimal importeAPagar) {
        this.mensaje = mensaje;
        this.placa = placa;
        this.tipoVehiculo = tipoVehiculo;
        this.fechaEntrada = fechaEntrada != null ? fechaEntrada.getTime() : null;
        this.fechaSalida = fechaSalida != null ? fechaSalida.getTime() : null;
        this.minutosEstancia = minutosEstancia;
        this.importeAPagar = importeAPagar;
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

    public Date getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(Date fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public int getMinutosEstancia() {
        return minutosEstancia;
    }

    public void setMinutosEstancia(int minutosEstancia) {
        this.minutosEstancia = minutosEstancia;
    }

    public BigDecimal getImporteAPagar() {
        return importeAPagar;
    }

    public void setImporteAPagar(BigDecimal importeAPagar) {
        this.importeAPagar = importeAPagar;
    }

    @Override
    public String toString() {
        return "SalidaResponse{" +
                "mensaje='" + mensaje + '\'' +
                ", placa='" + placa + '\'' +
                ", tipoVehiculo='" + tipoVehiculo + '\'' +
                ", fechaEntrada=" + fechaEntrada +
                ", fechaSalida=" + fechaSalida +
                ", minutosEstancia=" + minutosEstancia +
                ", importeAPagar=" + importeAPagar +
                '}';
    }
} 