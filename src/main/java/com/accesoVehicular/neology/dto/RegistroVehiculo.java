package com.accesoVehicular.neology.dto;

import com.accesoVehicular.neology.model.TipoVehiculo;

public class RegistroVehiculo {
    private String placa;
    private TipoVehiculo tipoVehiculo;


    public RegistroVehiculo() {
    }

    public RegistroVehiculo(String placa, TipoVehiculo tipoVehiculo) {
        this.placa = placa;
        this.tipoVehiculo = tipoVehiculo;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }
}
