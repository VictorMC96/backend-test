package com.ar21.pruebatecnica.utils;

public enum TipoVehiculo {

    OFICIAL("OFICIAL"),
    RESIDENTE("RESIDENTE"),
    NO_RESIDENTE("NO_RESIDENTE");

    private String value;


    public String getValue(){

        return value;

    }

    TipoVehiculo(String value) {
        this.value = value;
    }
}
