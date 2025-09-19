package com.estacionamiento.parkingsystem.model;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
public class VehiculoResidente extends Vehiculo{
    private int tiempoAcumuladoMinutos = 0;

    public VehiculoResidente() {
        super();
    }

    public VehiculoResidente(String placa) {
        super(placa, TipoVehiculo.RESIDENTE);
    }
}
