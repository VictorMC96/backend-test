package com.neology.assessment.java_backend.mapper;

import com.neology.assessment.java_backend.dto.TipoVehiculoEnum;
import com.neology.assessment.java_backend.dto.request.VehiculoRequest;
import com.neology.assessment.java_backend.entity.TipoVehiculo;
import com.neology.assessment.java_backend.entity.Vehiculo;

import java.util.Calendar;

public class VehiculoMapper {

    public static Vehiculo requestToEntity(VehiculoRequest vehiculoRequest, TipoVehiculoEnum tipoVehiculoEnum) {
        Vehiculo vehiculo =  new Vehiculo();
        vehiculo.setPlaca(vehiculoRequest.getPlaca());
        vehiculo.setFechaRegistro(Calendar.getInstance());
        TipoVehiculo tipoVehiculo = new TipoVehiculo();
        tipoVehiculo.setId(tipoVehiculoEnum.getCodigo());
        vehiculo.setTipoVehiculo(tipoVehiculo);
        return vehiculo;
    }
}
