package com.neology.estacionamiento.web.request;

/**
 * DTO para la solicitud de entrada de un vehículo al estacionamiento
 */
public class EntradaRequest {

    private String placa;

    public EntradaRequest() {}

    public EntradaRequest(String placa) {
        this.placa = placa;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public String toString() {
        return "EntradaRequest{placa='" + placa + "'}";
    }
} 