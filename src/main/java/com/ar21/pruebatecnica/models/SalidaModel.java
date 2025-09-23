package com.ar21.pruebatecnica.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalidaModel {

    private String horaEntrada;

    private String horaSalida;

    private double totalEstanciaTotalAcumulado;

    private String totalImporteAPagar;
}
