package com.accesoVehicular.neology.service;

import com.accesoVehicular.neology.dto.RegistroVehiculo;

import com.accesoVehicular.neology.model.TipoVehiculo;
import com.accesoVehicular.neology.model.Vehiculo;
import com.accesoVehicular.neology.repository.IVehiculoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    private final IVehiculoRepository vehicleRepository;
    private final TipoVehiculoService tipoVehiculoService;

    public VehiculoService(IVehiculoRepository vehicleRepository, TipoVehiculoService tipoVehiculoService) {
        this.vehicleRepository = vehicleRepository;
        this.tipoVehiculoService = tipoVehiculoService;
    }

    @Transactional
    public Vehiculo registrarVehiculo(RegistroVehiculo request) throws Exception {
        if (request == null || request.getPlaca() == null || request.getPlaca().isEmpty()) {
            throw new Exception("Campos faltantes en la solicitud de registro de vehiculo");
        }

        if (vehicleRepository.findByPlaca(request.getPlaca()).isPresent()) {
            throw new Exception("Vehiculo ya registrado con la placa: " + request.getPlaca());
        }

        Vehiculo vehiculo = new Vehiculo(
                request.getPlaca(),
                request.getTipoVehiculo(),
                true
        );

        Vehiculo persisted = vehicleRepository.saveAndFlush(vehiculo);
        return persisted;
    }

    @Transactional
    public Vehiculo registrarVehiculo(String placa, String tipo_vehiculo) throws Exception {
        if (placa == null || tipo_vehiculo == null || placa.isEmpty() || tipo_vehiculo.isEmpty()) {
            throw new Exception("Campos faltantes en la solicitud de registro de vehiculo");
        }

        TipoVehiculo tipoVehiculo = this.tipoVehiculoService.findByNombre(tipo_vehiculo);

        if (vehicleRepository.findByPlaca(placa).isPresent()) {
            throw new Exception("Vehiculo ya registrado con la placa: " + placa);
        }

        Vehiculo vehiculo = new Vehiculo(
                placa,
                tipoVehiculo,
                true
        );

        Vehiculo persisted = vehicleRepository.saveAndFlush(vehiculo);
        return persisted;
    }

    @Transactional
    public Vehiculo getByPlaca(String placa) {
        return this.vehicleRepository.findByPlaca(placa).orElse(null);
    }

    @Transactional
    public List<Vehiculo> getAll() {
        return vehicleRepository.findAll();
    }

    @Transactional
    public List<Vehiculo> getByTipoVehiculoNombre(String tipoVehiculoNombre) {
        return vehicleRepository.findByTipoVehiculoNombre(tipoVehiculoNombre);
    }


}
