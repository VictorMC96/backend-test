package com.mx.estacionamiento.estacinamiento.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaymentReportDTO {

    private String plate;
    private int minutes;
    private double amount;

}
