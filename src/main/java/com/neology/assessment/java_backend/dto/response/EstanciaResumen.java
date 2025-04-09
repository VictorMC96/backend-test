package com.neology.assessment.java_backend.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class EstanciaResumen {
    private String placa;
    private Integer tiempoTotalMinutos;
    private Double montoTotalPagar;

    public EstanciaResumen(String placa, Integer tiempoTotalMinutos, Double montoTotalPagar) {
        this.placa = placa;
        this.tiempoTotalMinutos = tiempoTotalMinutos;
        this.montoTotalPagar = montoTotalPagar;
    }
}
