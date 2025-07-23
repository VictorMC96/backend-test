package com.neology.estacionamiento.application.dto;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

/**
 * DTO para información de deuda de residentes
 */
public class DeudaResidenteInfo {
    private String placa;
    private BigDecimal deuda;
    private Date fechaConsulta;

    public DeudaResidenteInfo(String placa, BigDecimal deuda) {
        this.placa = placa;
        this.deuda = deuda;
        this.fechaConsulta = Calendar.getInstance().getTime();
    }

    // Getters
    public String getPlaca() { return placa; }
    public BigDecimal getDeuda() { return deuda; }
    public Date getFechaConsulta() { return fechaConsulta; }
} 