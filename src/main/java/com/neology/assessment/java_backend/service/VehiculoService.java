package com.neology.assessment.java_backend.service;

import com.neology.assessment.java_backend.dto.TipoVehiculoEnum;
import com.neology.assessment.java_backend.dto.request.VehiculoRequest;
import com.neology.assessment.java_backend.entity.Vehiculo;
import com.neology.assessment.java_backend.exception.BusinessException;

import java.sql.SQLException;

public interface VehiculoService {
    Vehiculo registrarVehiculo(VehiculoRequest vehiculoRequest, TipoVehiculoEnum tipoVehiculo) throws BusinessException;
}
