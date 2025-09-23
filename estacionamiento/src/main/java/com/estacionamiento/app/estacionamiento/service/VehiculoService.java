package com.estacionamiento.app.estacionamiento.service;

import com.estacionamiento.app.estacionamiento.entities.Vehiculo;

public interface VehiculoService {

    Vehiculo save(Vehiculo vehiculo);
    
    Vehiculo findByPlaca(String placa);

}
