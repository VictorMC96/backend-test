package com.neology.estacionamiento.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OFICIAL")
public class Oficial extends Vehiculo {}
