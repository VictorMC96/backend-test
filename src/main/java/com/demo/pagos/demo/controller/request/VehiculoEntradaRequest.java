package com.demo.pagos.demo.controller.request;

public class VehiculoEntradaRequest {
    private String placa;

    // Constructor, getters y setters
    public VehiculoEntradaRequest() {}

    public VehiculoEntradaRequest(String placa) {
        this.placa = placa;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
}
