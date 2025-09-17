package com.estacionamiento.parkingsystem.service;

import com.estacionamiento.parkingsystem.model.TipoVehiculo;
import com.estacionamiento.parkingsystem.model.Vehiculo;
import com.estacionamiento.parkingsystem.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoServiceImpl implements VehiculoService{
    @Autowired
    private VehiculoRepository vehiculoRepository;

    @Override
    public List<Vehiculo> obtenerTodos() {
        return vehiculoRepository.findAll();
    }

    @Override
    public List<Vehiculo> obtenerPorTipo(String tipo) {
        return vehiculoRepository.findByTipo(TipoVehiculo.valueOf(tipo));
    }

    @Override
    public Vehiculo obtenerPorId(Long id) {
        Optional<Vehiculo> vehiculo = vehiculoRepository.findById(id);
        return vehiculo.orElse(null); // Puedes lanzar una excepción si prefieres
    }

    @Override
    public Vehiculo crearVehiculo(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }

    @Override
    public Vehiculo actualizarVehiculo(Long id, Vehiculo vehiculo) {
        if (vehiculoRepository.existsById(id)) {
            vehiculo.setId(id); // Asegúrate de que tu entidad tenga el método setId
            return vehiculoRepository.save(vehiculo);
        }
        return null; // O lanzar excepción
    }

    @Override
    public void eliminarVehiculo(Long id) {
        vehiculoRepository.deleteById(id);
    }
}
