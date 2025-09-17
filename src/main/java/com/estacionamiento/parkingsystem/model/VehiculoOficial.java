package com.estacionamiento.parkingsystem.model;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
public class VehiculoOficial extends Vehiculo{

    // Constructor vacío que llama al constructor vacío de la clase padre
    public VehiculoOficial() {
        super();
    }

    // Constructor con placa y tipo
    public VehiculoOficial(String placa) {
        super(placa, TipoVehiculo.OFICIAL);
    }

}
