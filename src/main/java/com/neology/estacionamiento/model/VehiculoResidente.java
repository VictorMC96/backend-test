package com.neology.estacionamiento.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class VehiculoResidente extends Vehiculo {
    private long tiempoAcumuladoMinutos;
}