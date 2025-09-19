package com.estacionamiento.parkingsystem.model;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
public class VehiculoOficial extends Vehiculo{

    public VehiculoOficial() {
        super();
    }

    public VehiculoOficial(String placa) {
        super(placa, TipoVehiculo.OFICIAL);
    }

}
