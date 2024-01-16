package com.backendtest.demo.service;

import com.backendtest.demo.entity.Vehiculo;
import com.backendtest.demo.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VehiculoService {

    @Autowired
    private VehiculoRepository vehiculoRepository;

    public void darDeAltaVehiculoOficial(Vehiculo vehiculo) {
        vehiculoRepository.save(vehiculo);
    }

    public void darDeAltaVehiculoResidente(Vehiculo vehiculo) {
        vehiculoRepository.save(vehiculo);
    }
}
