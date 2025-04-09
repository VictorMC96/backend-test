package com.neology.assessment.java_backend.service.impl;

import com.neology.assessment.java_backend.controller.BusinessMessage;
import com.neology.assessment.java_backend.dto.TipoVehiculoEnum;
import com.neology.assessment.java_backend.dto.request.VehiculoRequest;
import com.neology.assessment.java_backend.entity.Vehiculo;
import com.neology.assessment.java_backend.exception.BusinessException;
import com.neology.assessment.java_backend.mapper.VehiculoMapper;
import com.neology.assessment.java_backend.repository.VehiculoRepository;
import com.neology.assessment.java_backend.service.VehiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;



@Service
public class VehiculoServiceImpl implements VehiculoService {

    @Autowired
    VehiculoRepository vehiculoRepository;

    @Override
    public Vehiculo registrarVehiculo(VehiculoRequest vehiculoRequest, TipoVehiculoEnum tipoVehiculoEnum) throws BusinessException {
        Vehiculo vehiculo = VehiculoMapper.requestToEntity(vehiculoRequest, tipoVehiculoEnum);
        try {
            vehiculoRepository.save(vehiculo);
        }
        catch (DataIntegrityViolationException e){
            throw new BusinessException(e.getMessage(), BusinessMessage.PLACA_REGISTRADA);
        }
        return vehiculo;
    }


}
