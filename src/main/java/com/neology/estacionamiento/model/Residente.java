package com.neology.estacionamiento.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;

@Entity
@Data
@DiscriminatorValue("RESIDENTE")
public class Residente extends Vehiculo {
    private int minutosAcumulados;
}
