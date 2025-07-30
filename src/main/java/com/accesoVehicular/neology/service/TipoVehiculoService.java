package com.accesoVehicular.neology.service;


import com.accesoVehicular.neology.model.TipoVehiculo;
import com.accesoVehicular.neology.repository.ITipoVehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TipoVehiculoService {

    private final ITipoVehiculoRepository tipoVehiculoRepository;

    public TipoVehiculoService(ITipoVehiculoRepository tipoVehiculoRepository) {
        this.tipoVehiculoRepository = tipoVehiculoRepository;
    }

    @Transactional
    public List<TipoVehiculo> getAll() {
        return tipoVehiculoRepository.findAll();
    }

    @Transactional
    public TipoVehiculo findByNombre(String nombre) {
        return tipoVehiculoRepository.findByNombre(nombre)
                .orElseThrow(() -> new RuntimeException("Tipo de vehiculo no encontrado: " + nombre));
    }

}
