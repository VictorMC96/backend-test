package com.neology.estacionamiento.web.request;

/**
 * DTO para la solicitud de registro de un vehículo (oficial o residente)
 */
public class RegistroVehiculoRequest {

    private String placa;

    public RegistroVehiculoRequest() {}

    public RegistroVehiculoRequest(String placa) {
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
        return "RegistroVehiculoRequest{placa='" + placa + "'}";
    }
} 