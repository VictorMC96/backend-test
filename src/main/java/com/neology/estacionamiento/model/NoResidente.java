package com.neology.estacionamiento.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("NO_RESIDENTE")
public class NoResidente extends Vehiculo {}
