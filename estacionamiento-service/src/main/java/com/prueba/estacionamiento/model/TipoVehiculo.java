package com.prueba.estacionamiento.model;

public enum TipoVehiculo {
	OFICIAL("Oficial"),
    RESIDENTE("Residente"),
    NO_RESIDENTE("No Residente");

    private final String valor;

    TipoVehiculo(String valor) {
        this.valor = valor;
    }

    public String getValor() {
        return valor;
    }
}
