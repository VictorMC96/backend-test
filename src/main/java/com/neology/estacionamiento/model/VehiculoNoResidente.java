package com.neology.estacionamiento.model;

import jakarta.persistence.Entity;
import lombok.EqualsAndHashCode;

@Entity
@EqualsAndHashCode(callSuper = true)
public class VehiculoNoResidente extends Vehiculo {

}
