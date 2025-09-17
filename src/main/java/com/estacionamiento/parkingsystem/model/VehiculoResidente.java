package com.estacionamiento.parkingsystem.model;

import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Entity
public class VehiculoResidente extends Vehiculo{
    private int tiempoAcumuladoMinutos = 0;

    // Constructor vacío que llama al constructor vacío de la clase padre
    public VehiculoResidente() {
        super();
    }

    // Constructor con placa y tipo
    public VehiculoResidente(String placa) {
        super(placa, TipoVehiculo.RESIDENTE);
    }
}
