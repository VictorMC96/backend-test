package com.mx.estacionamiento.estacinamiento.dto;

import com.mx.estacionamiento.estacinamiento.model.VehicleType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class VehicleRequest {

    private String plate;
    private VehicleType type;

}
