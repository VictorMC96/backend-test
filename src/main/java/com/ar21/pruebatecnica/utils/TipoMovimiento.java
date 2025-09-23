package com.ar21.pruebatecnica.utils;

public enum TipoMovimiento {

    ENTRADA("ENTRADA"),
    SALIDA("SALIDA"),
    TOTAL_PAGAR(" : $ 50");

    private String value;

    private TipoMovimiento(String value){
        this.value=value;
    }

    public String getValue(){
        return value;
    }
}
