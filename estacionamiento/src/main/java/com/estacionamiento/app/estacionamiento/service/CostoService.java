package com.estacionamiento.app.estacionamiento.service;

import com.estacionamiento.app.estacionamiento.entities.Costo;

public interface CostoService {

    Costo save(Costo costo);
    Costo getByTipo(String tipo);
}
