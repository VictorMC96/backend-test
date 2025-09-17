package com.estacionamiento.parkingsystem.model;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
public class VehiculoNoResidente extends  Vehiculo{
    // Constructor vacío que llama al constructor vacío de la clase padre
    public VehiculoNoResidente() {
        super();
    }

    // Constructor con placa y tipo
    public VehiculoNoResidente(String placa) {
        super(placa, TipoVehiculo.NO_RESIDENTE);
    }
}
