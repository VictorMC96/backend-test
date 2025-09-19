package com.estacionamiento.parkingsystem.model;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
public class VehiculoNoResidente extends  Vehiculo{

    public VehiculoNoResidente() {
        super();
    }

    public VehiculoNoResidente(String placa) {
        super(placa, TipoVehiculo.NO_RESIDENTE);
    }
}
