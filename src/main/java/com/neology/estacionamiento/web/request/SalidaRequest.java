package com.neology.estacionamiento.web.request;

/**
 * DTO para la solicitud de salida de un vehículo del estacionamiento
 */
public class SalidaRequest {

    private String placa;

    public SalidaRequest() {}

    public SalidaRequest(String placa) {
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
        return "SalidaRequest{placa='" + placa + "'}";
    }
} 