package com.backendtest.demo.dto;

import lombok.Data;

@Data
public class InformePagoResidente {

    private String placa;
    private int tiempoEstacionadoMin;
    private double cantidadAPagar;

}
